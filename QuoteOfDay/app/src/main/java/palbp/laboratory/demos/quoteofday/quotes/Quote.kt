package palbp.laboratory.demos.quoteofday.quotes

import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenEntity

data class Quote(val text: String, val author: String) {
    init { require(text.isNotBlank() && author.isNotBlank()) }
}

fun Quote(dto: SirenEntity<QuoteDtoProperties>): Quote {
    val quote = dto.properties
    require(quote != null)
    return Quote(text = quote.text, author = quote.author)
}