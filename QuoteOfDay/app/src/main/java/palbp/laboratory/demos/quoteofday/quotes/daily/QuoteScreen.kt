package palbp.laboratory.demos.quoteofday.quotes.daily

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.ui.*
import palbp.laboratory.demos.quoteofday.ui.theme.QuoteOfDayTheme

data class QuoteScreenState(
    val quote: Quote? = null,
    val loadingState: RefreshingState = RefreshingState.Idle
)

@Composable
fun QuoteScreen(
    state: QuoteScreenState = QuoteScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onUpdateRequest: (() -> Unit)? = null,
) {
    Log.i(TAG, "QuoteOfDayScreen: composing")
    QuoteOfDayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag("QuoteScreen"),
            backgroundColor = MaterialTheme.colors.background,
            floatingActionButton = {
                if (onUpdateRequest != null) {
                    RefreshFab(
                        onClick = onUpdateRequest,
                        state = state.loadingState
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            topBar = {
                TopBar(navigation = onNavigationRequested)
            }
        ) { innerPadding ->
            Log.i(TAG, "QuoteOfDayScreen content: composing")
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                if (state.quote != null)
                    QuoteView(quote = state.quote)
            }
        }
    }
}

private val loremIpsum = LoremIpsum(words = 40)

private val loremIpsumQuote = Quote(
    text = loremIpsum.values.joinToString { it },
    author = "Cicero"
)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun QuoteOfDayScreenPreview() {
    QuoteScreen(QuoteScreenState(quote = loremIpsumQuote))
}