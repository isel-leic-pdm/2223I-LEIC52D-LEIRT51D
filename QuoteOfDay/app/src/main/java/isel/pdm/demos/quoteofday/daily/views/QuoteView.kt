package isel.pdm.demos.quoteofday.daily.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.daily.Quote
import isel.pdm.demos.quoteofday.ui.theme.QuoteOfDayTheme

@Composable
fun QuoteView(quote: Quote) {
    Log.v(TAG, "QuoteView composed")
    Column(
        modifier = Modifier.padding(all = 64.dp).testTag(tag = "QuoteView")
    ) {
        Text(
            text = quote.content,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
        Text(
            text = quote.author,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun QuoteViewPreview() {
    val quoteText = "O poeta é um fingidor.\n" +
            "Finge tão completamente\n" +
            "Que chega a fingir que é dor\n" +
            "A dor que deveras sente."

    val quote = Quote(quoteText, "Fernando Pessoa")

    QuoteOfDayTheme {
        QuoteView(quote = quote)
    }
}
