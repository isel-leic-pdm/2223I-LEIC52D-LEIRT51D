package palbp.laboratory.demos.tictactoe.preferences.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.preferences.MotoInputTag
import palbp.laboratory.demos.tictactoe.preferences.NicknameInputTag
import palbp.laboratory.demos.tictactoe.preferences.PreferencesScreenTag
import palbp.laboratory.demos.tictactoe.testutils.assertIsReadOnly
import palbp.laboratory.demos.tictactoe.ui.EditButtonTag
import palbp.laboratory.demos.tictactoe.ui.NavigateBackTag
import palbp.laboratory.demos.tictactoe.ui.SaveButtonTag

@RunWith(AndroidJUnit4::class)
class PreferencesActivityDisplayModeTests {

    @get:Rule
    val testRule = createAndroidComposeRule<PreferencesActivity>()

    @Test
    fun preferences_screen_is_displayed() {
        testRule.onNodeWithTag(PreferencesScreenTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        testRule.onNodeWithTag(NavigateBackTag).assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(PreferencesScreenTag).assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun screen_has_edit_button_if_user_info_exists() {
        testRule.onNodeWithTag(EditButtonTag).assertExists()
    }

    @Test
    fun screen_textFields_do_not_accept_user_input() {
        testRule.onNodeWithTag(NicknameInputTag).assertIsReadOnly()
        testRule.onNodeWithTag(MotoInputTag).assertIsReadOnly()
    }

    @Test
    fun pressing_edit_button_places_screen_in_edit_mode() {

        // Act
        testRule.onNodeWithTag(EditButtonTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag(EditButtonTag).assertDoesNotExist()
        testRule.onNodeWithTag(SaveButtonTag).assertExists()
    }
}