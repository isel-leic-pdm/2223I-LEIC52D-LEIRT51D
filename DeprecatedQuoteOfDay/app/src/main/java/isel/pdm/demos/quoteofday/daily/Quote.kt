package isel.pdm.demos.quoteofday.daily

data class Quote(
    val content: String,
    val author: String
) {
    init {
        require(content.isNotBlank())
        require(author.isNotBlank())
    }
}

fun QuoteDto.toQuote() = Quote(
    content = properties?.text ?: "",
    author = properties?.author ?: ""
)
