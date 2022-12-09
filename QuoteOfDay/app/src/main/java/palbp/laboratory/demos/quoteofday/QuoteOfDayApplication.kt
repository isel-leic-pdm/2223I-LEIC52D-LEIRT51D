package palbp.laboratory.demos.quoteofday

import android.app.Application
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import okhttp3.Cache
import okhttp3.OkHttpClient
import palbp.laboratory.demos.quoteofday.quotes.*
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SubEntity
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SubEntityDeserializer
import java.net.URL
import java.util.concurrent.TimeUnit

const val TAG = "QuoteOfDayApp"

interface DependenciesContainer {
    val quoteService: QuoteService
}

private val quoteAPIHome = URL("https://406d-2001-690-2008-df53-88a7-3c22-f6ca-459c.ngrok.io")

class QuoteOfDayApplication : DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(Cache(directory = cacheDir, maxSize = 50 * 1024 * 1024))
            .build()
    }

    private val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .registerTypeHierarchyAdapter(
                SubEntity::class.java,
                SubEntityDeserializer<QuoteDtoProperties>(QuoteDtoProperties::class.java)
            )
            .create()
    }

    override val quoteService: QuoteService by lazy {
        RealQuoteService(
            httpClient = httpClient,
            jsonEncoder = jsonEncoder,
            quoteHome = quoteAPIHome
        )
    }

    private val workerConstraints  = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(true)
        .build()

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "QuoteOfDayApplication.onCreate() on process ${android.os.Process.myPid()}")

        val workRequest =
            PeriodicWorkRequestBuilder<QuotesWorker>(repeatInterval = 12, TimeUnit.HOURS)
                .setConstraints(workerConstraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "QuotesWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        Log.v(TAG, "QuotesWorker was scheduled")
    }
}


@Suppress("unused")
private class FakeQuoteService : QuoteService {
    private val aQuote = Quote(
        text = "O poeta é um fingidor.\n" +
                "Finge tão completamente\n" +
                "Que chega a fingir que é dor\n" +
                "A dor que deveras sente.",
        author = "Fernando Pessoa"
    )

    override suspend fun fetchQuote(mode: QuoteService.Mode): Quote {
        delay(3000)
        return aQuote
    }

    override suspend fun fetchWeekQuotes(mode: QuoteService.Mode): List<Quote> {
        delay(3000)
        return buildList { repeat(20) { add(aQuote) } }
    }
}

