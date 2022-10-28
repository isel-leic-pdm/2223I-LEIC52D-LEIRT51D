package palbp.laboratory.demos.quoteofday.utils.hypermedia

import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Test
import palbp.laboratory.demos.quoteofday.quotes.QuoteDtoProperties
import palbp.laboratory.demos.quoteofday.quotes.QuoteListDto
import palbp.laboratory.demos.quoteofday.quotes.QuoteListDtoProperties
import palbp.laboratory.demos.quoteofday.quotes.QuoteListDtoType
import java.net.URI

class SirenSerializationTests {

    @Test
    fun `serialized content is equivalent to deserialized result`() {

        // Arrange
        val formatter = GsonBuilder()
            .registerTypeHierarchyAdapter(
                SubEntity::class.java,
                SubEntityDeserializer<QuoteDtoProperties>(QuoteDtoProperties::class.java)
            )
            .create()

        val size = 5
        val expected = SirenEntity(
            clazz = listOf("QuotesList"),
            properties = QuoteListDtoProperties(size),
            entities = buildList {
                repeat(size) {
                    add(EmbeddedEntity(
                        clazz = listOf("Quote"),
                        rel = listOf("quote"),
                        properties = QuoteDtoProperties(it.toLong(), "text", "author"),
                        links = listOf(SirenLink(rel = listOf("self"), href = URI("/")))
                    ))
                }
            }
        )

        // Act
        val json = formatter.toJson(expected)
        val actual = formatter.fromJson<QuoteListDto>(json, QuoteListDtoType.type)

        // Assert
        Assert.assertEquals(expected, actual)
    }
}