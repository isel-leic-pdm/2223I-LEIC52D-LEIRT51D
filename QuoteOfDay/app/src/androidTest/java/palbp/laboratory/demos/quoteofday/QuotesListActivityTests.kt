package palbp.laboratory.demos.quoteofday

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity
import palbp.laboratory.demos.quoteofday.utils.assertEmpty
import palbp.laboratory.demos.quoteofday.utils.assertNotEmpty
import palbp.laboratory.demos.quoteofday.utils.isExpanded

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class QuotesListActivityTests {

    @get:Rule
    val quoteActivityRule = createAndroidComposeRule<QuotesListActivity>()

    @Test
    fun displayed_quotes_list_survives_reconfiguration() {

        // Arrange
        quoteActivityRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertEmpty()
        quoteActivityRule.onNodeWithTag("RefreshFab").performClick()
        quoteActivityRule.waitForIdle()
        quoteActivityRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()

        // Act
        quoteActivityRule.activityRule.scenario.recreate()
        quoteActivityRule.waitForIdle()

        // Assert
        quoteActivityRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()
    }

    @Test
    fun expanded_state_of_quotes_is_preserved_on_reconfiguration() {

        // Arrange
        quoteActivityRule.onNodeWithTag("RefreshFab").performClick()
        quoteActivityRule.waitForIdle()
        quoteActivityRule
            .onAllNodesWithTag("ExpandableQuoteView")
            .assertNotEmpty()
        quoteActivityRule
            .onAllNodesWithTag("ExpandableQuoteView.ExpandAction")
            .onFirst()
            .performClick()
        quoteActivityRule.waitForIdle()
        quoteActivityRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)

        // Act
        quoteActivityRule.activityRule.scenario.recreate()
        quoteActivityRule.waitForIdle()

        // Assert
        quoteActivityRule
            .onAllNodes(matcher = isExpanded())
            .assertCountEquals(expectedSize = 1)
    }
}