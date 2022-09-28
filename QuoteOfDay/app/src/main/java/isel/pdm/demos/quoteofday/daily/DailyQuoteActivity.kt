package isel.pdm.demos.quoteofday.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.pdm.demos.quoteofday.DependenciesContainer
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.daily.views.LoadingState

class DailyQuoteActivity : ComponentActivity() {

    private val vm by viewModels<DailyQuoteViewModel>()
    private val service by lazy {
        (application as DependenciesContainer).quoteOfDayService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quote = vm.quote
        val isLoading =
            if(vm.isLoading.value) LoadingState.Loading
            else LoadingState.Idle

        Log.v(TAG, application.javaClass.name)
        setContent {
            Log.v(TAG, "root composed")
            QuoteOfDayScreen(
                quote = quote.value,
                state = isLoading,
                onUpdateRequested = {
                    vm.fetchQuoteOfDay(service)
                }
            )
        }
    }
}
