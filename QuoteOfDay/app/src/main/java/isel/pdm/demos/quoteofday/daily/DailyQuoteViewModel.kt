package isel.pdm.demos.quoteofday.daily

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DailyQuoteViewModel(
    private val service: QuoteOfDayService
) : ViewModel() {

    private val _quote: MutableState<Quote?> = mutableStateOf(null)
    val quote: State<Quote?>
        get() = _quote

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    fun fetchQuoteOfDay() {
        viewModelScope.launch {

            _isLoading.value = true
            _quote.value = service.getTodayQuote()
            _isLoading.value = false
        }
    }
}