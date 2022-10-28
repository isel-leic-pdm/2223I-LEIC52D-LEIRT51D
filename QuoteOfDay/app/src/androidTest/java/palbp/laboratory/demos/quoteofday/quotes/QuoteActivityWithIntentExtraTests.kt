package palbp.laboratory.demos.quoteofday.quotes

import android.content.Intent
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.quotes.daily.QuoteActivity
import palbp.laboratory.demos.quoteofday.ui.NavigateBackTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToHistoryTestTag
import palbp.laboratory.demos.quoteofday.ui.NavigateToInfoTestTag
import palbp.laboratory.demos.quoteofday.testutils.createAndroidComposeRule

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class QuoteActivityWithIntentExtraTests {

    private val intent = Intent(
        InstrumentationRegistry.getInstrumentation().targetContext,
        QuoteActivity::class.java
    ).also {
        it.putExtra(
            QuoteActivity.QUOTE_EXTRA,
            LocalQuoteDto(text ="test content", "test author")
        )
    }

    @get:Rule
    val testRule = createAndroidComposeRule<QuoteActivity>(intent)

    @Test
    fun screen_contains_all_navigation_options_except_history() {
        // Assert
        testRule.onNodeWithTag(NavigateToHistoryTestTag).assertDoesNotExist()
        testRule.onNodeWithTag(NavigateToInfoTestTag).assertExists()
        testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    @Test
    fun screen_displays_quote_and_does_not_contain_fab() {
        // Assert
        testRule.onNodeWithTag("QuoteScreen").assertExists()
        testRule.onNodeWithTag("QuoteView").assertExists()
        testRule.onNodeWithTag("RefreshFab").assertDoesNotExist()
    }

    @Test
    fun pressing_navigate_back_finishes_activity() {

        // Arrange
        testRule.onNodeWithTag("QuoteScreen").assertExists()

        // Act
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        testRule.waitForIdle()

        // Assert
        testRule.onNodeWithTag("QuoteScreen").assertDoesNotExist()
        assert(testRule.activityRule.scenario.state == Lifecycle.State.DESTROYED)
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
}
