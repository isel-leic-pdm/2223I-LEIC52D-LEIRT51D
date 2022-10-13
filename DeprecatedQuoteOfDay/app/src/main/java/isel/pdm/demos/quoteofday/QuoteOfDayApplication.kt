package isel.pdm.demos.quoteofday

import android.app.Application
import com.google.gson.Gson
import isel.pdm.demos.quoteofday.daily.FakeQuoteOfDayService
import isel.pdm.demos.quoteofday.daily.QuoteOfDayService
import isel.pdm.demos.quoteofday.daily.RealQuoteOfDayService
import okhttp3.OkHttpClient
import java.net.URL

const val TAG = "QuoteOfDayApp"

interface DependenciesContainer {
    val quoteOfDayService: QuoteOfDayService
}

private val quoteAPIHome = URL("https://4216-2001-690-2008-df53-50eb-1e5c-b012-16d4.ngrok.io")

class QuoteOfDayApplication : DependenciesContainer, Application() {
    private val client by lazy { OkHttpClient() }
    private val gson by lazy { Gson() }

    override val quoteOfDayService: QuoteOfDayService
        get() = RealQuoteOfDayService(
            client = client,
            jsonFormatter = gson,
            homeUrl = quoteAPIHome
        )
}
