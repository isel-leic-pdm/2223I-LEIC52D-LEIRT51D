package palbp.laboratory.demos.tictactoe.game.play.domain

import kotlinx.coroutines.flow.Flow
import palbp.laboratory.demos.tictactoe.game.lobby.domain.Challenge
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo

/**
 * Sum type used to describe events occurring while the match is ongoing.
 *
 * [GameStarted] to signal that the game has started.
 * [MoveMade] to signal that the a move was made.
 * [GameEnded] to signal the game termination.
 */
sealed class GameEvent(val game: Game)
class GameStarted(game: Game) : GameEvent(game)
class MoveMade(game: Game) : GameEvent(game)
class GameEnded(game: Game, val winner: Marker? = null) : GameEvent(game)

/**
 * Abstraction that characterizes a match between two players, that is, the
 * required interactions.
 */
interface Match {

    /**
     * Starts the match. The first to make a move is the challenger. The game
     * is only actually in progress after its initial state is published on the flow.
     * @param [localPlayer] the local player information
     * @param [challenge] the challenge bearing the players' information
     * @return the flow of game state change events, expressed as [GameEvent] instances
     * @throws IllegalStateException if a game is in progress
     */
    fun start(localPlayer: PlayerInfo, challenge: Challenge): Flow<GameEvent>

    /**
     * Makes a move at the given coordinates.
     * @throws IllegalStateException if a game is not in progress or the move is illegal,
     * either because it's not the local player turn or the position is not free.
     */
    suspend fun makeMove(at: Coordinate)

    /**
     * Forfeits the current game.
     * @throws IllegalStateException if a game is not in progress
     */
    suspend fun forfeit()

    /**
     * Ends the match, cleaning up if necessary.
     */
    suspend fun end()
}

