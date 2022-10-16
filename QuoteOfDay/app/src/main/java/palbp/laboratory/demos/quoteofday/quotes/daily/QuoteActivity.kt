package palbp.laboratory.demos.quoteofday.quotes.daily

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import palbp.laboratory.demos.quoteofday.DependenciesContainer
import palbp.laboratory.demos.quoteofday.info.InfoActivity
import palbp.laboratory.demos.quoteofday.quotes.LocalQuoteDto
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity
import palbp.laboratory.demos.quoteofday.ui.RefreshingState
import palbp.laboratory.demos.quoteofday.utils.viewModelInit

/**
 * The activity that hosts the screen for displaying a single quote. The quote
 * to be displayed may be either fetched from the API, in which case it will be
 * the day's quote, or received as an Intent extra.
 */
class QuoteActivity : ComponentActivity() {

    companion object {
        private const val QUOTE_EXTRA = "QUOTE_EXTRA"
        fun navigate(origin: Activity, quote: LocalQuoteDto? = null) {
            with(origin) {
                val intent = Intent(this, QuoteActivity::class.java)
                if (quote != null)
                    intent.putExtra(QUOTE_EXTRA, quote)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: QuoteScreenViewModel by viewModels {
        viewModelInit {
            QuoteScreenViewModel(dependencies.quoteService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedExtra = quoteExtra
            if (receivedExtra != null) {
                QuoteScreen(
                    state = QuoteScreenState(Quote(receivedExtra), RefreshingState.Idle),
                    onInfoRequest = { InfoActivity.navigate(origin = this) },
                    onBackRequested = { finish() }
                )
            }
            else {
                val loadingState: RefreshingState =
                    if (viewModel.isLoading) RefreshingState.Refreshing
                    else RefreshingState.Idle
                QuoteScreen(
                    state = QuoteScreenState(viewModel.quote, loadingState),
                    onUpdateRequest = { viewModel.fetchQuote() },
                    onInfoRequest = { InfoActivity.navigate(origin = this) },
                    onHistoryRequested = { QuotesListActivity.navigate(origin = this) }
                )
            }
        }
    }

    private val quoteExtra: LocalQuoteDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(QUOTE_EXTRA, LocalQuoteDto::class.java)
            else
                intent.getParcelableExtra(QUOTE_EXTRA)
}

