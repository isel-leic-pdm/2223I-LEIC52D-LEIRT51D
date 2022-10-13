package isel.pdm.demos.quoteofday.daily

import isel.pdm.demos.quoteofday.utils.SirenEntity

data class QuoteDtoProperties(
    val id: Long,
    val text: String,
    val author: String
)

typealias QuoteDto = SirenEntity<QuoteDtoProperties>
val QuoteDtoType = SirenEntity.getType<QuoteDtoProperties>()

