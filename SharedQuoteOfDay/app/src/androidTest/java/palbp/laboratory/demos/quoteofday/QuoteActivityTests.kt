package palbp.laboratory.demos.quoteofday

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

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class QuoteActivityTests {

    @Test
    fun uses_application_context() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("palbp.laboratory.demos.quoteofday", appContext.packageName)
    }

    @get:Rule
    val quoteActivityRule = createAndroidComposeRule<QuoteActivity>()

    @Test
    fun displayed_quote_survives_reconfiguration() {

        // Arrange
        quoteActivityRule.onNodeWithTag("QuoteView").assertDoesNotExist()
        quoteActivityRule.onNodeWithTag("RefreshFab").performClick()
        quoteActivityRule.waitForIdle()
        quoteActivityRule.onNodeWithTag("QuoteView").assertExists()

        // Act
        quoteActivityRule.activityRule.scenario.recreate()
        quoteActivityRule.waitForIdle()

        // Assert
        quoteActivityRule.onNodeWithTag("QuoteView").assertExists()
    }
}