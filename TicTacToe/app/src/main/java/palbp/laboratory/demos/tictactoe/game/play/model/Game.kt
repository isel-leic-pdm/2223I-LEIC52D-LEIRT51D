package palbp.laboratory.demos.tictactoe.game.play.model

/**
 * Represents a Tic-Tac-Toe game. Instances are immutable.
 * @property localPlayer    The local player marker
 * @property board          The game board
 */
data class Game(val localPlayer: Marker, val board: Board)
