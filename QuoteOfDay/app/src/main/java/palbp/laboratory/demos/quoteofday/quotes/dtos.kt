package palbp.laboratory.demos.quoteofday.quotes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenEntity

data class QuoteDtoProperties(
    val id: Long,
    val text: String,
    val author: String
)

typealias QuoteDto = SirenEntity<QuoteDtoProperties>
val QuoteDtoType = SirenEntity.getType<QuoteDtoProperties>()

@Parcelize
data class LocalQuoteDto(
    val text: String,
    val author: String
) : Parcelable