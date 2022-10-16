package palbp.laboratory.demos.quoteofday.quotes

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenMediaType
import palbp.laboratory.demos.quoteofday.utils.send
import java.net.URL

interface QuoteService {
    suspend fun fetchQuote(): Quote
    suspend fun fetchWeekQuotes(): List<Quote>
}

class RealQuoteService(
    private val httpClient: OkHttpClient,
    private val quoteHome: URL,
    private val jsonEncoder: Gson
) : QuoteService {

    override suspend fun fetchQuote(): Quote {
        val request = Request.Builder()
            .url(quoteHome)
            .build()

        Log.v(TAG, "fetchQuote: before request.send in Thread = ${Thread.currentThread().name}")

        val quoteDto = request.send(httpClient) { response ->
            Log.v(TAG, "fetchQuote: inside response handler in Thread = ${Thread.currentThread().name}")
            val contentType = response.body?.contentType()
            if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                jsonEncoder.fromJson<QuoteDto>(
                    response.body?.string(),
                    QuoteDtoType.type
                )
            }
            else {
                Log.e(TAG, "fetchQuote: Got response status ${response.code} from API. Is the home URL correct?")
                TODO()
            }
        }

        Log.v(TAG, "fetchQuote: after request.send in Thread = ${Thread.currentThread().name}")
        return Quote(quoteDto)
    }

    override suspend fun fetchWeekQuotes(): List<Quote> {
        TODO()
    }
}