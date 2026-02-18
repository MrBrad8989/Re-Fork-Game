package com.example.parchismania

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parchismania.data.AppDataStore
import com.example.parchismania.data.BoardSkin
import com.example.parchismania.engine.GameEngine
import com.example.parchismania.engine.GameEngine.PlayerConfig
import com.example.parchismania.engine.GameState
import com.example.parchismania.engine.PlayerColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AiSpeed { SLOW, NORMAL, FAST }

data class GameSettings(
    val aiSpeed: AiSpeed = AiSpeed.NORMAL,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val animationsEnabled: Boolean = true,
    val boardSkin: BoardSkin = BoardSkin.CLASSIC,
)

data class GameStats(val gamesPlayed: Int = 0, val gamesWon: Int = 0)

data class PlayerSetup(
    val playersCount: Int = 2,
    val humanCount: Int = 1,
    val colors: List<PlayerColor> = listOf(PlayerColor.YELLOW, PlayerColor.RED),
)

data class WalletState(
    val coins: Int = 0,
    val ownedSkins: Set<BoardSkin> = setOf(BoardSkin.CLASSIC),
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val store = AppDataStore(application.applicationContext)

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    private val _setup = MutableStateFlow(PlayerSetup())
    val setup: StateFlow<PlayerSetup> = _setup.asStateFlow()

    private val _wallet = MutableStateFlow(WalletState())
    val wallet: StateFlow<WalletState> = _wallet.asStateFlow()

    private val _stats = MutableStateFlow(GameStats())
    val stats: StateFlow<GameStats> = _stats.asStateFlow()

    private val rng = Random

    private var aiJob: Job? = null

    init {
        viewModelScope.launch {
            store.flow.collect { ps ->
                _settings.value = GameSettings(
                    aiSpeed = runCatching { AiSpeed.valueOf(ps.aiSpeed) }.getOrNull() ?: AiSpeed.NORMAL,
                    soundEnabled = ps.soundEnabled,
                    vibrationEnabled = ps.vibrationEnabled,
                    animationsEnabled = ps.animationsEnabled,
                    boardSkin = ps.boardSkin,
                )

                val colors = ps.lastColorsCsv
                    .split(',')
                    .mapNotNull { runCatching { PlayerColor.valueOf(it.trim()) }.getOrNull() }
                _setup.value = PlayerSetup(
                    playersCount = ps.lastPlayersCount.coerceIn(2, 4),
                    humanCount = ps.lastHumanCount.coerceIn(1, 4),
                    colors = (if (colors.size >= 2) colors else listOf(PlayerColor.YELLOW, PlayerColor.RED)).take(4),
                )

                _wallet.value = WalletState(
                    coins = ps.coinsWallet,
                    ownedSkins = ps.ownedSkins,
                )
            }
        }
    }

    fun startQuickGame() {
        startGame(
            listOf(
                PlayerConfig(color = PlayerColor.YELLOW, isAI = false, name = "Tú"),
                PlayerConfig(color = PlayerColor.RED, isAI = true, name = "CPU"),
            )
        )
    }

    fun startGame(configs: List<PlayerConfig>) {
        aiJob?.cancel()
        val state = GameEngine.createInitialState(configs)
        _gameState.value = state
        maybeRunAiTurn(state)
    }

    fun resetGame() {
        aiJob?.cancel()
        _gameState.value = null
    }

    fun updateSettings(partial: GameSettings.() -> GameSettings) {
        _settings.value = _settings.value.partial()

        val s = _settings.value
        viewModelScope.launch {
            store.update { ps ->
                ps.copy(
                    aiSpeed = s.aiSpeed.name,
                    soundEnabled = s.soundEnabled,
                    vibrationEnabled = s.vibrationEnabled,
                    animationsEnabled = s.animationsEnabled,
                    boardSkin = s.boardSkin,
                )
            }
        }
    }

    fun updateSetup(next: PlayerSetup) {
        _setup.value = next
        viewModelScope.launch {
            store.update { ps ->
                ps.copy(
                    lastPlayersCount = next.playersCount,
                    lastHumanCount = next.humanCount,
                    lastColorsCsv = next.colors.joinToString(",") { it.name },
                )
            }
        }
    }

    fun startFromSetup() {
        val st = _setup.value
        val colors = st.colors.distinct().take(st.playersCount).let { list ->
            // ensure enough unique colors
            val all = PlayerColor.entries
            (list + all.filter { it !in list }).take(st.playersCount)
        }
        val configs = colors.mapIndexed { idx, c ->
            val isHuman = idx < st.humanCount
            PlayerConfig(color = c, isAI = !isHuman, name = if (isHuman) "Jugador ${idx + 1}" else "CPU ${idx + 1 - st.humanCount}")
        }
        startGame(configs)
    }

    fun buySkin(skin: BoardSkin, price: Int): Boolean {
        val w = _wallet.value
        if (skin in w.ownedSkins) return true
        if (w.coins < price) return false
        viewModelScope.launch {
            store.update { ps ->
                ps.copy(
                    coinsWallet = maxOf(0, ps.coinsWallet - price),
                    ownedSkins = ps.ownedSkins + skin,
                )
            }
        }
        return true
    }

    fun selectSkin(skin: BoardSkin) {
        if (skin !in _wallet.value.ownedSkins) return
        updateSettings { copy(boardSkin = skin) }
    }

    fun rollDice() {
        val state = _gameState.value ?: return
        if (state.gameOver) return
        if (state.diceRolled) return

        val current = state.players[state.currentPlayerIndex]
        if (current.isAI) return

        val dice = GameEngine.rollDice(rng)
        val withDice = state.copy(
            diceValue = dice,
            diceRolled = true,
            turnPhase = com.example.parchismania.engine.TurnPhase.MOVE,
            message = "Sacaste $dice"
        )

        val movable = GameEngine.getMovablePieces(withDice)
        val updated = withDice.copy(movablePieces = movable)

        if (movable.isEmpty()) {
            _gameState.value = updated.copy(message = "Sacaste $dice - No puedes mover")
            viewModelScope.launch {
                delay(900)
                val skipped = GameEngine.skipTurn(updated)
                _gameState.value = skipped
                maybeRunAiTurn(skipped)
            }
            return
        }

        _gameState.value = updated
    }

    fun movePiece(pieceId: String) {
        val state = _gameState.value ?: return
        if (state.gameOver) return
        if (!state.movablePieces.contains(pieceId)) return

        val current = state.players[state.currentPlayerIndex]
        if (current.isAI) return

        val result = GameEngine.movePiece(state, pieceId)
        _gameState.value = result.newState

        if (result.newState.gameOver) {
            val humanWon = result.newState.winner?.let { winnerColor ->
                result.newState.players.firstOrNull { it.color == winnerColor }?.isAI == false
            } ?: false
            _stats.value = _stats.value.copy(
                gamesPlayed = _stats.value.gamesPlayed + 1,
                gamesWon = _stats.value.gamesWon + if (humanWon) 1 else 0
            )

            // Add earned coins to wallet (sum all players for this match)
            val earned = result.newState.coinsEarned.values.sum()
            viewModelScope.launch {
                store.update { ps -> ps.copy(coinsWallet = ps.coinsWallet + earned) }
            }
            return
        }

        maybeRunAiTurn(result.newState)
    }

    private fun aiDelayMs(): Long {
        return when (_settings.value.aiSpeed) {
            AiSpeed.SLOW -> 1500
            AiSpeed.FAST -> 400
            AiSpeed.NORMAL -> 800
        }
    }

    private fun maybeRunAiTurn(state: GameState) {
        if (state.gameOver) return
        val current = state.players[state.currentPlayerIndex]
        if (!current.isAI) return

        aiJob?.cancel()
        aiJob = viewModelScope.launch {
            delay(aiDelayMs())

            val dice = GameEngine.rollDice(rng)
            var s = state.copy(
                diceValue = dice,
                diceRolled = true,
                turnPhase = com.example.parchismania.engine.TurnPhase.MOVE,
                message = "${current.name} saca $dice",
            )

            val movable = GameEngine.getMovablePieces(s)
            s = s.copy(movablePieces = movable)
            _gameState.value = s

            if (movable.isEmpty()) {
                delay(aiDelayMs())
                val skipped = GameEngine.skipTurn(s)
                _gameState.value = skipped
                maybeRunAiTurn(skipped)
                return@launch
            }

            delay(aiDelayMs())
            val chosen = GameEngine.getAIMove(s)
            if (chosen != null) {
                val result = GameEngine.movePiece(s, chosen)
                _gameState.value = result.newState

                if (result.newState.gameOver) {
                    val humanWon = result.newState.winner?.let { winnerColor ->
                        result.newState.players.firstOrNull { it.color == winnerColor }?.isAI == false
                    } ?: false
                    _stats.value = _stats.value.copy(
                        gamesPlayed = _stats.value.gamesPlayed + 1,
                        gamesWon = _stats.value.gamesWon + if (humanWon) 1 else 0
                    )

                    val earned = result.newState.coinsEarned.values.sum()
                    store.update { ps -> ps.copy(coinsWallet = ps.coinsWallet + earned) }
                    return@launch
                }

                maybeRunAiTurn(result.newState)
            }
        }
    }
}
