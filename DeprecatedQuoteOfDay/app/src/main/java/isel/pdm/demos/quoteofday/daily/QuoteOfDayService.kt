package isel.pdm.demos.quoteofday.daily

import android.util.Log
import com.google.gson.Gson
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.utils.SirenEntity
import isel.pdm.demos.quoteofday.utils.SirenMediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface QuoteOfDayService {
    suspend fun getTodayQuote(): Quote
}

class RealQuoteOfDayService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val homeUrl: URL
) : QuoteOfDayService {
    override suspend fun getTodayQuote(): Quote {
        Log.v(TAG, "getTodayQuote: Before withContext on thread ${Thread.currentThread().name}")

        val result = suspendCoroutine<Quote> { continuation ->
            val request = Request.Builder()
                .url(homeUrl)
                .build()
            Log.v(TAG, "getTodayQuote: Before newCall.enqueue on thread ${Thread.currentThread().name}")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v(TAG, "onResponse on thread ${Thread.currentThread().name}")
                    val contentType = response.body?.contentType()
                    if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                        val quoteDto = jsonFormatter.fromJson<QuoteDto>(
                            response.body?.string(),
                            QuoteDtoType.type
                        )
                        continuation.resume(quoteDto.toQuote())
                    }
                    else {
                        TODO()
                    }
                }
            })
        }

        Log.v(TAG, "getTodayQuote: After newCall.enqueue on on thread ${Thread.currentThread().name}")
        return result
    }
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