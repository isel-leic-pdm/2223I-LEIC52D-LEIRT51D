package palbp.laboratory.demos.quoteofday.quotes

import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenEntity

data class QuoteDtoProperties(
    val id: Long,
    val text: String,
    val author: String
)

typealias QuoteDto = SirenEntity<QuoteDtoProperties>
val QuoteDtoType = SirenEntity.getType<QuoteDtoProperties>()
