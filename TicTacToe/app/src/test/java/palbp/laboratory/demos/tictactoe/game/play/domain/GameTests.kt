package palbp.laboratory.demos.tictactoe.game.play.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTests {

    @Test
    fun `making a move when on the local player's turn succeeds`() {
        val initial = Game(
            localPlayerMarker = Marker.firstToMove,
            board = Board(turn = Marker.firstToMove)
        )

        val moveCoordinate = Coordinate(row = 0, column = 0)
        val sut = initial.makeMove(at = moveCoordinate)

        assertEquals(Marker.firstToMove.other, sut.board.turn)
        assertEquals(Marker.firstToMove, sut.board[moveCoordinate])
    }

    @Test(expected = IllegalStateException::class)
    fun `making a move when not on the local player's turn throws`() {
        val sut = Game(
            localPlayerMarker = Marker.firstToMove.other,
            board = Board(turn = Marker.firstToMove)
        )

        sut.makeMove(at = Coordinate(row = 0, column = 0))
    }
}