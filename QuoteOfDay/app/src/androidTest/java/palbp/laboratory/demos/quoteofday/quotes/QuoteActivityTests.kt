package palbp.laboratory.demos.quoteofday.quotes

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.quotes.daily.QuoteActivity
import palbp.laboratory.demos.quoteofday.ui.NavigateBackTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToHistoryTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToInfoTestTag

@RunWith(AndroidJUnit4::class)
class QuoteActivityTests {

    @Test
    fun uses_application_context() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("palbp.laboratory.demos.quoteofday", appContext.packageName)
    }

    @get:Rule
    val testRule = createAndroidComposeRule<QuoteActivity>()

    @Test
    fun screen_contains_all_navigation_options_except_back() {

        // Assert
        testRule.onNodeWithTag(NavigateToHistoryTestTag).assertExists()
        testRule.onNodeWithTag(NavigateToInfoTestTag).assertExists()
        testRule.onNodeWithTag(NavigateBackTestTag).assertDoesNotExist()
    }

    @Test
    fun displayed_quote_survives_reconfiguration() {

        // Arrange
        testRule.onNodeWithTag("QuoteView").assertExists()

        // Act
        testRule.activityRule.scenario.recreate()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("QuoteView").assertExists()
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
    fun pressing_navigate_to_history_displays_quotesListScreen() {

        // Arrange
        testRule.onNodeWithTag("QuotesListScreen").assertDoesNotExist()

        // Act
        testRule.onNodeWithTag(NavigateToHistoryTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("QuotesListScreen").assertExists()
    }
}