package palbp.laboratory.demos.quoteofday.quotes.weekly

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.ui.*
import palbp.laboratory.demos.quoteofday.ui.theme.QuoteOfDayTheme

data class QuotesListScreenState(
    val quotes: List<Quote> = emptyList(),
    val isLoading: RefreshingState = RefreshingState.Idle
)

@Composable
fun QuotesListScreen(
    state: QuotesListScreenState = QuotesListScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onQuoteSelected: (Quote) -> Unit = { },
    onUpdateRequest: () -> Unit = { }
) {
    QuoteOfDayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag("QuotesListScreen"),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(navigation = onNavigationRequested) },
            floatingActionButton = {
                RefreshFab(
                    onClick = onUpdateRequest,
                    state = state.isLoading
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(state.quotes) {
                    ExpandableQuoteView(
                        quote = it,
                        onSelected = { onQuoteSelected(it) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun QuotesListScreenPreview() {
    QuotesListScreen(QuotesListScreenState())
}
