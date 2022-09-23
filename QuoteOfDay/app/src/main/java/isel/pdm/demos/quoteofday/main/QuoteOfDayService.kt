package isel.pdm.demos.quoteofday.main

import kotlinx.coroutines.delay

interface QuoteOfDayService {
    suspend fun getTodayQuote(): Quote
}

class FakeQuoteOfDayService : QuoteOfDayService {
    override suspend fun getTodayQuote(): Quote {
        val quoteText = "O poeta é um fingidor.\n" +
                "Finge tão completamente\n" +
                "Que chega a fingir que é dor\n" +
                "A dor que deveras sente."

        val quote = Quote(quoteText, "Fernando Pessoa")
        delay(timeMillis = 3000)
        return quote
    }
}