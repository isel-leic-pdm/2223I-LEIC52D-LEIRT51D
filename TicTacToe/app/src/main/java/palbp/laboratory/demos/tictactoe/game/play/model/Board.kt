package palbp.laboratory.demos.tictactoe.game.play.model

/**
 * Represents a Tic-Tac-Toe board. Instances are immutable.
 * @property turn   The next player to move
 * @property tiles  The board tiles
 */
data class Board(
    val turn: Marker = Marker.firstToMove,
    val tiles: List<List<Marker?>> =
        List(
            size = BOARD_SIDE,
            init = { List(size = BOARD_SIDE, init = { null }) }
        )
) {
    /**
     * Overloads the indexing operator
     */
    operator fun get(at: Coordinate): Marker? = getMove(at)

    /**
     * Gets the move at the given coordinates.
     * @param at    the move's coordinates
     * @return the [Marker] instance that made the move, or null if the position is empty
     */
    fun getMove(at: Coordinate): Marker? = tiles[at.row][at.column]

    /**
     * Makes a move at the given coordinates and returns the new board instance.
     * @param at    the board's coordinate
     * @throws IllegalArgumentException if the position is already occupied
     * @return the new board instance
     */
    fun makeMove(at: Coordinate): Board {
        check(this[at] == null)
        return Board(
            turn = turn.other,
            tiles = tiles.mapIndexed { row, elem ->
                if (row == at.row)
                    List(tiles[row].size) { col ->
                        if (col == at.column) turn
                        else tiles[row][col]
                    }
                else
                    elem

            }
        )
    }

    /**
     * Converts this instance to a list of moves.
     */
    fun toMovesList(): List<Marker?> = tiles.flatten()
}

/**
 * Extension function that checks whether this board represents a tied game or not
 * @return true if the board is a tied game, false otherwise
 */
fun Board.isTied(): Boolean =
    toMovesList().all { it != null } && !hasWon(Marker.CIRCLE) && !hasWon(Marker.CROSS)

/**
 * Extension function that checks whether the given marker has won the game
 * @return true if the player with the given marker has won, false otherwise
 */
fun Board.hasWon(marker: Marker): Boolean =
    tiles.any { row -> row.all { it == marker } } ||
    (0 until BOARD_SIDE).any { column ->
        (0 until BOARD_SIDE).all { row -> tiles[row][column] == marker }
    } ||
    tiles[0][0] == marker && tiles[1][1] == marker && tiles[2][2] == marker ||
    tiles[0][2] == marker && tiles[1][1] == marker && tiles[2][0] == marker

