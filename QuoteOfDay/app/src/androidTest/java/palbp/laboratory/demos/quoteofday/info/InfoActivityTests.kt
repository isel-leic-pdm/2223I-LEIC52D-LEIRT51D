package palbp.laboratory.demos.quoteofday.info

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle.State
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.ui.NavigateBackTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToHistoryTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToInfoTestTag

@RunWith(AndroidJUnit4::class)
class InfoActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<InfoActivity>()

    @Test
    fun screen_only_contains_back_navigation_option() {

        // Assert
        testRule.onNodeWithTag(NavigateToHistoryTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToInfoTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        // Arrange
        testRule.onNodeWithTag("InfoScreen").assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("InfoScreen").assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == State.DESTROYED)
    }
}