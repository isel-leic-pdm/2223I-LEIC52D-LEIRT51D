package palbp.laboratory.demos.quoteofday.quotes

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.*
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenMediaType
import java.io.IOException
import java.net.URL
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

        val quote = request.send(httpClient) { response ->
            val contentType = response.body?.contentType()
            if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                val quoteDto = jsonEncoder.fromJson<QuoteDto>(
                    response.body?.string(),
                    QuoteDtoType.type
                )
                Quote(quoteDto)
            }
            else {
                Log.e(TAG, "onResponse: got response status ${response.code} from API. Is the home URL correct?")
                TODO()
            }
        }
        return quote
    }

    override suspend fun fetchWeekQuotes(): List<Quote> {
        // TODO
        delay(3000)
        return emptyList()
    }
}

private suspend fun Request.send(
    httpClient: OkHttpClient,
    responseHandler: (Response) -> Unit
): Quote {

    return suspendCoroutine { continuation ->
        httpClient.newCall(request = this).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.v(TAG, "onFailure in Thread = ${Thread.currentThread().name}")
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.v(TAG, "onResponse in Thread = ${Thread.currentThread().name}")
                responseHandler(response)
            }
        })
    }
}
