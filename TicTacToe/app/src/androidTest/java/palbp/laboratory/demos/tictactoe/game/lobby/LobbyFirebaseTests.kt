package palbp.laboratory.demos.tictactoe.game.lobby

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.testutils.SuspendingCountDownLatch
import palbp.laboratory.demos.tictactoe.testutils.SuspendingGate

@ExperimentalCoroutinesApi
class LobbyFirebaseTests {

    @get:Rule
    val populatedLobbyRule = PopulatedFirebaseLobby()

    @Test
    fun getPlayers_returns_players_in_the_lobby(): Unit = runTest {
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
    fun enter_places_player_in_the_lobby(): Unit = runTest {
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
    fun leave_removes_player_from_the_lobby(): Unit = runTest {
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

    @Test
    fun enterAndObserve_flow_reports_lobby_changes(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        val observations: MutableList<List<PlayerInfo>> = mutableListOf()
        val canDelete = SuspendingGate()
        val expectedObservations = SuspendingCountDownLatch(2)

        // Act
        val collectJob = launch {
            sut.enterAndObserve(localTestPlayer).collect {
                canDelete.open()
                expectedObservations.countDown()
                observations.add(it)
            }
        }

        canDelete.await()

        populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(otherTestPlayersInLobby.first().id.toString())
            .delete()
            .await()

        expectedObservations.await()
        sut.leave()
        collectJob.join()

        // Assert
        assertEquals(2, observations.size)
        assertEquals(otherTestPlayersInLobby.size + 1, observations.first().size)
        assertTrue(observations.first().contains(localTestPlayer))

        assertEquals(otherTestPlayersInLobby.size, observations[1].size)
        assertTrue(observations.first().contains(localTestPlayer))
    }
}
