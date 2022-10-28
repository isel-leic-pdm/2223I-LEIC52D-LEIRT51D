package palbp.laboratory.demos.quoteofday.quotes

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenLink
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenMediaType
import palbp.laboratory.demos.quoteofday.utils.send
import java.lang.reflect.Type
import java.net.URL

/**
 * Contract to be supported by quote service implementations. This abstraction's
 * purpose is mostly to facilitate automated testing.
 */
interface QuoteService {

    /**
     * Used to identify how implementations SHOULD behave:
     * - [FORCE_REMOTE] is used to indicate that the operation MUST try to access
     * the remote data source
     * - [FORCE_LOCAL] is usd to indicate that the operation SHOULD only use the
     * the local version of the data, if available
     * - [AUTO] states that the selection of which data to use is left to the
     * implementation.
     */
    enum class Mode { FORCE_REMOTE, FORCE_LOCAL, AUTO }

    /**
     * Gets the daily quote
     * @param mode how the operation should behave. @see [Mode]
     * @return the quote for the day
     */
    suspend fun fetchQuote(mode: Mode = Mode.AUTO): Quote

    /**
     * Gets the week's quotes
     * @param mode how the operation should behave. @see [Mode]
     * @return the week's quotes
     */
    suspend fun fetchWeekQuotes(mode: Mode = Mode.AUTO): List<Quote>
}

/**
 * Actual implementation of the quote service that is responsible for obtaining
 * the quotes from the remote web API. Because the API is hypermedia driven,
 * the service discovers the existing resource links by navigating the APIs
 * responses in search of the corresponding links.
 */
class RealQuoteService(
    private val quoteHome: URL,
    private val httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) : QuoteService {

    override suspend fun fetchQuote(mode: QuoteService.Mode): Quote {
        val request = buildRequest(url = quoteHome, mode = mode)

        val quoteDto = request.send(httpClient) { response ->
            handleResponse<QuoteDto>(response, QuoteDtoType.type)
        }

        weekQuotesLink = getWeekQuotesLink(quoteDto)
        if (weekQuotesLink == null)
            throw UnresolvedLinkException()

        return Quote(quoteDto)
    }

    override suspend fun fetchWeekQuotes(mode: QuoteService.Mode): List<Quote> {

        val weekQuotesURL: URL = ensureWeekQuotesLink()

        val request = buildRequest(url = weekQuotesURL, mode = mode)

        return request.send(httpClient) { response ->
            handleResponse<QuoteListDto>(response, QuoteListDtoType.type)
        }.toQuoteList()
    }

    /**
     * This method's usefulness is circumstantial. In more realistic scenarios
     * we will not be handling API responses with this simplistic approach.
     */
    private fun <T> handleResponse(response: Response, type: Type): T {
        val contentType = response.body?.contentType()
        return if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
            try {
                val body = response.body?.string()
                jsonEncoder.fromJson<T>(body, type)
            }
            catch (e: JsonSyntaxException) {
                throw UnexpectedResponseException(response)
            }
        }
        else {
            throw UnexpectedResponseException(response = response)
        }
    }

    /**
     * The link for the APIs resource bearing the week's quotes, or null if not
     * yet discovered.
     */
    private var weekQuotesLink: SirenLink? = null

    /**
     * Navigates [quoteDto] in search of the link for the APIs resource
     * bearing the week's quotes.
     * @return the link found in the DTO, or null
     */
    private fun getWeekQuotesLink(quoteDto: QuoteDto) =
        quoteDto.links?.find { it.rel.contains("week") }

    /**
     * Makes sure we have the required link, if necessary, by navigating again
     * through the APIs responses, starting at the home resource (the quote of
     * the day, in this case).
     *
     * @return the [URL] instance representing the link for the week's quotes
     * @throws [UnresolvedLinkException] if the link could not be found
     */
    private suspend fun ensureWeekQuotesLink(): URL {
        if (weekQuotesLink == null) {
            fetchQuote()
        }
        val link = weekQuotesLink ?: throw UnresolvedLinkException()
        return link.href.toURL()
    }

    /**
     * Builds a request.
     */
    private fun buildRequest(url: URL, mode: QuoteService.Mode) =
        with(Request.Builder()) {
            when(mode) {
                QuoteService.Mode.FORCE_REMOTE -> cacheControl(CacheControl.FORCE_NETWORK)
                QuoteService.Mode.FORCE_LOCAL -> cacheControl(CacheControl.FORCE_CACHE)
                else -> this
            }
        }.url(url).build()
}

/**
 * Exception throw when a required navigation link could not be found by
 * the service implementation in the APIs responses.
 */
class UnresolvedLinkException(msg: String = "") : Exception(msg)

/**
 * Exception throw when an unexpected response was received from the API.
 */
class UnexpectedResponseException(
    val response: Response
) : Exception("Unexpected ${response.code} response from the API.")
