package isel.pdm.demos.quoteofday.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import isel.pdm.demos.quoteofday.DependenciesContainer
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.daily.views.LoadingState
import isel.pdm.demos.quoteofday.utils.loggableMutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyQuoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fakeService = (application as DependenciesContainer).quoteOfDayService
        Log.v(TAG, application.javaClass.name)
        setContent {
            Log.v(TAG, "root composed")
            val quote = remember {
                Log.v(TAG, "Inside remember quote calculation")
                loggableMutableStateOf<Quote?>(
                    at = "root.quote",
                    value = null
                )
            }
            val isLoading = remember {
                Log.v(TAG, "Inside remember isLoading calculation")
                loggableMutableStateOf(
                    at = "root.isLoading",
                    value = LoadingState.Idle
                )
            }
            Log.v(TAG, "Composing activity content")
            QuoteOfDayScreen(
                quote = quote.value,
                state = isLoading.value,
                onUpdateRequested = {
                    CoroutineScope(Dispatchers.Main).launch {
                        Log.v(TAG, "onUpdateRequested()")
                        isLoading.value = LoadingState.Loading
                        quote.value = fakeService.getTodayQuote()
                        isLoading.value = LoadingState.Idle
                    }
                }
            )
        }
    }
}
