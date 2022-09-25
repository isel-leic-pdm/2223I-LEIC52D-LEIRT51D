package isel.pdm.demos.quoteofday

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import isel.pdm.demos.quoteofday.main.Quote
import isel.pdm.demos.quoteofday.main.QuoteOfDayService
import kotlinx.coroutines.delay

const val FAKE_FETCH_DELAY = 2000L

private class TestFakeQuoteService : QuoteOfDayService {

    override suspend fun getTodayQuote(): Quote {
        delay(FAKE_FETCH_DELAY)
        return Quote("Test text", "Test author")
    }
}

class QuoteOfDayTestApplication : DependenciesContainer, Application() {
    override val quoteOfDayService: QuoteOfDayService
        get() = TestFakeQuoteService()
}

@Suppress("unused")
class QuoteOfDayTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, QuoteOfDayTestApplication::class.java.name, context)
    }
}