package palbp.laboratory.demos.tictactoe.game.lobby

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LobbyFirebaseTests {

    @get:Rule
    val populatedLobbyRule = PopulatedFirebaseLobby()

    @Test
    fun getPlayers_returns_players_in_the_lobby(): Unit = runBlocking {
        // Arrange
        val sut = populatedLobbyRule.lobby

        // Act
        val playersInLobby = sut.getPlayers()

        // Assert
        val expectedInLobby = otherTestPlayersInLobby
        assertTrue(playersInLobby.containsAll(expectedInLobby))
        assertTrue(expectedInLobby.size == playersInLobby.size)
    }

    @Test
    fun enter_places_player_in_the_lobby(): Unit = runBlocking {
        // Arrange
        val sut = populatedLobbyRule.lobby

        // Act
        sut.enter(localTestPlayer)

        // Assert
        val playersInLobby = sut.getPlayers()
        val expectedInLobby = otherTestPlayersInLobby + localTestPlayer
        assertTrue(playersInLobby.containsAll(expectedInLobby))
        assertTrue(expectedInLobby.size == playersInLobby.size)

        sut.leave()
    }

    @Test
    fun leave_removes_player_from_the_lobby(): Unit = runBlocking {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)
        val playersInLobbyAfterEnter = sut.getPlayers()

        // Act
        sut.leave()
        val playersInLobbyAfterLeave = sut.getPlayers()

        // Assert
        assertTrue(playersInLobbyAfterEnter.contains(localTestPlayer))
        assertFalse(playersInLobbyAfterLeave.contains(localTestPlayer))
    }
}
