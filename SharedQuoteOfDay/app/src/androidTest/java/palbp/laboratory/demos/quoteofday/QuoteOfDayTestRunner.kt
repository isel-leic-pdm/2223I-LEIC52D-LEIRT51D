package palbp.laboratory.demos.quoteofday

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.QuoteService

private class TestFakeQuoteService : QuoteService {

    override suspend fun fetchQuote(): Quote {
        return Quote(text = "Test text", author = "Test author")
    }

    override suspend fun fetchWeekQuotes(): List<Quote> {
        return buildList {
            for (count in 1..5) {
                add(
                    Quote(
                        text = "Test text $count",
                        author = "Test author $count"
                    )
                )
            }
        }
    }
}

class QuoteOfDayTestApplication : DependenciesContainer, Application() {
    override val quoteService: QuoteService by lazy { TestFakeQuoteService() }
}

@Suppress("unused")
class QuoteOfDayTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, QuoteOfDayTestApplication::class.java.name, context)
    }
}