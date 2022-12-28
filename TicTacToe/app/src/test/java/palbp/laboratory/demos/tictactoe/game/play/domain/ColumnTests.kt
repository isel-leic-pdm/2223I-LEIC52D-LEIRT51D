package palbp.laboratory.demos.tictactoe.game.play.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ColumnTests {

    @Test
    fun `isValidRow returns true when in the board bounds indexes`() {
        assertTrue(isValidRow(2))
    }

    @Test
    fun `isValidRow returns false for negative index`() {
        assertFalse(isValidRow(-4))
    }

    @Test
    fun `isValidColumn returns true when in the board bounds indexes`() {
        assertTrue(isValidColumn(0))
    }

    @Test
    fun `isValidColumn returns false for negative index`() {
        assertFalse(isValidRow(-1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create Coordinate with invalid row throws`() {
        Coordinate(row = -2, column = 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create Coordinate with invalid column throws`() {
        Coordinate(row = 2, column = -3)
    }

    @Test
    fun `create Coordinate with valid coordinates succeeds`() {
        Coordinate(row = 2, column = 1)
    }
}
