package isel.pdm.demos.quoteofday.daily

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.daily.views.LoadingButton
import isel.pdm.demos.quoteofday.daily.views.LoadingState
import isel.pdm.demos.quoteofday.daily.views.QuoteView
import isel.pdm.demos.quoteofday.ui.TopBar
import isel.pdm.demos.quoteofday.ui.theme.QuoteOfDayTheme

@Composable
fun QuoteOfDayScreen(
    quote: Quote? = null,
    state: LoadingState,
    onUpdateRequested: () -> Unit,
    onInfoRequest: () -> Unit
) {
    QuoteOfDayTheme {
        Log.v(TAG, "QuoteOfDayScreen composed")
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onInfoRequested = { onInfoRequest() }) }
        ) { innerPadding ->
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(weight = 1.0f)
                        .padding(innerPadding)
                ) {
                    if (quote != null)
                        QuoteView(quote = quote)
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LoadingButton(
                        state = state,
                        onClick = onUpdateRequested,
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuoteOfDayScreenPreviewWithQuote() {
    val quoteText = "O poeta é um fingidor.\n" +
            "Finge tão completamente\n" +
            "Que chega a fingir que é dor\n" +
            "A dor que deveras sente."

    val quote = Quote(quoteText, "Fernando Pessoa")
    QuoteOfDayTheme {
        QuoteOfDayScreen(
            quote = quote,
            state = LoadingState.Idle,
            onUpdateRequested = { },
            onInfoRequest = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuoteOfDayScreenPreviewWithoutQuote() {
    QuoteOfDayTheme {
        QuoteOfDayScreen(
            state = LoadingState.Idle,
            onUpdateRequested = { },
            onInfoRequest = { }
        )
    }
}
