package com.example.parchismania.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "refork_prefs")

enum class BoardSkin { CLASSIC, MIDNIGHT, CANDY }

data class PersistedSettings(
    val aiSpeed: String = "NORMAL",
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val animationsEnabled: Boolean = true,
    val boardSkin: BoardSkin = BoardSkin.CLASSIC,
    val coinsWallet: Int = 0,
    val ownedSkins: Set<BoardSkin> = setOf(BoardSkin.CLASSIC),
    val lastPlayersCount: Int = 2,
    val lastHumanCount: Int = 1,
    val lastColorsCsv: String = "YELLOW,RED",
)

class AppDataStore(private val context: Context) {

    private object Keys {
        val aiSpeed = stringPreferencesKey("ai_speed")
        val soundEnabled = booleanPreferencesKey("sound_enabled")
        val vibrationEnabled = booleanPreferencesKey("vibration_enabled")
        val animationsEnabled = booleanPreferencesKey("animations_enabled")
        val boardSkin = stringPreferencesKey("board_skin")
        val coinsWallet = intPreferencesKey("coins_wallet")
        val ownedSkins = stringPreferencesKey("owned_skins")
        val lastPlayersCount = intPreferencesKey("last_players_count")
        val lastHumanCount = intPreferencesKey("last_human_count")
        val lastColorsCsv = stringPreferencesKey("last_colors_csv")
    }

    val flow: Flow<PersistedSettings> = context.dataStore.data.map { p ->
        val skin = p[Keys.boardSkin]?.let { runCatching { BoardSkin.valueOf(it) }.getOrNull() } ?: BoardSkin.CLASSIC
        val owned = p[Keys.ownedSkins]
            ?.split(',')
            ?.mapNotNull { runCatching { BoardSkin.valueOf(it) }.getOrNull() }
            ?.toSet()
            ?: setOf(BoardSkin.CLASSIC)

        PersistedSettings(
            aiSpeed = p[Keys.aiSpeed] ?: "NORMAL",
            soundEnabled = p[Keys.soundEnabled] ?: true,
            vibrationEnabled = p[Keys.vibrationEnabled] ?: true,
            animationsEnabled = p[Keys.animationsEnabled] ?: true,
            boardSkin = skin,
            coinsWallet = p[Keys.coinsWallet] ?: 0,
            ownedSkins = if (owned.isEmpty()) setOf(BoardSkin.CLASSIC) else owned,
            lastPlayersCount = p[Keys.lastPlayersCount] ?: 2,
            lastHumanCount = p[Keys.lastHumanCount] ?: 1,
            lastColorsCsv = p[Keys.lastColorsCsv] ?: "YELLOW,RED",
        )
    }

    suspend fun update(block: (PersistedSettings) -> PersistedSettings) {
        context.dataStore.edit { prefs ->
            val current = PersistedSettings(
                aiSpeed = prefs[Keys.aiSpeed] ?: "NORMAL",
                soundEnabled = prefs[Keys.soundEnabled] ?: true,
                vibrationEnabled = prefs[Keys.vibrationEnabled] ?: true,
                animationsEnabled = prefs[Keys.animationsEnabled] ?: true,
                boardSkin = prefs[Keys.boardSkin]?.let { runCatching { BoardSkin.valueOf(it) }.getOrNull() } ?: BoardSkin.CLASSIC,
                coinsWallet = prefs[Keys.coinsWallet] ?: 0,
                ownedSkins = prefs[Keys.ownedSkins]
                    ?.split(',')
                    ?.mapNotNull { runCatching { BoardSkin.valueOf(it) }.getOrNull() }
                    ?.toSet()
                    ?: setOf(BoardSkin.CLASSIC),
                lastPlayersCount = prefs[Keys.lastPlayersCount] ?: 2,
                lastHumanCount = prefs[Keys.lastHumanCount] ?: 1,
                lastColorsCsv = prefs[Keys.lastColorsCsv] ?: "YELLOW,RED",
            )

            val next = block(current)

            prefs[Keys.aiSpeed] = next.aiSpeed
            prefs[Keys.soundEnabled] = next.soundEnabled
            prefs[Keys.vibrationEnabled] = next.vibrationEnabled
            prefs[Keys.animationsEnabled] = next.animationsEnabled
            prefs[Keys.boardSkin] = next.boardSkin.name
            prefs[Keys.coinsWallet] = next.coinsWallet
            prefs[Keys.ownedSkins] = next.ownedSkins.joinToString(",") { it.name }
            prefs[Keys.lastPlayersCount] = next.lastPlayersCount
            prefs[Keys.lastHumanCount] = next.lastHumanCount
            prefs[Keys.lastColorsCsv] = next.lastColorsCsv
        }
    }
}
