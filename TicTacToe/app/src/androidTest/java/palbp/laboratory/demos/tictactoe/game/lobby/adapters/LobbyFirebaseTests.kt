package palbp.laboratory.demos.tictactoe.game.lobby.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import palbp.laboratory.demos.tictactoe.game.lobby.*
import palbp.laboratory.demos.tictactoe.game.lobby.domain.ChallengeReceived
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo
import palbp.laboratory.demos.tictactoe.game.lobby.domain.RosterUpdated
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.otherTestPlayersInLobby
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfo
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
        println("expected.size = ${expectedInLobby.size}")
        println("playersInLobby.size = ${playersInLobby.size}")
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
    fun enterAndObserve_flow_reports_roster_changes(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        val expectedCount = otherTestPlayersInLobby.size + 1
        val observations: MutableList<List<PlayerInfo>> = mutableListOf()
        val canDelete = SuspendingGate()
        val hasDeleted = SuspendingGate()

        // Act
        val collectJob = launch {
            sut.enterAndObserve(localTestPlayer).collect { event ->
                if (event is RosterUpdated) {
                    when (event.players.size) {
                        expectedCount -> {
                            canDelete.open()
                            observations.add(event.players)
                        }
                        expectedCount-1 -> {
                            hasDeleted.open()
                            observations.add(event.players)
                        }
                    }
                }
            }
        }

        canDelete.await()

        populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(otherTestPlayersInLobby.first().id.toString())
            .delete()
            .await()

        hasDeleted.await()
        sut.leave()
        collectJob.join()

        // Assert
        assertEquals(2, observations.size)
        assertEquals(otherTestPlayersInLobby.size + 1, observations.first().size)
        assertTrue(observations.first().contains(localTestPlayer))

        assertEquals(otherTestPlayersInLobby.size, observations[1].size)
        assertTrue(observations.first().contains(localTestPlayer))
    }

    @Test
    fun enterAndObserve_flow_reports_received_challenges(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        val enteredLobby = SuspendingGate()
        val challengeReceived = SuspendingGate()
        var received: PlayerInfo? = null

        // Act
        val collectJob = launch {
            sut.enterAndObserve(localTestPlayer).collect { event ->
                if (event is RosterUpdated) {
                    if (event.players.any { it == localTestPlayer })
                        enteredLobby.open()
                }
                if (event is ChallengeReceived) {
                    received = event.challenge.challenger
                    challengeReceived.open()
                }
            }
        }

        enteredLobby.await()

        val challenger = PlayerInfo(UserInfo(nick = "challengerNick"))

        populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(localTestPlayer.id.toString())
            .update(CHALLENGER_FIELD, challenger.toDocumentContent())
            .await()

        challengeReceived.await()
        sut.leave()
        collectJob.join()

        // Assert
        assertEquals(challenger, received)
    }

    @Test
    fun issueChallenge_updates_challenged_data_with_challenger_info(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)

        // Act
        val challengedPLayer = otherTestPlayersInLobby.first()
        sut.issueChallenge(challengedPLayer)

        // Assert
        val challenge = populatedLobbyRule.app.emulatedFirestoreDb
            .collection(LOBBY)
            .document(challengedPLayer.id.toString())
            .get()
            .await()
            .toChallengeOrNull()

        assertNotNull(challenge)
        assertEquals(localTestPlayer, challenge?.challenger)
        assertEquals(challengedPLayer, challenge?.challenged)
    }

    @Test
    fun issueChallenge_returns_match_participants_info(): Unit = runTest {
        // Arrange
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)

        // Act
        val match = sut.issueChallenge(otherTestPlayersInLobby.first())

        // Assert
        assertEquals(localTestPlayer, match.challenger)
        assertEquals(otherTestPlayersInLobby.first(), match.challenged)
    }

    @Test(expected = IllegalStateException::class)
    fun issueChallenge_without_entering_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.issueChallenge(otherTestPlayersInLobby.first())
    }

    @Test(expected = IllegalStateException::class)
    fun leave_without_entering_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.leave()
    }

    @Test(expected = IllegalStateException::class)
    fun enter_without_leaving_lobby_throws(): Unit = runTest {
        val sut = populatedLobbyRule.lobby
        sut.enter(localTestPlayer)
        sut.enter(localTestPlayer)
    }
}
