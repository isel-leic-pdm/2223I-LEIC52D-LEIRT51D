package palbp.laboratory.demos.quoteofday.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.quoteofday.quotes.Quote

@Composable
fun ExpandableQuoteView(quote: Quote, onSelected: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    StatelessExpandableQuoteView(
        quote = quote,
        isExpanded = isExpanded,
        onExpandedToggleRequest = { isExpanded = !isExpanded },
        onSelected = onSelected
    )
}

val expandedPropertyKey: SemanticsPropertyKey<Boolean> = SemanticsPropertyKey("Expanded")

@Composable
private fun StatelessExpandableQuoteView(
    quote: Quote,
    isExpanded: Boolean,
    onExpandedToggleRequest: () -> Unit = { },
    onSelected: () -> Unit = { }
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .testTag("ExpandableQuoteView")
            .semantics { set(expandedPropertyKey, isExpanded) }
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onSelected)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val maxLines: Int by animateIntAsState(
                    targetValue = if (isExpanded) 10 else 1,
                    animationSpec = tween(
                        delayMillis = 50,
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
                Text(
                    text = quote.text,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Start,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                )
                val icon =
                    if (isExpanded) Icons.Default.ArrowDropUp
                    else Icons.Default.ArrowDropDown
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onExpandedToggleRequest)
                        .testTag("ExpandableQuoteView.ExpandAction")
                )
            }
            Text(
                text = quote.author,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandedQuoteViewPreview() {
    StatelessExpandableQuoteView(quote = aQuote, isExpanded = true)
}

@Preview(showBackground = true)
@Composable
private fun CollapsedQuoteViewPreview() {
    StatelessExpandableQuoteView(quote = aQuote, isExpanded = false)
}

@Preview(showBackground = true)
@Composable
private fun ExpandableQuoteViewPreview() {
    ExpandableQuoteView(quote = aQuote, onSelected = { })
}

private val aQuote = Quote(
    text = "O poeta é um fingidor.\n" +
            "Finge tão completamente\n" +
            "Que chega a fingir que é dor\n" +
            "A dor que deveras sente.",
    author = "Fernando Pessoa"
)
