package com.example.parchismania.engine

import kotlin.math.max
import kotlin.random.Random

enum class PlayerColor { RED, BLUE, GREEN, YELLOW }

data class Piece(
    val id: String,
    val color: PlayerColor,
    val position: Int = 0, // 1..68 on main ring, 0 when home/home-stretch/finished
    val isHome: Boolean = true,
    val isFinished: Boolean = false,
    val homeIndex: Int = 0 // 0 not in stretch, 1..7 in home stretch (7 means finished)
)

data class Player(
    val color: PlayerColor,
    val isAI: Boolean,
    val name: String,
    val pieces: List<Piece>,
    val finishedCount: Int = 0,
)

data class CaptureEvent(val capturedBy: PlayerColor, val capturedColor: PlayerColor, val position: Int)

data class GameState(
    val players: List<Player>,
    val currentPlayerIndex: Int = 0,
    val diceValue: Int? = null,
    val diceRolled: Boolean = false,
    val gameOver: Boolean = false,
    val winner: PlayerColor? = null,
    val consecutiveSixes: Int = 0,
    val message: String = "",
    val movablePieces: List<String> = emptyList(),
    val turnPhase: TurnPhase = TurnPhase.ROLL,
    val coinsEarned: Map<PlayerColor, Int> = PlayerColor.entries.associateWith { 0 },
    val lastCaptureEvent: CaptureEvent? = null,
)

enum class TurnPhase { ROLL, MOVE, ANIMATING, AI_THINKING }

object GameEngine {
    const val BOARD_SIZE = 68
    const val HOME_STRETCH_SIZE = 7

    val START_POSITIONS = mapOf(
        PlayerColor.YELLOW to 5,
        PlayerColor.BLUE to 22,
        PlayerColor.RED to 39,
        PlayerColor.GREEN to 56,
    )

    // Note: The original project includes "68"; we keep it for parity.
    val SAFE_POSITIONS = setOf(5, 12, 17, 22, 29, 34, 39, 46, 51, 56, 63, 68)

    val HOME_ENTRY = mapOf(
        PlayerColor.YELLOW to 68,
        PlayerColor.BLUE to 17,
        PlayerColor.RED to 34,
        PlayerColor.GREEN to 51,
    )

    val COLOR_ORDER = listOf(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.RED, PlayerColor.GREEN)

    object CoinRewards {
        const val capture = 15
        const val finishPiece = 10
        const val winGame = 50
        const val finishFast = 25
        const val exitHome = 2
    }

    fun createInitialState(configs: List<PlayerConfig>): GameState {
        val players = configs.map { cfg ->
            Player(color = cfg.color, isAI = cfg.isAI, name = cfg.name, pieces = createPieces(cfg.color))
        }
        val firstName = players.firstOrNull()?.name ?: "Jugador"
        return GameState(
            players = players,
            currentPlayerIndex = 0,
            message = "Turno de $firstName",
            coinsEarned = PlayerColor.entries.associateWith { 0 },
        )
    }

    fun rollDice(rng: Random = Random): Int = rng.nextInt(1, 7)

    fun getMovablePieces(state: GameState): List<String> {
        val dice = state.diceValue ?: return emptyList()
        val player = state.players[state.currentPlayerIndex]
        val movable = mutableListOf<String>()

        for (piece in player.pieces) {
            if (piece.isFinished) continue

            if (piece.isHome) {
                if (canMoveFromHome(dice)) {
                    val startPos = START_POSITIONS.getValue(player.color)
                    val blocked = isPositionBlockedBySameColor(player, startPos, excludePieceId = piece.id)
                    if (!blocked) movable += piece.id
                }
                continue
            }

            if (isInHomeStretch(piece)) {
                val newHomeIndex = piece.homeIndex + dice
                if (newHomeIndex <= HOME_STRETCH_SIZE) movable += piece.id
                continue
            }

            val relPos = getRelativePosition(player.color, piece.position)
            val distToHome = getDistanceToHome(relPos)

            if (distToHome == 0) {
                if (dice <= HOME_STRETCH_SIZE) movable += piece.id
                continue
            }

            if (dice <= distToHome) {
                val newRelPos = relPos + dice
                val newAbsPos = getAbsolutePosition(player.color, newRelPos)
                val blocked = isPositionBlockedByBarrier(state, newAbsPos)
                if (!blocked) movable += piece.id
            } else if (dice - distToHome <= HOME_STRETCH_SIZE) {
                movable += piece.id
            }
        }

        return movable
    }

    data class MoveResult(
        val captured: Boolean,
        val capturedPieceId: String? = null,
        val capturedColor: PlayerColor? = null,
        val finished: Boolean,
        val extraTurn: Boolean,
        val newState: GameState,
        val coinsGained: Int,
    )

