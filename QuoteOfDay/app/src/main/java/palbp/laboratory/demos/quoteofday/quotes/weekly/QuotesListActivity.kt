package palbp.laboratory.demos.quoteofday.quotes.weekly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import palbp.laboratory.demos.quoteofday.DependenciesContainer
import palbp.laboratory.demos.quoteofday.info.InfoActivity
import palbp.laboratory.demos.quoteofday.quotes.daily.QuoteActivity
import palbp.laboratory.demos.quoteofday.quotes.toLocalDto
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity.Companion.navigate
import palbp.laboratory.demos.quoteofday.ui.RefreshingState
import palbp.laboratory.demos.quoteofday.utils.viewModelInit

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
        setContent {
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle
            QuotesListScreen(
                state = QuotesListScreenState(viewModel.quotes, loadingState),
                onQuoteSelected = {
                    QuoteActivity.navigate(origin = this, quote = it.toLocalDto())
                },
                onBackRequested = { finish() },
                onInfoRequest = { InfoActivity.navigate(origin = this) },
                onUpdateRequest = { viewModel.fetchWeekQuotes() }
            )
        }
    }
}