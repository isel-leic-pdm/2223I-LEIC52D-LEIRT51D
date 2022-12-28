package palbp.laboratory.demos.tictactoe.game.play.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import palbp.laboratory.demos.tictactoe.game.play.CleanupMatchesRule
import palbp.laboratory.demos.tictactoe.game.play.domain.*
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.testutils.SuspendingCountDownLatch
import palbp.laboratory.demos.tictactoe.testutils.SuspendingGate

@ExperimentalCoroutinesApi
class MatchFirebaseTests {

    @get:Rule
    val matchesRule = CleanupMatchesRule()

    @Test
    fun start_reports_new_game_on_returned_flow(): Unit = runTest {

        val sut = matchesRule.match
        val gameStateChangedGate = SuspendingGate()
        var gameEvent: GameEvent? = null

        // Act
        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameEvent = it
                gameStateChangedGate.open()
            }
        }

        gameStateChangedGate.await()
        collectJob.cancel()

        // Assert
        assertTrue(gameEvent is GameStarted)
        assertNotNull(gameEvent)
    }

    @Test
    fun start_publishes_match_when_local_player_is_challenged(): Unit = runTest {

        val sut = matchesRule.match
        val gameStateChangedGate = SuspendingGate()

        // Act
        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameStateChangedGate.open()
            }
        }

        gameStateChangedGate.await()
        collectJob.cancel()

        // Assert
        val challenger = matchesRule.remoteInitiatedChallenge.challenger
        val matchDoc = matchesRule.app.emulatedFirestoreDb
            .collection(ONGOING)
            .document(challenger.id.toString())
            .get()
            .await()

        assertNotNull(matchDoc.toMatchStateOrNull())
    }

    @Test
    fun makeMove_publishes_new_game_state_on_flow(): Unit = runTest {

        val sut = matchesRule.match
        val expectedGameStatesCount = 2
        val gameStartedGate = SuspendingGate()
        val expectedGameStatesGate = SuspendingCountDownLatch(expectedGameStatesCount)
        val gameStates = mutableListOf<Game>()

        // Act
        val challenge = matchesRule.locallyInitiatedChallenge
        val collectJob = launch {
            sut.start(localPlayer = localTestPlayer, challenge = challenge)
                .collect {
                    gameStates.add(it.game)
                    expectedGameStatesGate.countDown()
                    if (gameStates.size == 1)
                        gameStartedGate.open()
                }
        }

        // simulate remote game start
        matchesRule.app.emulatedFirestoreDb
            .collection(ONGOING)
            .document(challenge.challenger.id.toString())
            .set(Board().toDocumentContent())
            .await()

        gameStartedGate.await()

        val at = Coordinate(1, 1)
        sut.makeMove(at)

        expectedGameStatesGate.await()
        collectJob.cancel()

        // Assert
        assertEquals(expectedGameStatesCount, gameStates.size)
        assertNotNull(gameStates[1].board[at])
    }

    @Test
    fun moves_made_by_remote_player_are_published_on_flow(): Unit = runTest {

        val sut = matchesRule.match
        val expectedGameStatesCount = 2
        val gameStartedGate = SuspendingGate()
        val expectedGameStatesGate = SuspendingCountDownLatch(expectedGameStatesCount)
        val gameStates = mutableListOf<Game>()

        // Act
        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameStates.add(it.game)
                expectedGameStatesGate.countDown()
                if (gameStates.size == 1)
                    gameStartedGate.open()
            }
        }

        gameStartedGate.await()

        // Simulate move made by the remote player
        val challenger = matchesRule.remoteInitiatedChallenge.challenger
        val at = Coordinate(0, 0)
        matchesRule.app.emulatedFirestoreDb
            .collection(ONGOING)
            .document(challenger.id.toString())
            .set(Board().makeMove(at).toDocumentContent())
            .await()

        expectedGameStatesGate.await()
        collectJob.cancel()

        // Assert
        assertNotNull(gameStates[1].board[at])
    }

    @Test(expected = IllegalStateException::class)
    fun makeMove_when_not_the_local_player_turn_throws(): Unit = runTest {
        val sut = matchesRule.match
        val gameStateChangedGate = SuspendingGate()

        // Act
        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameStateChangedGate.open()
            }
        }

        gameStateChangedGate.await()
        collectJob.cancel()

        sut.makeMove(Coordinate(0, 0))
    }

    @Test
    fun forfeit_by_local_player_publishes_his_intent() = runTest {

        // Arrange
        val sut = matchesRule.match
        val gameStartedGate = SuspendingGate()
        var gameStarted: GameEvent? = null

        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameStarted = it
                gameStartedGate.open()
            }
        }

        gameStartedGate.await()
        collectJob.cancel()

        // Act
        matchesRule.match.forfeit()

        // Assert
        val challenger = matchesRule.remoteInitiatedChallenge.challenger
        val matchState = matchesRule.app.emulatedFirestoreDb
            .collection(ONGOING)
            .document(challenger.id.toString())
            .get()
            .await()
            .toMatchStateOrNull()

        assertNotNull(gameStarted)
        assertNotNull(matchState)
        assertEquals(gameStarted?.game?.localPlayerMarker, matchState?.second)
    }

    @Test
    fun forfeit_publishes_event_on_flow() = runTest {

        // Arrange
        val sut = matchesRule.match
        val gameStartedGate = SuspendingGate()
        val gameEndedGate = SuspendingGate()
        var forfeitEvent: GameEvent? = null

        val collectJob = launch {
            sut.start(
                localPlayer = localTestPlayer,
                challenge = matchesRule.remoteInitiatedChallenge
            ).collect {
                gameStartedGate.open()
                if (it is GameEnded) {
                    forfeitEvent = it
                    gameEndedGate.open()
                }
            }
        }

        gameStartedGate.await()

        // Act
        sut.forfeit()
        gameEndedGate.await()
        collectJob.cancel()

        // Assert
        assertNotNull(forfeitEvent)
    }
}