    fun movePiece(state: GameState, pieceId: String): MoveResult {
        val dice = state.diceValue ?: return MoveResult(false, finished = false, extraTurn = false, newState = state, coinsGained = 0)

        val playerIndex = state.currentPlayerIndex
        val player = state.players[playerIndex]
        val piece = player.pieces.first { it.id == pieceId }

        var captured = false
        var capturedPieceId: String? = null
        var capturedColor: PlayerColor? = null
        var finished = false
        var extraTurn = dice == 6
        var coinsGained = 0

        var newPlayers = state.players.toMutableList()
        var newPlayer = player
        var newPiece = piece
        var message: String? = null
        var lastCapture: CaptureEvent? = null

        fun updatePlayerPieces(updatedPieces: List<Piece>) {
            newPlayer = newPlayer.copy(pieces = updatedPieces)
            newPlayers[playerIndex] = newPlayer
        }

        if (newPiece.isHome && dice == 5) {
            newPiece = newPiece.copy(isHome = false, position = START_POSITIONS.getValue(newPlayer.color), homeIndex = 0)
            coinsGained += CoinRewards.exitHome

            val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
            updatePlayerPieces(piecesUpdated)

            val cap = checkCapture(newPlayers, movedPiece = newPiece)
            if (cap != null) {
                captured = true
                capturedPieceId = cap.pieceId
                capturedColor = cap.color
                extraTurn = true
                coinsGained += CoinRewards.capture
                lastCapture = CaptureEvent(newPlayer.color, cap.color, newPiece.position)
                message = "${newPlayer.name} come ficha de ${cap.color.name.lowercase()}!"
                newPlayers = cap.updatedPlayers.toMutableList()
            }
        } else if (isInHomeStretch(newPiece)) {
            val newHomeIndex = newPiece.homeIndex + dice
            if (newHomeIndex == HOME_STRETCH_SIZE) {
                newPiece = newPiece.copy(isFinished = true, homeIndex = HOME_STRETCH_SIZE)
                finished = true
                extraTurn = true
                coinsGained += CoinRewards.finishPiece

                val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                updatePlayerPieces(piecesUpdated)
                newPlayer = newPlayer.copy(finishedCount = newPlayer.finishedCount + 1)
                newPlayers[playerIndex] = newPlayer

            } else if (newHomeIndex < HOME_STRETCH_SIZE) {
                newPiece = newPiece.copy(homeIndex = newHomeIndex)
                val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                updatePlayerPieces(piecesUpdated)
            }
        } else {
            val relPos = getRelativePosition(newPlayer.color, newPiece.position)
            val distToHome = getDistanceToHome(relPos)

            if (distToHome == 0) {
                newPiece = newPiece.copy(homeIndex = dice, position = 0)
                val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                updatePlayerPieces(piecesUpdated)

                if (dice == HOME_STRETCH_SIZE) {
                    newPiece = newPiece.copy(isFinished = true, homeIndex = HOME_STRETCH_SIZE)
                    finished = true
                    extraTurn = true
                    coinsGained += CoinRewards.finishPiece
                    val piecesUpdated2 = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                    updatePlayerPieces(piecesUpdated2)
                    newPlayer = newPlayer.copy(finishedCount = newPlayer.finishedCount + 1)
                    newPlayers[playerIndex] = newPlayer
                }
            } else if (dice > distToHome) {
                val homeSteps = dice - distToHome
                newPiece = newPiece.copy(homeIndex = homeSteps, position = 0)
                val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                updatePlayerPieces(piecesUpdated)

                if (homeSteps == HOME_STRETCH_SIZE) {
                    newPiece = newPiece.copy(isFinished = true, homeIndex = HOME_STRETCH_SIZE)
                    finished = true
                    extraTurn = true
                    coinsGained += CoinRewards.finishPiece
                    val piecesUpdated2 = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                    updatePlayerPieces(piecesUpdated2)
                    newPlayer = newPlayer.copy(finishedCount = newPlayer.finishedCount + 1)
                    newPlayers[playerIndex] = newPlayer
                }
            } else {
                val newRelPos = relPos + dice
                val newAbsPos = getAbsolutePosition(newPlayer.color, newRelPos)
                newPiece = newPiece.copy(position = newAbsPos)
                val piecesUpdated = newPlayer.pieces.map { if (it.id == newPiece.id) newPiece else it }
                updatePlayerPieces(piecesUpdated)

                val cap = checkCapture(newPlayers, movedPiece = newPiece)
                if (cap != null) {
                    captured = true
                    capturedPieceId = cap.pieceId
                    capturedColor = cap.color
                    extraTurn = true
                    coinsGained += CoinRewards.capture
                    lastCapture = CaptureEvent(newPlayer.color, cap.color, newAbsPos)
                    message = "${newPlayer.name} come ficha de ${cap.color.name.lowercase()}!"
                    newPlayers = cap.updatedPlayers.toMutableList()
                }
            }
        }

        // Win condition
        var gameOver = state.gameOver
        var winner = state.winner
        if (!gameOver) {
            val refreshedPlayer = newPlayers[playerIndex]
            if (refreshedPlayer.finishedCount == 4) {
                gameOver = true
                winner = refreshedPlayer.color
                message = "${refreshedPlayer.name} ha ganado!"
                coinsGained += CoinRewards.winGame
            }
        }

        // Three consecutive sixes rule (parity with TS implementation)
        var consecutiveSixes = state.consecutiveSixes
        if (consecutiveSixes >= 2 && dice == 6) {
            extraTurn = false
        }

        if (extraTurn && dice == 6) {
            consecutiveSixes += 1
            if (consecutiveSixes >= 3) {
                // Send one moved piece back home (approximation used in TS: first non-home, non-finished, non-stretch)
                val p = newPlayers[playerIndex]
                val candidate = p.pieces.firstOrNull { !it.isHome && !it.isFinished && !isInHomeStretch(it) }
                if (candidate != null) {
                    val updatedPieces = p.pieces.map {
                        if (it.id == candidate.id) it.copy(isHome = true, position = 0, homeIndex = 0) else it
                    }
                    newPlayers[playerIndex] = p.copy(pieces = updatedPieces)
                }
                extraTurn = false
                consecutiveSixes = 0
                message = "${newPlayers[playerIndex].name}: 3 seises seguidos. Ficha a casa."
            }
        }

        val coinsEarned = state.coinsEarned.toMutableMap().apply {
            this[newPlayers[playerIndex].color] = (this[newPlayers[playerIndex].color] ?: 0) + coinsGained
        }

        var nextPlayerIndex = state.currentPlayerIndex
        if (!extraTurn || gameOver) {
            if (!gameOver) {
                nextPlayerIndex = (playerIndex + 1) % newPlayers.size
                consecutiveSixes = 0
            }
        }

        val nextMessage = when {
            gameOver -> message ?: state.message
            captured -> message ?: state.message
            else -> {
                val np = newPlayers[nextPlayerIndex]
                "Turno de ${np.name}"
            }
        }

        val newState = GameState(
            players = newPlayers,
            currentPlayerIndex = nextPlayerIndex,
            diceValue = null,
            diceRolled = false,
            gameOver = gameOver,
            winner = winner,
            consecutiveSixes = consecutiveSixes,
            message = nextMessage,
            movablePieces = emptyList(),
            turnPhase = TurnPhase.ROLL,
            coinsEarned = coinsEarned,
            lastCaptureEvent = lastCapture,
        )

        return MoveResult(
            captured = captured,
            capturedPieceId = capturedPieceId,
            capturedColor = capturedColor,
            finished = finished,
            extraTurn = extraTurn,
            newState = newState,
            coinsGained = coinsGained,
        )
    }

