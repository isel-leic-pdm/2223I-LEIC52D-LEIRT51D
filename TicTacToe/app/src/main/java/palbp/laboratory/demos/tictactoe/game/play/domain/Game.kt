package palbp.laboratory.demos.tictactoe.game.play.domain

import palbp.laboratory.demos.tictactoe.game.lobby.domain.Challenge
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo
import palbp.laboratory.demos.tictactoe.game.lobby.domain.firstToMove

/**
 * Represents a Tic-Tac-Toe game. Instances are immutable.
 * @property localPlayerMarker  The local player marker
 * @property forfeitedBy        The marker of the player who forfeited the game, if that was the case
 * @property board              The game board
 */
data class Game(
    val localPlayerMarker: Marker = Marker.firstToMove,
    val forfeitedBy: Marker? = null,
    val board: Board = Board()
)

/**
 * Makes a move on this [Game], returning a new instance.
 * @param at the coordinates where the move is to be made
 * @return the new [Game] instance
 * @throws IllegalStateException if its an invalid move, either because its
 * not the local player's turn or the move cannot be made on that location
 */
fun Game.makeMove(at: Coordinate): Game {
    check(localPlayerMarker == board.turn)
    return copy(board = board.makeMove(at))
}

/**
 * Gets which marker is to be assigned to the local player for the given challenge.
 */
fun getLocalPlayerMarker(localPlayer: PlayerInfo, challenge: Challenge) =
    if (localPlayer == challenge.firstToMove) Marker.firstToMove
    else Marker.firstToMove.other

/**
 * Gets the game current result
 */
fun Game.getResult() =
    if (forfeitedBy != null) HasWinner(forfeitedBy.other)
    else board.getResult()