package palbp.laboratory.demos.tictactoe.game.play.domain

import org.junit.Assert.*
import org.junit.Test

class BoardTests {

    @Test
    fun `newly created board has no moves`() {
        val sut = Board()
        assertTrue(sut.toMovesList().all { it == null })
    }

    @Test
    fun `turn on a newly created board is the first to move`() {
        val sut = Board()
        assertTrue(sut.turn == Marker.firstToMove)
    }

    @Test
    fun `making a move on an empty tile succeeds and changes turn`() {
        // Arrange
        val at = Coordinate(row = 1, column = 2)
        val sut = Board()
        val firstTurn = sut.turn

        // Act
        val boardAfterMove = sut.makeMove(at)

        // Assert
        val move = boardAfterMove.getMove(at)
        assertNotNull(move)
        assertTrue(move == firstTurn)
        assertNotEquals(firstTurn, boardAfterMove.turn)
    }

    @Test(expected = IllegalStateException::class)
    fun `making a move on a non empty tile throws`() {
        // Arrange
        val at = Coordinate(row = 0, column = 0)
        val sut = Board().makeMove(at)

        // Act
        sut.makeMove(at)
    }

    @Test
    fun `hasWon returns true when row has the same marker in all tiles`() {

        val sut = Board(
            turn = Marker.CROSS,
            tiles = listOf(
                listOf(null, Marker.CROSS, Marker.CROSS),
                buildList { Marker.CIRCLE },
                listOf(Marker.CROSS, null, null)
            )
        )

        assertTrue(sut.hasWon(Marker.CIRCLE))
    }

    @Test
    fun `hasWon returns true when column has the same marker in all tiles`() {

        val sut = Board(
            turn = Marker.CIRCLE,
            tiles = listOf(
                listOf(null, Marker.CROSS, Marker.CIRCLE),
                listOf(null, Marker.CROSS, Marker.CIRCLE),
                listOf(Marker.CIRCLE, Marker.CROSS, null)
            )
        )

        assertTrue(sut.hasWon(Marker.CROSS))
    }

    @Test
    fun `hasWon returns true when diagonal has the same marker in all tiles`() {

        val sut = Board(
            turn = Marker.CIRCLE,
            tiles = listOf(
                listOf(Marker.CROSS, null, Marker.CIRCLE),
                listOf(null, Marker.CROSS, Marker.CIRCLE),
                listOf(Marker.CIRCLE, null, Marker.CROSS)
            )
        )

        assertTrue(sut.hasWon(Marker.CROSS))
    }

    @Test
    fun `hasWon returns false when no winner is found`() {

        val sut = Board(
            turn = Marker.CIRCLE,
            tiles = listOf(
                listOf(null, null, Marker.CIRCLE),
                listOf(null, Marker.CROSS, null),
                listOf(null, null, null)
            )
        )

        assertFalse(sut.hasWon(Marker.CROSS))
        assertFalse(sut.hasWon(Marker.CIRCLE))
    }

    @Test
    fun `isTied returns true when no winner is found and board is complete`() {

        val sut = Board(
            turn = Marker.CROSS,
            tiles = listOf(
                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CIRCLE),
                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CROSS),
                listOf(Marker.CROSS, Marker.CIRCLE, Marker.CIRCLE)
            )
        )

        assertTrue(sut.isTied())
    }

    @Test
    fun `getResult returns HasWinner with winner marker when there is one winner`() {
        val sut = Board(
            turn = Marker.CIRCLE,
            tiles = listOf(
                listOf(Marker.CROSS, null, Marker.CIRCLE),
                listOf(null, Marker.CROSS, Marker.CIRCLE),
                listOf(Marker.CIRCLE, null, Marker.CROSS)
            )
        )

        val result = sut.getResult()
        assertTrue(result is HasWinner)
        assertTrue((result as HasWinner).winner == Marker.CROSS)
    }

    @Test
    fun `getResult returns Tied when the board is tied`() {
        val sut = Board(
            turn = Marker.CROSS,
            tiles = listOf(
                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CIRCLE),
                listOf(Marker.CIRCLE, Marker.CROSS, Marker.CROSS),
                listOf(Marker.CROSS, Marker.CIRCLE, Marker.CIRCLE)
            )
        )

        assertTrue(sut.getResult() is Tied)
    }

    @Test
    fun `getResult returns NotFinished when there is no winner and the board is not yet complete`() {
        val sut = Board(
            turn = Marker.CIRCLE,
            tiles = listOf(
                listOf(null, null, Marker.CIRCLE),
                listOf(null, Marker.CROSS, null),
                listOf(null, null, null)
            )
        )

        assertTrue(sut.getResult() is OnGoing)
    }
}