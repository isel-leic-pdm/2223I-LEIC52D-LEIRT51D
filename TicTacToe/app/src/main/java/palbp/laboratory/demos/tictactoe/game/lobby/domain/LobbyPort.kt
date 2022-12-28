package palbp.laboratory.demos.tictactoe.game.lobby.domain

import kotlinx.coroutines.flow.Flow

/**
 * Sum type used to describe events occurring while the player is in the lobby.
 *
 * [RosterUpdated] to describe changes in the set of players in the lobby
 * [ChallengeReceived] when a challenge is received by the local player.
 */
sealed class LobbyEvent
data class RosterUpdated(val players: List<PlayerInfo>) : LobbyEvent()
data class ChallengeReceived(val challenge: Challenge) : LobbyEvent()

/**
 * Abstraction that characterizes the game's lobby.
 */
interface Lobby {
    /**
     * Gets the list of players currently in the lobby
     * @return the list of players currently in the lobby
     */
    suspend fun getPlayers(): List<PlayerInfo>

    /**
     * Enters the lobby. It cannot be entered again until left.
     * @return the list of players currently in the lobby
     * @throws IllegalStateException    if the lobby was already entered
     */
    suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo>

    /**
     * Enters the lobby and subscribes to events that occur in the lobby.
     * It cannot be entered again until left.
     * @return the flow of lobby events
     * @throws IllegalStateException    if the lobby was already entered
     */
    fun enterAndObserve(localPlayer: PlayerInfo): Flow<LobbyEvent>

    /**
     * Issues a challenge to the given player. In order to issue a challenge,
     * the player must have previously entered the lobby. The player that issues
     * the challenge exits the lobby.
     * @param   [to]  the player to which the challenge will be issued
     * @return  the challenge information
     * @throws IllegalStateException    if the lobby was not yet entered
     */
    suspend fun issueChallenge(to: PlayerInfo): Challenge

    /**
     * Leaves the lobby. If the lobby was entered using [enterAndObserve],
     * the returned flow is closed, thereby ending the flow of lobby events.
     * @throws IllegalStateException    if the lobby was not yet entered
     */
    suspend fun leave()
}

