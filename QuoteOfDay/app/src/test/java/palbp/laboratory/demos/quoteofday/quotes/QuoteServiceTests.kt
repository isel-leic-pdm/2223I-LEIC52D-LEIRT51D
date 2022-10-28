package palbp.laboratory.demos.quoteofday.quotes

import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import palbp.laboratory.demos.quoteofday.testutils.MockWebServerRule
import palbp.laboratory.demos.quoteofday.utils.hypermedia.*

class QuoteServiceTests {

    @get:Rule
    val testRule = MockWebServerRule()

    private val jsonFormatter = GsonBuilder()
        .registerTypeHierarchyAdapter(
            SubEntity::class.java,
            SubEntityDeserializer<QuoteDtoProperties>(QuoteDtoProperties::class.java)
        )
        .create()

    private val httpClient: OkHttpClient = OkHttpClient.Builder().build()
    private val expectedQuoteText = "Text"
    private val expectedQuoteAuthor = "Author"
    private val expectedQuoteCount = 5

    @Test
    fun `fetchQuote returns quote when response is understood`(): Unit =
        runBlocking {

            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(
                    buildQuoteResponseBody(mockServer, withLink = true)
                ))
            )

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            val actual = sut.fetchQuote()

            // Assert
            Assert.assertEquals(expectedQuoteAuthor, actual.author)
            Assert.assertEquals(expectedQuoteText, actual.text)
        }

    @Test(expected = UnexpectedResponseException::class)
    fun `fetchQuote throws when response content is not understood`(): Unit =
        runBlocking{
            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(MockResponse().setBody("Hello"))

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchQuote()
        }

    @Test(expected = UnexpectedResponseException::class)
    fun `fetchQuote throws when response status code is unexpected`(): Unit =
        runBlocking {
            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(MockResponse().setResponseCode(404))

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchQuote()
        }

    @Test(expected = UnresolvedLinkException::class)
    fun `fetchQuote throws when link for week resource is not found`(): Unit =
        runBlocking {

            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(
                    buildQuoteResponseBody(mockServer, withLink = false)
                ))
            )

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchQuote()
        }

    @Test
    fun `fetchWeekQuotes returns quotes when response is understood`(): Unit =
        runBlocking {

            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(
                    buildQuoteResponseBody(mockServer, withLink = true)
                ))
            )
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(buildWeekQuotesResponseBody(mockServer)))
            )

            val sut = RealQuoteService(
                mockServer.url("/").toUrl(),
                httpClient,
                jsonFormatter
            )

            // Act
            val actual = sut.fetchWeekQuotes()

            // Assert
            Assert.assertEquals(expectedQuoteCount, actual.size)
            actual.forEach {
                Assert.assertEquals(expectedQuoteText, it.text)
                Assert.assertEquals(expectedQuoteAuthor, it.author)
            }
        }

    @Test(expected = UnexpectedResponseException::class)
    fun `fetchWeekQuotes throws when response content is not understood`(): Unit =
        runBlocking{
            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(
                    buildQuoteResponseBody(mockServer, withLink = true)
                ))
            )
            mockServer.enqueue(MockResponse().setBody("Hello"))

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchWeekQuotes()
        }

    @Test(expected = UnexpectedResponseException::class)
    fun `fetchWeekQuotes throws when response status code is unexpected`(): Unit =
        runBlocking {
            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(
                    buildQuoteResponseBody(mockServer, withLink = true)
                ))
            )
            mockServer.enqueue(MockResponse().setResponseCode(404))

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchWeekQuotes()
        }

    @Test(expected = UnresolvedLinkException::class)
    fun `fetchWeekQuotes throws when link for week resource is not found`(): Unit =
        runBlocking {

            // Arrange
            val mockServer = testRule.server
            mockServer.enqueue(response = MockResponse()
                .setHeader("content-type", SirenMediaType)
                .setBody(jsonFormatter.toJson(buildWeekQuotesResponseBody(mockServer)))
            )

            val sut = RealQuoteService(mockServer.url("/").toUrl(), httpClient, jsonFormatter)

            // Act
            sut.fetchQuote()
        }

    private fun buildQuoteResponseBody(server: MockWebServer, withLink: Boolean) =
        SirenEntity(
            clazz = listOf("Quote"),
            properties = QuoteDtoProperties(id = 1, expectedQuoteText, expectedQuoteAuthor),
            links = buildList {
                add(SirenLink(rel = listOf("self"), href = server.url("/").toUri()))
                if (withLink)
                    add(SirenLink(rel = listOf("week"), href =  server.url("/week").toUri()))
            }
        )

    private fun buildWeekQuotesResponseBody(server: MockWebServer) = SirenEntity(
        clazz = listOf("QuotesList"),
        properties = QuoteListDtoProperties(size = expectedQuoteCount),
        entities = buildList {
            repeat(expectedQuoteCount) {
                add(EmbeddedEntity(
                    clazz = listOf("Quote"),
                    rel = listOf("quote"),
                    properties = QuoteDtoProperties(
                        it.toLong(),
                        expectedQuoteText,
                        expectedQuoteAuthor
                    ),
                    links = listOf(SirenLink(rel = listOf("self"), href = server.url("/").toUri()))
                ))
            }
        }
    )
}