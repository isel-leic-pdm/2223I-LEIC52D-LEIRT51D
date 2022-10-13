@file:Suppress("unused")

package isel.pdm.demos.quoteofday.utils

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.internal.http.HttpMethod
import java.net.URI

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */

private const val APPLICATION_TYPE = "application"
private const val SIREN_SUBTYPE = "vnd.siren+json"

val SirenMediaType = "$APPLICATION_TYPE/$SIREN_SUBTYPE".toMediaType()

/**
 * Gets a Siren self link for the given URI
 *
 * @param uri   the string with the self URI
 * @return the resulting siren link
 */
fun selfLink(uri: String) = SirenLink(rel = listOf("self"), href = URI(uri))

/**
 * Class whose instances represent links as they are represented in Siren.
 */
data class SirenLink(
    val rel: List<String>,
    val href: URI,
    val title: String? = null,
    val type: MediaType? = null)

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
data class SirenAction(
    val name: String,
    val href: URI,
    val title: String? = null,
    @SerializedName("class")
    val clazz: List<String>? = null,
    val method: HttpMethod? = null,
    val type: MediaType? = null,
    val fields: List<Field>? = null
) {
    /**
     * Represents action's fields
     */
    data class Field(
        val name: String,
        val type: String? = null,
        val value: String? = null,
        val title: String? = null
    )
}

data class SirenEntity<T>(
    @SerializedName("class") val clazz: List<String>? = null,
    val properties: T? =null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
) {
    companion object {
        inline fun <reified T> getType(): TypeToken<SirenEntity<T>> =
            object : TypeToken<SirenEntity<T>>() { }
    }
}

/**
 * Base class for admissible sub entities, namely, [EmbeddedLink] and [EmbeddedEntity].
 * Notice that this is a closed class hierarchy.
 */
sealed class SubEntity

data class EmbeddedLink(
    @SerializedName("class")
    val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()

data class EmbeddedEntity<T>(
    val rel: List<String>,
    @SerializedName("class") val clazz: List<String>? = null,
    val properties: T? =null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: SirenAction? = null,
    val title: String? = null
) : SubEntity() {
    companion object {
        inline fun <reified T> getType(): TypeToken<EmbeddedEntity<T>> =
            object : TypeToken<EmbeddedEntity<T>>() { }
    }
}
