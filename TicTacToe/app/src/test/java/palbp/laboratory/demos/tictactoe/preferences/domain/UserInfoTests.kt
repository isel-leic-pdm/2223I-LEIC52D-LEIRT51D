package palbp.laboratory.demos.tictactoe.preferences.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserInfoTests {
    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank nick throws`() {
        UserInfo(nick = "\n  \t ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create instance with blank moto throws`() {
        UserInfo(nick = "nick", moto = "\n  \t ")
    }

    @Test
    fun `create instance with non empty nick and moto succeeds`() {
        UserInfo(nick = "nick", moto = "moto")
    }

    @Test
    fun `create instance with non empty nick and no moto succeeds`() {
        UserInfo(nick = "nick")
    }

    @Test
    fun `validateUserInfoParts returns false when nick is blank`() {
        assertFalse(validateUserInfoParts("  \n", null))
    }

    @Test
    fun `validateUserInfoParts returns false when moto is blank`() {
        assertFalse(validateUserInfoParts("nick", " "))
    }

    @Test
    fun `validateUserInfoParts returns true when moto is null`() {
        assertTrue(validateUserInfoParts("nick", null))
    }
}