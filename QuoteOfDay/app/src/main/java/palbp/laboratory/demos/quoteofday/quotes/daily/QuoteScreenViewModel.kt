package palbp.laboratory.demos.quoteofday.quotes.daily

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.QuoteService

class QuoteScreenViewModel(
    private val quoteService: QuoteService
): ViewModel() {

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _quote by mutableStateOf<Result<Quote>?>(null)
    val quote: Result<Quote>?
        get() = _quote

    fun fetchQuote(forcedRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading = true
            _quote =
                try {
                    Result.success(quoteService.fetchQuote(
                        if (forcedRefresh) QuoteService.Mode.FORCE_REMOTE
                        else QuoteService.Mode.AUTO
                    ))
                }
                catch (e: Exception) { Result.failure(e) }
            _isLoading = false
        }
    }
}

