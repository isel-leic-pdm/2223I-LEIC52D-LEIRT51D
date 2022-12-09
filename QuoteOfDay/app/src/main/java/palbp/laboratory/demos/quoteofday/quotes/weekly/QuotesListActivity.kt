package palbp.laboratory.demos.quoteofday.quotes.weekly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import palbp.laboratory.demos.quoteofday.DependenciesContainer
import palbp.laboratory.demos.quoteofday.R
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.info.InfoActivity
import palbp.laboratory.demos.quoteofday.quotes.ApiException
import palbp.laboratory.demos.quoteofday.quotes.daily.QuoteActivity
import palbp.laboratory.demos.quoteofday.quotes.toLocalDto
import palbp.laboratory.demos.quoteofday.ui.ErrorAlert
import palbp.laboratory.demos.quoteofday.ui.NavigationHandlers
import palbp.laboratory.demos.quoteofday.ui.RefreshingState
import palbp.laboratory.demos.quoteofday.utils.viewModelInit
import java.io.IOException

/**
 * The activity that hosts the screen for displaying a list of quotes. The
 * list to be displayed is fetched from the API and it bears this week's quotes.
 */
class QuotesListActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, QuotesListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: QuotesListScreenViewModel by viewModels {
        viewModelInit {
            QuotesListScreenViewModel(dependencies.quoteService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "QuotesListActivity.onCreate()")

        if (viewModel.quotes == null)
            viewModel.fetchWeekQuotes()

        setContent {
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val quotes = viewModel.quotes?.getOrNull() ?: emptyList()
            QuotesListScreen(
                state = QuotesListScreenState(quotes, loadingState),
                onQuoteSelected = {
                    QuoteActivity.navigate(origin = this, quote = it.toLocalDto())
                },
                onUpdateRequest = { viewModel.fetchWeekQuotes(forcedRefresh = true) },
                onNavigationRequested = NavigationHandlers(
                    onBackRequested = { finish() },
                    onInfoRequested = { InfoActivity.navigate(origin = this) }
                )
            )

            if (viewModel.quotes?.isFailure == true)
                ErrorMessage()
        }
    }

    @Composable
    private fun ErrorMessage() {
        try { viewModel.quotes?.getOrThrow() }
        catch (e: IOException) {
            ErrorAlert(
                title = R.string.error_api_title,
                message = R.string.error_could_not_reach_api,
                buttonText = R.string.error_retry_button_text,
                onDismiss = { viewModel.fetchWeekQuotes() }
            )
        }
        catch (e: ApiException) {
            ErrorAlert(
                title = R.string.error_api_title,
                message = R.string.error_unknown_api_response,
                buttonText = R.string.error_exit_button_text,
                onDismiss = { finishAndRemoveTask() }
            )
        }
    }
}