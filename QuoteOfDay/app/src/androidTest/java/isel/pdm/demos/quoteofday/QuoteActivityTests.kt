package isel.pdm.demos.quoteofday

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import isel.pdm.demos.quoteofday.daily.DailyQuoteActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class QuoteActivityTests {

    @Test
    fun uses_application_context() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("isel.pdm.demos.quoteofday", context.packageName)
        Assert.assertTrue(context.applicationContext is DependenciesContainer)
    }

    @get:Rule
    val quoteActivityRule = createAndroidComposeRule<DailyQuoteActivity>()

    @Test
    fun displayed_quote_survives_reconfiguration() {
        // Arrange
        quoteActivityRule.onNodeWithTag("QuoteView").assertDoesNotExist()
        quoteActivityRule.onNodeWithTag("LoadingButton").performClick()
        quoteActivityRule.onNodeWithTag("QuoteView").assertExists()

        // Act
        quoteActivityRule.activityRule.scenario.recreate()
        quoteActivityRule.waitForIdle()

        // Assert
        quoteActivityRule.onNodeWithTag("QuoteView").assertExists()
    }
}