    fun getAIMove(state: GameState): String? {
        val movable = state.movablePieces
        if (movable.isEmpty()) return null
        if (movable.size == 1) return movable.first()

        val player = state.players[state.currentPlayerIndex]
        val dice = state.diceValue ?: return movable.first()

        var bestPiece = movable.first()
        var bestScore = Double.NEGATIVE_INFINITY

        for (pieceId in movable) {
            val piece = player.pieces.first { it.id == pieceId }
            var score = 0.0

            if (piece.isHome && dice == 5) score += 50

            if (!piece.isHome && !piece.isFinished) {
                if (isInHomeStretch(piece)) {
                    val newHome = piece.homeIndex + dice
                    score += if (newHome == HOME_STRETCH_SIZE) 200.0 else 100.0 + newHome * 10.0
                } else {
                    val relPos = getRelativePosition(player.color, piece.position)
                    score += relPos

                    val distToHome = getDistanceToHome(relPos)
                    if (dice >= distToHome) score += 150

                    if (dice <= distToHome) {
                        val newRelPos = relPos + dice
                        val newAbsPos = getAbsolutePosition(player.color, newRelPos)

                        // Can capture?
                        for (otherPlayer in state.players) {
                            if (otherPlayer.color == player.color) continue
                            for (otherPiece in otherPlayer.pieces) {
                                if (otherPiece.isHome || otherPiece.isFinished || isInHomeStretch(otherPiece)) continue
                                if (otherPiece.position == newAbsPos && !isSafePosition(newAbsPos)) {
                                    score += 120
                                }
                            }
                        }

                        if (isSafePosition(newAbsPos)) score += 20
                    }

                    var inDanger = false
                    for (otherPlayer in state.players) {
                        if (otherPlayer.color == player.color) continue
                        for (otherPiece in otherPlayer.pieces) {
                            if (otherPiece.isHome || otherPiece.isFinished || isInHomeStretch(otherPiece)) continue
                            val otherRel = getRelativePosition(otherPlayer.color, otherPiece.position)
                            val pieceRel = getRelativePosition(otherPlayer.color, piece.position)
                            if (pieceRel > otherRel && pieceRel - otherRel <= 6) inDanger = true
                        }
                    }
                    if (inDanger) score += 30
                }
            }

            if (score > bestScore) {
                bestScore = score
                bestPiece = pieceId
            }
        }

        return bestPiece
    }

