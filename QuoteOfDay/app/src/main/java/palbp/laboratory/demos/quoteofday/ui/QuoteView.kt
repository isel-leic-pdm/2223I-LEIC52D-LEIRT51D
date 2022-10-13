package palbp.laboratory.demos.quoteofday.ui

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
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.quotes.Quote

@Composable
fun QuoteView(quote: Quote) {
    Log.i(TAG, "QuoteView: composing")
    Column(modifier = Modifier.padding(64.dp).testTag("QuoteView")) {
        Text(
            text = quote.text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Start
        )
        Text(
            text = quote.author,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuoteViewPreview() {
    QuoteView(aQuote)
}

private val aQuote = Quote(
    text = "O poeta é um fingidor.\n" +
            "Finge tão completamente\n" +
            "Que chega a fingir que é dor\n" +
            "A dor que deveras sente.",
    author = "Fernando Pessoa"
)