package palbp.laboratory.demos.tictactoe.game.play.ui

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
import palbp.laboratory.demos.tictactoe.game.play.domain.*
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.otherTestPlayersInLobby
import palbp.laboratory.demos.tictactoe.testutils.SuspendingGate

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GameScreenViewModelTests {

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as DependenciesContainer
    }

    @Test
    fun initial_match_state_is_IDLE() {
        val sut = GameScreenViewModel(app.match)
        assertEquals(MatchState.IDLE, sut.state)
    }

    @Test
    fun startMatch_puts_created_match_in_onGoingGame_state_flow() = runTest {
        // Arrange
        val expectedGame = Game(localPlayerMarker = Marker.firstToMove.other, board = Board())
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any()) } returns flow { emit(GameStarted(expectedGame)) }
        }
        val sut = GameScreenViewModel(mockMatch)

        // Act
        val startedGate = SuspendingGate()
        var createdGame: Game? = null
        val collectJob = launch {
            sut.onGoingGame.collect {
                if (sut.state == MatchState.STARTED) {
                    createdGame = it
                    startedGate.open()
                }
            }
        }

        sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        startedGate.await()
        collectJob.cancel()

        // Assert
        assertNotNull(createdGame)
        assertEquals(expectedGame, createdGame)
        coVerify { mockMatch.start(any(), any()) }
    }

    @Test
    fun a_match_cannot_be_started_while_another_one_is_running() = runTest {
        // Arrange
        val sut = GameScreenViewModel(app.match)

        // Act
        val firstStart = sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        val secondStart = sut.startMatch(localTestPlayer, otherPlayerStartsTestChallenge)
        firstStart?.cancel()

        // Assert
        assertNotNull(firstStart)
        assertNull(secondStart)
    }

    @Test
    fun makeMove_on_started_match_succeeds() = runTest {
        // Arrange
        var game = Game(localPlayerMarker = Marker.firstToMove, board = Board())
        val mockMatch: Match = mockk(relaxed = true) {
            val at = slot<Coordinate>()
            coEvery { start(any(), any()) } returns flow { emit(GameStarted(game)) }
            coEvery { makeMove(capture(at)) } answers {
                game = game.makeMove(at.captured)
            }
        }

        val sut = GameScreenViewModel(mockMatch)
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Act
        val at = Coordinate(row = 0, column = 0)
        sut.makeMove(at)?.join()

        // Assert
        assertNotNull(game.board[at])
        coVerify(exactly = 1) { mockMatch.start(any(), any()) }
        coVerify(exactly = 1) { mockMatch.makeMove(any()) }
    }

    @Test
    fun finishing_the_match_sets_state_to_FINISHED() = runTest {
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any()) } returns flow {
                var game = Game(localPlayerMarker = Marker.firstToMove, board = Board())
                emit(GameStarted(game))
                game = game.makeMove(at = Coordinate(0, 0))
                emit(MoveMade(game))
                game = game.copy(board = game.board.makeMove(at = Coordinate(0, 1)))
                emit(MoveMade(game))
                game = game.copy(board = game.board.makeMove(at = Coordinate(1, 1)))
                emit(MoveMade(game))
                game = game.copy(board = game.board.makeMove(at = Coordinate(0, 2)))
                emit(MoveMade(game))
                game = game.copy(board = game.board.makeMove(at = Coordinate(2, 2)))
                emit(MoveMade(game))
            }
        }

        val sut = GameScreenViewModel(mockMatch)
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Assert
        assertEquals(MatchState.FINISHED, sut.state)
    }

    @Test
    fun forfeit_on_started_match_succeeds() = runTest {
        // Arrange
        val game = Game(localPlayerMarker = Marker.firstToMove, board = Board())
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any()) } returns flow { emit(GameStarted(game)) }
            coEvery { forfeit() } returns Unit
        }

        val sut = GameScreenViewModel(mockMatch)
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Act
        val job = sut.forfeit()
        job?.join()

        // Assert
        assertNotNull(job)
        coVerify(exactly = 1) { mockMatch.forfeit() }
    }

    @Test
    fun forfeiting_the_match_sets_state_to_FINISHED() = runTest {
        val mockMatch: Match = mockk(relaxed = true) {
            coEvery { start(any(), any()) } returns flow {
                val game = Game(localPlayerMarker = Marker.firstToMove, board = Board())
                emit(GameStarted(game))
                emit(GameEnded(game.copy(forfeitedBy = Marker.firstToMove), Marker.firstToMove.other))
            }
        }

        val sut = GameScreenViewModel(mockMatch)
        sut.startMatch(localTestPlayer, localPlayerStartsTestChallenge)?.join()

        // Assert
        assertEquals(MatchState.FINISHED, sut.state)
    }

    @Test
    fun forfeit_when_match_is_not_started_returns_null() = runTest {
        // Arrange
        val sut = GameScreenViewModel(app.match)

        // Act
        val job = sut.forfeit()

        // Assert
        assertNull(job)
    }
}

private val otherPlayerStartsTestChallenge = Challenge(
    challenger = otherTestPlayersInLobby.first(),
    challenged = localTestPlayer
)

private val localPlayerStartsTestChallenge = Challenge(
    challenger = otherTestPlayersInLobby.first(),
    challenged = localTestPlayer
)
