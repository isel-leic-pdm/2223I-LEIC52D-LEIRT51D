package palbp.laboratory.demos.quoteofday.quotes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import palbp.laboratory.demos.quoteofday.utils.hypermedia.EmbeddedEntity
import palbp.laboratory.demos.quoteofday.utils.hypermedia.SirenEntity

/**
 * The APIs Quote DTO
 */
typealias QuoteDto = SirenEntity<QuoteDtoProperties>

/**
 * The properties field of an APIs QuoteDTo
 */
data class QuoteDtoProperties(
    val id: Long,
    val text: String,
    val author: String
)

/**
 * The TypeToken instance that describes the type of the Quote DTO properties
 * payload, for convenience.
 */
val QuoteDtoType = SirenEntity.getType<QuoteDtoProperties>()

/**
 * The APIs Quote List DTO
 */
typealias QuoteListDto = SirenEntity<QuoteListDtoProperties>

/**
 * The properties field of an APIs Quote List DTO
 */
data class QuoteListDtoProperties(
    val size: Int
)

/**
 * The TypeToken instance that describes the type of the Quote DTO properties
 * payload, for convenience.
 */
val QuoteListDtoType = SirenEntity.getType<QuoteListDtoProperties>()

/**
 * The APIs Quote DTO contained in quotes' lists.
 */
typealias QuoteListItemDto = EmbeddedEntity<QuoteDtoProperties>

/**
 * Used to create local (to the Android device) out-of-process Quote
 * representations.
 */
@Parcelize
data class LocalQuoteDto(
    val text: String,
    val author: String
) : Parcelable
