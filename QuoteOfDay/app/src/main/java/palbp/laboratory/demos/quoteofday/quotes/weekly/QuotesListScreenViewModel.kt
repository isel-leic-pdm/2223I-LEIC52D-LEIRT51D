package palbp.laboratory.demos.quoteofday.quotes.weekly

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import palbp.laboratory.demos.quoteofday.quotes.Quote
import palbp.laboratory.demos.quoteofday.quotes.QuoteService

class QuotesListScreenViewModel(
    private val quoteService: QuoteService
) : ViewModel() {

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _quotes by mutableStateOf<Result<List<Quote>>?>(null)
    val quotes: Result<List<Quote>>?
        get() = _quotes

    fun fetchWeekQuotes(forcedRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading = true
            _quotes =
                try {
                    Result.success(quoteService.fetchWeekQuotes(
                            if (forcedRefresh) QuoteService.Mode.FORCE_REMOTE
                            else QuoteService.Mode.AUTO
                    ))
                }
                catch (e: Exception) { Result.failure(e) }
            _isLoading = false
        }
    }
}