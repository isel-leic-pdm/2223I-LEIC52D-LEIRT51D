package palbp.laboratory.demos.quoteofday.quotes

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.quoteofday.R
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity
import palbp.laboratory.demos.quoteofday.testutils.PreserveDefaultFakeServiceRule
import palbp.laboratory.demos.quoteofday.testutils.createPreserveDefaultFakeServiceComposeRule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuoteListActivityErrorTests {

    @get:Rule
    val testRule = createPreserveDefaultFakeServiceComposeRule()

    private val application by lazy {
        (testRule.activityRule as PreserveDefaultFakeServiceRule).testApplication
    }

    @Test
    fun error_displayed_when_fetchWeekQuotes_throws_UnexpectedResponseException() {

        application.quoteService = mockk {
            coEvery { fetchQuote() } returns
                    Quote(text = "Test text", author = "Test author")
            coEvery { fetchWeekQuotes() } throws UnexpectedResponseException()
        }

        ActivityScenario.launch(QuotesListActivity::class.java).use {
            testRule.onNodeWithTag("QuoteView").assertDoesNotExist()
            testRule.onNodeWithTag("ErrorAlert").assertExists()
            val expected = application.resources.getString(R.string.error_exit_button_text)
            testRule.onNodeWithText(expected).assertExists()
        }
    }

    @Test
    fun error_displayed_when_fetchWeekQuotes_throws_IOException() {

        application.quoteService = mockk {
            coEvery { fetchQuote() } returns
                    Quote(text = "Test text", author = "Test author")
            coEvery { fetchWeekQuotes() } throws IOException()
        }

        ActivityScenario.launch(QuotesListActivity::class.java).use {
            testRule.onNodeWithTag("QuoteView").assertDoesNotExist()
            testRule.onNodeWithTag("ErrorAlert").assertExists()
            val expected = application.resources.getString(R.string.error_retry_button_text)
            testRule.onNodeWithText(expected).assertExists()
        }
    }
}