package palbp.laboratory.demos.quoteofday.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import palbp.laboratory.demos.quoteofday.R
import palbp.laboratory.demos.quoteofday.ui.theme.QuoteOfDayTheme

/**
 * Used to aggregate [TopBar] navigation handlers.
 */
data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onHistoryRequested: (() -> Unit)? = null,
    val onInfoRequested: (() -> Unit)? = null,
)

// Test tags for the TopBar navigation elements
const val NavigateBackTestTag = "NavigateBack"
const val NavigateToHistoryTestTag = "NavigateToHistory"
const val NavigateToInfoTestTag = "NavigateToInfo"

@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers()) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if (navigation.onHistoryRequested != null) {
                IconButton(
                    onClick = navigation.onHistoryRequested,
                    modifier = Modifier.testTag(NavigateToHistoryTestTag)
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_history)
                    )
                }
            }
            if (navigation.onInfoRequested != null) {
                IconButton(
                    onClick = navigation.onInfoRequested,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_info)
                    )
                }
            }
        }
    )}

@Preview
@Composable
private fun TopBarPreviewInfoAndHistory() {
    QuoteOfDayTheme {
        TopBar(
            NavigationHandlers(onInfoRequested = { }, onHistoryRequested = { })
        )
    }
}

@Preview
@Composable
private fun TopBarPreviewBackAndInfo() {
    QuoteOfDayTheme {
        TopBar(
            NavigationHandlers(onBackRequested = { }, onInfoRequested = { })
        )
    }
}

@Preview
@Composable
private fun TopBarPreviewBack() {
    QuoteOfDayTheme {
        TopBar(NavigationHandlers(onBackRequested = { }))
    }
}
