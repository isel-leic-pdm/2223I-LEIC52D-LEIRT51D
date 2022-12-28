package palbp.laboratory.demos.tictactoe.game.play.ui

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.R
import palbp.laboratory.demos.tictactoe.game.lobby.domain.Challenge
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo
import palbp.laboratory.demos.tictactoe.game.lobby.domain.firstToMove
import palbp.laboratory.demos.tictactoe.game.play.domain.*
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.otherTestPlayersInLobby
import palbp.laboratory.demos.tictactoe.testutils.PreserveDefaultDependencies
import palbp.laboratory.demos.tictactoe.testutils.createPreserveDefaultDependenciesComposeRule

private const val STARTUP_DELAY = 2000L

@RunWith(AndroidJUnit4::class)
class GameActivityTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val delayedMockMatch: Match = createMockMatch(STARTUP_DELAY)
    private val immediateMockMatch: Match = createMockMatch()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private fun createMockMatch(delayMs: Long? = null): Match = mockk(relaxed = true) {
        val localPlayer = slot<PlayerInfo>()
        val challenge = slot<Challenge>()
        coEvery { start(capture(localPlayer), capture(challenge)) } answers {
            flow {
                if (delayMs != null)
                    delay(delayMs)
                val localMarker = getLocalPlayerMarker(localPlayer.captured, challenge.captured)
                emit(GameStarted(Game(localPlayerMarker = localMarker, board = Board())))
            }
        }
        coEvery { makeMove(any()) } returns Unit
        coEvery { forfeit() } returns Unit
    }

    private fun createMatchIntent(localPLayerStarts: Boolean): Pair<Intent, Challenge> {
        val challenge =
            if (localPLayerStarts)
                Challenge(
                    challenger = localTestPlayer,
                    challenged = otherTestPlayersInLobby.first()
                )
            else
                Challenge(
                    challenger = otherTestPlayersInLobby.first(),
                    challenged = localTestPlayer
                )
        val intent = Intent(application, GameActivity::class.java).also {
            it.putExtra(GameActivity.MATCH_INFO_EXTRA, MatchInfo(localTestPlayer, challenge))
        }
        return Pair(intent, challenge)
    }

    @Test
    fun game_activity_starts_by_displaying_starting_dialog() {

        // Arrange
        application.match = delayedMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            testRule.onNodeWithTag(StartingMatchDialogTag).assertExists()
            testRule
                .onNodeWithTag(GameScreenTitleTag)
                .assertTextEquals(application.getString(R.string.game_screen_waiting))
        }
    }

    @Test
    fun when_game_starts_the_player_turn_is_displayed() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            val title =
                if (localTestPlayer == challenge.firstToMove) R.string.game_screen_your_turn
                else R.string.game_screen_opponent_turn

            testRule.onNodeWithTag(StartingMatchDialogTag).assertDoesNotExist()
            testRule
                .onNodeWithTag(GameScreenTitleTag)
                .assertTextEquals(application.getString(title))
        }
    }

    @Test
    fun when_its_local_player_turn_board_is_enabled() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            assertEquals(challenge.firstToMove, localTestPlayer)
            testRule
                .onAllNodesWithTag(TileViewTag)
                .assertAll(isEnabled())
        }
    }

    @Test
    fun when_its_local_player_turn_board_clicks_make_moves() {

        // Arrange
        application.match = immediateMockMatch
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onAllNodesWithTag(TileViewTag)
                .onFirst()
                .performClick()

            testRule.waitForIdle()

            // Assert
            assertEquals(challenge.firstToMove, localTestPlayer)
            coVerify(exactly = 1) { immediateMockMatch.makeMove(any()) }
        }
    }

    @Test
    fun when_its_remote_player_turn_board_is_disabled() {

        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            // Assert
            assertNotEquals(challenge.firstToMove, localTestPlayer)
            testRule
                .onAllNodesWithTag(TileViewTag)
                .assertAll(isNotEnabled())
        }
    }

    @Test
    fun when_its_remote_player_turn_board_clicks_do_not_make_moves() {

        // Arrange
        application.match = immediateMockMatch
        val (intent, challenge) = createMatchIntent(localPLayerStarts = false)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onAllNodesWithTag(TileViewTag)
                .onFirst()
                .performClick()

            testRule.waitForIdle()

            // Assert
            assertNotEquals(challenge.firstToMove, localTestPlayer)
            coVerify(exactly = 0) { immediateMockMatch.makeMove(any()) }
        }
    }

    @Test
    fun when_game_ends_match_end_dialog_is_shown() {
        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)
        val localMarker = getLocalPlayerMarker(localTestPlayer, challenge)
        application.match = mockk {
            var result: BoardResult? = null
            coEvery { start(any(), any()) } answers {
                flow {
                    val game = Game(localPlayerMarker = localMarker, board = Board())
                    emit(GameStarted(game))
                    while (result != null)
                        delay(1000)
                    emit(GameEnded(game = game, winner = localMarker.other))
                }
            }
            coEvery { makeMove(any()) } returns Unit
            coEvery { forfeit() } answers { result = HasWinner(winner = localMarker.other) }
            coEvery { end() } returns Unit
        }

        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onNodeWithTag(ForfeitButtonTag)
                .performClick()

            testRule.waitForIdle()

            // Assert
            testRule.onNodeWithTag(MatchEndedDialogTag).assertExists()
        }
    }

    @Test
    fun when_forfeit_button_is_pressed_forfeit_is_called() {
        // Arrange
        application.match = immediateMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onNodeWithTag(ForfeitButtonTag)
                .performClick()

            testRule.waitForIdle()

            // Assert
            coVerify(exactly = 1) { immediateMockMatch.forfeit() }
        }
    }

    @Test
    fun back_navigation_when_match_is_ongoing_forfeits_it() {
        // Arrange
        application.match = immediateMockMatch
        val (intent, _) = createMatchIntent(localPLayerStarts = true)

        // Act
        ActivityScenario.launch<GameActivity>(intent).use {

            it.onActivity { activity -> activity.onBackPressedDispatcher.onBackPressed() }
            testRule.waitForIdle()

            // Assert
            coVerify(exactly = 1) { immediateMockMatch.forfeit() }
        }
    }

    @Test
    fun dismissing_match_end_dialog_finishes_activity() {
        // Arrange
        val (intent, challenge) = createMatchIntent(localPLayerStarts = true)
        val localMarker = getLocalPlayerMarker(localTestPlayer, challenge)
        application.match = mockk {
            var result: BoardResult? = null
            coEvery { start(any(), any()) } answers {
                flow {
                    val game = Game(localPlayerMarker = localMarker, board = Board())
                    emit(GameStarted(game))
                    while (result != null)
                        delay(1000)
                    emit(GameEnded(game = game, winner = localMarker.other))
                }
            }
            coEvery { makeMove(any()) } returns Unit
            coEvery { forfeit() } answers { result = HasWinner(winner = localMarker.other) }
            coEvery { end() } returns Unit
        }

        ActivityScenario.launch<GameActivity>(intent).use {

            testRule
                .onNodeWithTag(ForfeitButtonTag)
                .performClick()

            testRule.waitForIdle()
            testRule.onNodeWithTag(MatchEndedDialogDismissButtonTag).performClick()

            // Assert
            testRule.waitUntil(2000) { it.state == Lifecycle.State.DESTROYED }
        }
    }
}