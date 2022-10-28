package palbp.laboratory.demos.quoteofday.quotes

import palbp.laboratory.demos.quoteofday.utils.hypermedia.EmbeddedEntity

/**
 * The domain entity for representing quotes
 */
data class Quote(val text: String, val author: String) {
    init { require(text.isNotBlank() && author.isNotBlank()) }
}

/**
 * Creates a [Quote] instance from the given DTO
 */
fun Quote(dto: QuoteDto): Quote {
    val quote = dto.properties
    require(quote != null)
    return Quote(text = quote.text, author = quote.author)
}

/**
 * Creates a [Quote] instance from the given DTO
 */
fun Quote(dto: QuoteListItemDto): Quote {
    val quote = dto.properties
    require(quote != null)
    return Quote(text = quote.text, author = quote.author)
}

/**
 * Creates a [Quote] instance from the given DTO
 */
fun Quote(localDto: LocalQuoteDto): Quote {
    return Quote(text = localDto.text, author = localDto.author)
}

/**
 * Converts this quote to a local DTO, that can be placed in Bundles and
 * passed around between activities.
 */
fun Quote.toLocalDto() = LocalQuoteDto(text, author)

/**
 * Converts this DTO to the corresponding domain representation of a
 * list of quotes
 */
@Suppress("unchecked_cast")
fun QuoteListDto.toQuoteList(): List<Quote> =
    entities?.map {
        val quoteDto = it as EmbeddedEntity<QuoteDtoProperties>
        Quote(quoteDto)
    } ?: emptyList()
