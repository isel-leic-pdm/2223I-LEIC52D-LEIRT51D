package palbp.laboratory.demos.quoteofday.utils

import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import palbp.laboratory.demos.quoteofday.testutils.MockWebServerRule


class OkHttpExtensionsTests {

    @get:Rule
    val testRule = MockWebServerRule()

    @Test
    fun `send returns the result of the response handler`() = runBlocking {

        // Arrange
        val expected = "Hello"
        val request = prepareRequest(expected)

        // Act
        val actual = request.send(OkHttpClient()) {
            it.body?.string()
        }

        // Assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `handler executes on another thread`() = runBlocking {

        // Arrange
        val externalThread = Thread.currentThread().id
        val request = prepareRequest()

        // Act
        val handlerThreadId = request.send(OkHttpClient()) {
            Thread.currentThread().id
        }

        // Assert
        Assert.assertNotEquals(externalThread, handlerThreadId)
    }

    @Test(expected = Exception::class)
    fun `send throws the exception thrown by the handler`(): Unit = runBlocking {

        // Arrange
        val request = prepareRequest()

        // Act
        request.send(OkHttpClient()) { throw Exception() }
    }

    private fun prepareRequest(expected: String = "some content"): Request {
        val mockServer = testRule.server
        mockServer.enqueue(MockResponse().setBody(expected))

        val url: HttpUrl = mockServer.url("/")
        return Request.Builder().url(url).build()
    }
}