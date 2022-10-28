package palbp.laboratory.demos.quoteofday.quotes.daily

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import palbp.laboratory.demos.quoteofday.TAG
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.QuoteService
import palbp.laboratory.demos.quoteofday.utils.loggableMutableStateOf

class QuoteScreenViewModel(
    private val quoteService: QuoteService
): ViewModel() {

    init {
        Log.v(TAG, "QuoteScreenViewModel.init()")
    }

    private var _isLoading by
        loggableMutableStateOf("QuoteScreenViewModel.isLoading", false)
    val isLoading: Boolean
        get() = _isLoading

    private var _quote by
        loggableMutableStateOf<Quote?>("QuoteScreenViewModel.quote", null)
    val quote: Quote?
        get() = _quote

    fun fetchQuote(forcedRefresh: Boolean = true) {
        // TODO: Check if we have connectivity and call QuoteService accordingly
        viewModelScope.launch {
            _isLoading = true
            _quote = quoteService.fetchQuote(
                if (forcedRefresh) QuoteService.Mode.FORCE_REMOTE
                else QuoteService.Mode.AUTO
            )
            _isLoading = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "QuoteScreenViewModel.onCleared()")
    }
}

