package isel.pdm.demos.quoteofday.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.pdm.demos.quoteofday.DependenciesContainer
import isel.pdm.demos.quoteofday.TAG
import isel.pdm.demos.quoteofday.daily.views.LoadingState
import isel.pdm.demos.quoteofday.info.InfoActivity

class DailyQuoteActivity : ComponentActivity() {

    private val service by lazy {
        (application as DependenciesContainer).quoteOfDayService
    }

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<DailyQuoteViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DailyQuoteViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, application.javaClass.name)
        setContent {
            val quote = vm.quote
            val isLoading =
                if(vm.isLoading.value) LoadingState.Loading
                else LoadingState.Idle

            Log.v(TAG, "root composed")
            QuoteOfDayScreen(
                quote = quote.value,
                state = isLoading,
                onUpdateRequested = {
                    vm.fetchQuoteOfDay()
                },
                onInfoRequest = { navigateToInfoScreen() }
            )
        }
    }

    private fun navigateToInfoScreen() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}
