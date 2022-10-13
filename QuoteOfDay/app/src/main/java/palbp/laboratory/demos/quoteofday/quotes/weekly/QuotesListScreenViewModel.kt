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

    private var _quotes by mutableStateOf<List<Quote>>(emptyList())
    val quotes: List<Quote>
        get() = _quotes

    fun fetchWeekQuotes() {
        viewModelScope.launch {
            _isLoading = true
            _quotes = quoteService.fetchWeekQuotes()
            _isLoading = false
        }
    }
}