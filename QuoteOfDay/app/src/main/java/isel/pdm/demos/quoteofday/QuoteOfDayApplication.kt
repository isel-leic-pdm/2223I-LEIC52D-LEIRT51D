package isel.pdm.demos.quoteofday

import android.app.Application
import isel.pdm.demos.quoteofday.main.FakeQuoteOfDayService
import isel.pdm.demos.quoteofday.main.QuoteOfDayService

interface DependenciesContainer {
    val quoteOfDayService: QuoteOfDayService
}

class QuoteOfDayApplication : DependenciesContainer, Application() {
    override val quoteOfDayService: QuoteOfDayService
        get() = FakeQuoteOfDayService()
}
