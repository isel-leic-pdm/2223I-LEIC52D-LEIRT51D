package palbp.laboratory.demos.tictactoe.preferences

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.game.lobby.LobbyScreenTag
import palbp.laboratory.demos.tictactoe.testutils.PreserveDefaultDependencies
import palbp.laboratory.demos.tictactoe.testutils.assertIsNotReadOnly
import palbp.laboratory.demos.tictactoe.testutils.createPreserveDefaultDependenciesComposeRule
import palbp.laboratory.demos.tictactoe.ui.SaveButtonTag

@RunWith(AndroidJUnit4::class)
class PreferencesActivityEditModeTests {

    @get:Rule
    val testRule = createPreserveDefaultDependenciesComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultDependencies).testApplication
    }

    private val mockRepo: UserInfoRepository = mockk(relaxed = true) {
        every { userInfo } returns null
    }

    @Test
    fun screen_has_save_button_if_user_info_does_not_exist() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertExists()
        }
    }

    @Test
    fun screen_save_button_is_disabled_if_entered_info_is_not_valid() {
        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
        }
    }

    @Test
    fun screen_save_button_becomes_enabled_if_entered_info_is_valid() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(NicknameInputTag).performTextInput("nick")
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsEnabled()
        }
    }

    @Test
    fun screen_textFields_do_accept_user_input() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(NicknameInputTag).assertIsNotReadOnly()
            testRule.onNodeWithTag(MotoInputTag).assertIsNotReadOnly()
        }
    }

    @Test
    fun pressing_save_button_stores_info_and_navigates_to_lobby() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(NicknameInputTag).performTextInput("nick")
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            testRule.waitForIdle()

            // Assert
            verify { mockRepo.userInfo }
            testRule.onNodeWithTag(PreferencesScreenTag).assertDoesNotExist()
            testRule.onNodeWithTag(LobbyScreenTag).assertExists()
        }
    }

    @Test
    fun when_started_with_FINISH_ON_SAVE_pressing_save_button_stores_info_and_finishes_activity() {
        application.userInfoRepo = mockRepo
        val intent = Intent(application, PreferencesActivity::class.java).also {
            it.putExtra(FINISH_ON_SAVE_EXTRA, true)
        }

        ActivityScenario.launch<PreferencesActivity>(intent).use {
            testRule.onNodeWithTag(NicknameInputTag).performTextInput("nick")
            testRule.onNodeWithTag(SaveButtonTag).performClick()
            testRule.waitForIdle()

            // Assert
            verify { mockRepo.userInfo = UserInfo("nick") }
            testRule.onNodeWithTag(PreferencesScreenTag).assertDoesNotExist()
            assert(it.state == Lifecycle.State.DESTROYED)
        }
    }


    @Test
    fun screen_save_button_becomes_enabled_if_moto_is_edited_and_erased() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(NicknameInputTag).performTextInput("nick")
            testRule.onNodeWithTag(MotoInputTag).performTextInput(" ")
            testRule.onNodeWithTag(MotoInputTag).performTextClearance()
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsEnabled()
        }
    }

    @Test
    fun screen_save_button_becomes_disabled_if_nick_is_edited_and_erased() {

        application.userInfoRepo = mockRepo

        ActivityScenario.launch(PreferencesActivity::class.java).use {
            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()

            testRule.onNodeWithTag(NicknameInputTag).performTextInput("nick")
            testRule.onNodeWithTag(SaveButtonTag).assertIsEnabled()
            testRule.onNodeWithTag(NicknameInputTag).performTextClearance()
            testRule.waitForIdle()

            testRule.onNodeWithTag(SaveButtonTag).assertIsNotEnabled()
        }
    }
}