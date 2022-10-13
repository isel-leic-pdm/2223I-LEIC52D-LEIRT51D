package palbp.laboratory.demos.quoteofday.quotes.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import palbp.laboratory.demos.quoteofday.DependenciesContainer
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.info.InfoActivity
import palbp.laboratory.demos.quoteofday.quotes.weekly.QuotesListActivity
import palbp.laboratory.demos.quoteofday.ui.RefreshingState
import palbp.laboratory.demos.quoteofday.utils.viewModelInit

class QuoteActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: QuoteScreenViewModel by viewModels {
        viewModelInit {
            QuoteScreenViewModel(dependencies.quoteService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "application: ${application.javaClass.name}")
        Log.v(TAG, "applicationContext: ${applicationContext.javaClass.name}")
        setContent {
            val loadingState: RefreshingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            QuoteOfDayScreen(
                state = QuoteOfDayScreenState(viewModel.quote, loadingState),
                onUpdateRequest = {
                    Log.v(TAG, "QuoteActivity.onUpdateRequest()")
                    viewModel.fetchQuote()
                },
                onInfoRequest = { InfoActivity.navigate(origin = this) },
                onHistoryRequested = { QuotesListActivity.navigate(origin = this) }
            )
        }
    }
}

