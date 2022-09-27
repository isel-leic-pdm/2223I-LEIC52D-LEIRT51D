package isel.pdm.demos.quoteofday

import android.app.Application
import isel.pdm.demos.quoteofday.daily.FakeQuoteOfDayService
import isel.pdm.demos.quoteofday.daily.QuoteOfDayService

const val TAG = "QuoteOfDayApp"

interface DependenciesContainer {
    val quoteOfDayService: QuoteOfDayService
}

class QuoteOfDayApplication : DependenciesContainer, Application() {
    override val quoteOfDayService: QuoteOfDayService
        get() = FakeQuoteOfDayService()
}
