package palbp.laboratory.demos.quoteofday

import org.junit.Test
import palbp.laboratory.demos.quoteofday.quotes.Quote

class QuoteTests {

    @Test(expected = IllegalArgumentException::class)
    fun `create quote with blank author throws`() {
        Quote(text = "A quote", author = " \n  \t ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create quote with blank text throws`() {
        Quote(text = "\t   \n", author = "The Author")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create quote with empty text throws`() {
        Quote(text = "", author = "The Author")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create quote with empty author throws`() {
        Quote(text = "A quote", author = "")
    }

    @Test
    fun `create quote with non empty text and author succeeds`() {
        Quote(text = "A quote", author = "The Author")
    }
}

