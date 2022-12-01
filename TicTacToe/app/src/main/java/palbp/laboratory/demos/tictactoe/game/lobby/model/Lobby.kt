package palbp.laboratory.demos.tictactoe.game.lobby.model

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction that characterizes the game's lobby.
 */
interface Lobby {
    suspend fun getPlayers(): List<PlayerInfo>
    suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo>
    fun enterAndObserve(localPlayer: PlayerInfo): Flow<List<PlayerInfo>>
    suspend fun leave()
}
