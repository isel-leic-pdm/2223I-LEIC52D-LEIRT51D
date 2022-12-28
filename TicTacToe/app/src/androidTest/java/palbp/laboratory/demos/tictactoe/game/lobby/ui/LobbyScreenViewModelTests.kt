package palbp.laboratory.demos.tictactoe.game.lobby.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.DependenciesContainer
import palbp.laboratory.demos.tictactoe.game.lobby.domain.*
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.otherTestPlayersInLobby
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfoRepository
import palbp.laboratory.demos.tictactoe.testutils.SuspendingGate

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LobbyScreenViewModelTests {

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as DependenciesContainer
    }

    @Test
    fun entering_lobby_always_gets_userInfo_from_repo() = runTest {
        // Arrange
        val mockRepo: UserInfoRepository = mockk {
            every { userInfo } returns localTestPlayer.info
        }
        val sut = LobbyScreenViewModel(app.lobby, mockRepo)

        // Act
        sut.enterLobby()?.join()
        sut.leaveLobby()?.join()
        sut.enterLobby()?.join()
        sut.leaveLobby()?.join()

        // Assert
        verify(exactly = 2) { mockRepo.userInfo }
    }

    @Test
    fun once_entered_the_lobby_cannot_be_entered_until_left() = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(app.lobby, app.userInfoRepo)
        sut.enterLobby()?.join()

        // Act
        val secondTry = sut.enterLobby()
        sut.leaveLobby()?.join()
        val lastTry = sut.enterLobby()

        // Assert
        assertNull(secondTry)
        assertNotNull(lastTry)
    }

    @Test
    fun local_player_is_excluded_from_the_flow(): Unit = runTest {
        // Arrange
        val allObservedPlayers = mutableListOf<PlayerInfo>()
        val sut = LobbyScreenViewModel(app.lobby, app.userInfoRepo)

        // Act
        val gate = SuspendingGate()
        val collectJob = launch {
            sut.enterLobby()
            sut.players.collect { players ->
                if (players.isNotEmpty()) {
                    gate.open()
                    allObservedPlayers.addAll(players)
                }
            }
        }

        // Lets wait for the first (and only) collect
        gate.await()
        collectJob.cancel()

        // Assert
        assertFalse(allObservedPlayers.any { it.info == localTestPlayer.info })
        assertFalse(allObservedPlayers.isEmpty())
    }

    @Test
    fun received_challenge_produces_IncomingChallenge_event(): Unit = runTest {
        // Arrange
        val mockLobby: Lobby = mockk {
            val localPlayer = slot<PlayerInfo>()
            every { enterAndObserve(capture(localPlayer)) } returns flow {
                val challenge = Challenge(
                    challenger = otherTestPlayersInLobby.first(),
                    challenged = localPlayer.captured
                )
                emit(ChallengeReceived(challenge))
            }
        }
        val sut = LobbyScreenViewModel(mockLobby, app.userInfoRepo)

        val gate = SuspendingGate()
        var receivedChallenge: Challenge? = null
        val pendingMatchJob = launch {
            sut.enterLobby()
            sut.pendingMatch.collect { evt ->
                if (evt is IncomingChallenge) {
                    gate.open()
                    receivedChallenge = evt.challenge
                }
            }
        }

        gate.await()
        pendingMatchJob.cancel()

        // Assert
        assertNotNull(receivedChallenge)
        assertEquals(localTestPlayer.info, receivedChallenge?.challenged?.info)
        assertEquals(otherTestPlayersInLobby.first(), receivedChallenge?.challenger)
    }

    @Test
    fun sent_challenge_produces_SentChallenge_event(): Unit = runTest {
        // Arrange
        val sut = LobbyScreenViewModel(app.lobby, app.userInfoRepo)

        val gate = SuspendingGate()
        var challengeSent: Challenge? = null
        val pendingMatchJob = launch {
            sut.enterLobby()
            sut.sendChallenge(otherTestPlayersInLobby.first())?.join()
            sut.pendingMatch.collect { evt ->
                if (evt is SentChallenge) {
                    gate.open()
                    challengeSent = evt.challenge
                }
            }
        }

        gate.await()
        pendingMatchJob.cancel()

        // Assert
        assertNotNull(challengeSent)
        assertEquals(localTestPlayer.info, challengeSent?.challenger?.info)
        assertEquals(otherTestPlayersInLobby.first(), challengeSent?.challenged)
    }
}