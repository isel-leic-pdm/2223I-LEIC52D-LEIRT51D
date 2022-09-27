package isel.pdm.demos.quoteofday

import isel.pdm.demos.quoteofday.daily.Quote
import org.junit.Test

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QuoteTests {
    @Test(expected = IllegalArgumentException::class)
    fun `create quote with empty content throws`() {
        Quote(content = "    ", author = "Some author")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create quote with empty author throws`() {
        Quote(content = "Some content", author = "  \n\t ")
    }
}