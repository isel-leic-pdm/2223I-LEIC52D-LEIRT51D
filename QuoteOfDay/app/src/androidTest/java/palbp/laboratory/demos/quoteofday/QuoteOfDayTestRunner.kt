package palbp.laboratory.demos.quoteofday

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import io.mockk.coEvery
import io.mockk.mockk
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.QuoteService

class QuoteOfDayTestApplication : DependenciesContainer, Application() {
    override var quoteService: QuoteService =
        mockk {
            coEvery { fetchQuote() } returns
                Quote(text = "Test text", author = "Test author")

            coEvery { fetchWeekQuotes() } returns
                buildList {
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

@Suppress("unused")
class QuoteOfDayTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, QuoteOfDayTestApplication::class.java.name, context)
    }
}
