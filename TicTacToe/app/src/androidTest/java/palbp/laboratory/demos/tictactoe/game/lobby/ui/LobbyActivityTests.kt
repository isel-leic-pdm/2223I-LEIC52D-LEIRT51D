package palbp.laboratory.demos.tictactoe.game.lobby.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.game.play.ui.GameScreenTag
import palbp.laboratory.demos.tictactoe.preferences.PreferencesScreenTag
import palbp.laboratory.demos.tictactoe.ui.NavigateBackTag
import palbp.laboratory.demos.tictactoe.ui.NavigateToPreferencesTag

@RunWith(AndroidJUnit4::class)
class LobbyActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<LobbyActivity>()

    @Test
    fun lobby_screen_is_displayed() {
        testRule.onNodeWithTag(LobbyScreenTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        testRule.onNodeWithTag(NavigateBackTag).assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(LobbyScreenTag).assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun pressing_navigate_to_settings_displays_preferences_activity() {

        // Act
        testRule.onNodeWithTag(PreferencesScreenTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToPreferencesTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(PreferencesScreenTag).assertExists()
    }

    @Test
    fun pressing_a_player_card_displays_game_activity() {
        // Act
        testRule.onAllNodesWithTag(PlayerInfoViewTag).onFirst().performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(GameScreenTag).assertExists()
    }
}
