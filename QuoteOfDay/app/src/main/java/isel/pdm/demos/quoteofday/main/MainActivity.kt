package isel.pdm.demos.quoteofday.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

const val TAG = "QuoteOfDayApp"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fakeService = FakeQuoteOfDayService()
        setContent {
            val quote = remember {
                Log.v(TAG, "Inside remember quote calculation")
                mutableStateOf<Quote?>(null)
            }
            val isLoading = remember {
                Log.v(TAG, "Inside remember isLoading calculation")
                mutableStateOf(LoadingState.Idle)
            }
            Log.v(TAG, "Composing activity content")
            QuoteOfDayScreen(
                quote = quote.value,
                state = isLoading.value,
                onUpdateRequested = {
                    Log.v(TAG, "onUpdateRequested()")
                    isLoading.value = LoadingState.Loading
                    quote.value = fakeService.getTodayQuote()
                    isLoading.value = LoadingState.Idle
                }
            )
        }
    }
}
