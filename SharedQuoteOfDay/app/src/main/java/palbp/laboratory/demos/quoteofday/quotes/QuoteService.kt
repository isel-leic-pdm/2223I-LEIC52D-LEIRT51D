package palbp.laboratory.demos.quoteofday.quotes

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.*
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenMediaType
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

        Log.v(TAG, "fetchQuote: before suspendCoroutine in Thread = ${Thread.currentThread().name}")
        val quote = suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.v(TAG, "fetchQuote: onFailure in Thread = ${Thread.currentThread().name}")
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "fetchQuote: onResponse in Thread = ${Thread.currentThread().name}")
                    val contentType = response.body?.contentType()
                    if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                        val quoteDto = jsonEncoder.fromJson<QuoteDto>(
                            response.body?.string(),
                            QuoteDtoType.type
                        )
                        continuation.resume(Quote(quoteDto))
                    }
                    else {
                        Log.e(TAG, "onResponse: got response status ${response.code} from API. Is the home URL correct?")
                        TODO()
                    }
                }
            })
        }

        Log.v(TAG, "fetchQuote: after suspendCoroutine in Thread = ${Thread.currentThread().name}")
        return quote
    }

    override suspend fun fetchWeekQuotes(): List<Quote> {
        // TODO
        delay(3000)
        return emptyList()
    }
}