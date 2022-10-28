package palbp.laboratory.demos.quoteofday.quotes

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity
import palbp.laboratory.demos.quoteofday.ui.NavigateBackTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToHistoryTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToInfoTestTag
import palbp.laboratory.demos.quoteofday.testutils.assertNotEmpty
import palbp.laboratory.demos.quoteofday.testutils.isExpanded

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class QuotesListActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<QuotesListActivity>()

    @Test
    fun screen_contains_all_navigation_options_except_history() {

        // Assert
        testRule.onNodeWithTag(NavigateToHistoryTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToInfoTestTag).assertExists()
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    @Test
    fun displayed_quotes_list_survives_reconfiguration() {

        // Arrange
        testRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()
    }

    @Test
    fun expanded_state_of_quotes_is_preserved_on_reconfiguration() {

        // Arrange
        testRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()
        testRule
            .onAllNodesWithTag("ExpandableQuoteView.ExpandAction")
            .onFirst()
            .performClick()
        testRule.waitForIdle()
        testRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)
    }

    @Test
    fun pressing_navigate_to_info_displays_infoScreen() {

        // Arrange
        testRule.onNodeWithTag("InfoScreen").assertDoesNotExist()

        // Act
        testRule.onNodeWithTag(NavigateToInfoTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("InfoScreen").assertExists()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        // Arrange
        testRule.onNodeWithTag("QuotesListScreen").assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("QuotesListScreen").assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }
}