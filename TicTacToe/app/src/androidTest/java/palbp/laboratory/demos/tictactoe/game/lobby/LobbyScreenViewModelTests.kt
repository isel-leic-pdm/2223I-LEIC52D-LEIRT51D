package palbp.laboratory.demos.tictactoe.game.lobby

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.DependenciesContainer
import palbp.laboratory.demos.tictactoe.preferences.UserInfoRepository
import palbp.laboratory.demos.tictactoe.testutils.SuspendingCountDownLatch

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
            every { userInfo } answers { localTestPlayer.info }
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
        val latch = SuspendingCountDownLatch(2)
        // There are exactly TWO calls to the collector's block:
        // - the first bearing an empty list, which is the initial content of the StateFlow
        // - the second with the only list produced by the mock
        // Regardless, we will check if the local player ever appeared
        val collectJob = launch {
            sut.enterLobby()
            sut.players.collect {
                latch.countDown()
                allObservedPlayers.addAll(it)
            }
        }

        // Lets wait for the first (and only) collect
        latch.await()
        collectJob.cancel()

        // Assert
        assertFalse(allObservedPlayers.any { it.info == localTestPlayer.info })
        assertFalse(allObservedPlayers.isEmpty())
    }
}