    fun skipTurn(state: GameState): GameState {
        val nextIndex = (state.currentPlayerIndex + 1) % state.players.size
        val nextPlayer = state.players[nextIndex]
        return state.copy(
            currentPlayerIndex = nextIndex,
            diceValue = null,
            diceRolled = false,
            consecutiveSixes = 0,
            movablePieces = emptyList(),
            turnPhase = TurnPhase.ROLL,
            lastCaptureEvent = null,
            message = "Turno de ${nextPlayer.name}",
        )
    }

    // ----- helpers -----

    data class PlayerConfig(val color: PlayerColor, val isAI: Boolean, val name: String)

    private fun createPieces(color: PlayerColor): List<Piece> {
        return List(4) { i ->
            Piece(id = "${color.name.lowercase()}-$i", color = color)
        }
    }

    private fun getAbsolutePosition(color: PlayerColor, relativePos: Int): Int {
        if (relativePos <= 0) return -1
        val start = START_POSITIONS.getValue(color)
        return ((start - 1 + relativePos - 1) % BOARD_SIZE) + 1
    }

    private fun getRelativePosition(color: PlayerColor, absolutePos: Int): Int {
        val start = START_POSITIONS.getValue(color)
        var rel = absolutePos - start + 1
        if (rel <= 0) rel += BOARD_SIZE
        return rel
    }

    private fun isInHomeStretch(piece: Piece): Boolean = piece.homeIndex > 0

    private fun canMoveFromHome(diceValue: Int): Boolean = diceValue == 5

    private fun getDistanceToHome(relativePos: Int): Int = BOARD_SIZE - relativePos

    private fun isPositionBlockedBySameColor(player: Player, position: Int, excludePieceId: String): Boolean {
        val count = player.pieces.count { p ->
            p.id != excludePieceId && !p.isHome && !p.isFinished && !isInHomeStretch(p) && p.position == position
        }
        return count >= 2
    }

    private fun isPositionBlockedByBarrier(state: GameState, position: Int): Boolean {
        for (pl in state.players) {
            val piecesAtPos = pl.pieces.filter { p ->
                !p.isHome && !p.isFinished && !isInHomeStretch(p) && p.position == position
            }
            if (piecesAtPos.size >= 2) return true
        }
        return false
    }

    private fun isSafePosition(position: Int): Boolean = SAFE_POSITIONS.contains(position)

    private data class CaptureResult(val pieceId: String, val color: PlayerColor, val updatedPlayers: List<Player>)

    private fun checkCapture(players: List<Player>, movedPiece: Piece): CaptureResult? {
        if (movedPiece.position == 0 || isInHomeStretch(movedPiece)) return null
        if (isSafePosition(movedPiece.position)) return null

        for ((pi, pl) in players.withIndex()) {
            if (pl.color == movedPiece.color) continue
            for (piece in pl.pieces) {
                if (piece.isHome || piece.isFinished || isInHomeStretch(piece)) continue
                if (piece.position == movedPiece.position) {
                    val sameColorAtPos = pl.pieces.filter { p ->
                        p.id != piece.id && !p.isHome && !p.isFinished && !isInHomeStretch(p) && p.position == piece.position
                    }
                    if (sameColorAtPos.isEmpty()) {
                        val sentHome = piece.copy(isHome = true, position = 0, homeIndex = 0)
                        val updatedPieces = pl.pieces.map { if (it.id == piece.id) sentHome else it }
                        val updatedPlayer = pl.copy(pieces = updatedPieces)
                        val updatedPlayers = players.toMutableList()
                        updatedPlayers[pi] = updatedPlayer
                        return CaptureResult(piece.id, pl.color, updatedPlayers)
                    }
                }
            }
        }
        return null
    }
}
