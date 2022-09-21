package isel.pdm.demos.quoteofday.main

interface QuoteOfDayService {
    fun getTodayQuote(): Quote
}

class FakeQuoteOfDayService : QuoteOfDayService {
    override fun getTodayQuote(): Quote {
        val quoteText = "O poeta é um fingidor.\n" +
                "Finge tão completamente\n" +
                "Que chega a fingir que é dor\n" +
                "A dor que deveras sente."

        val quote = Quote(quoteText, "Fernando Pessoa")
        Thread.sleep(3000)
        return quote
    }
}