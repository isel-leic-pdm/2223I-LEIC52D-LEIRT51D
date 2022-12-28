package palbp.laboratory.demos.tictactoe.game.play.domain

/**
 * Enumeration type used to represent the game's moves.
 */
enum class Marker {

    CIRCLE, CROSS;

    companion object {
        val firstToMove: Marker = CIRCLE
    }

    /**
     * The other player move
     */
    val other: Marker
        get() = if (this == CIRCLE) CROSS else CIRCLE
}

