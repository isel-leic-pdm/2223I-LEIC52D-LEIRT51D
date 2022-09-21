package isel.pdm.demos.quoteofday.main

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.pdm.demos.quoteofday.R
import isel.pdm.demos.quoteofday.ui.theme.QuoteOfDayTheme

enum class LoadingState { Idle, Loading }

@Composable
fun LoadingButton(
    state: LoadingState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.v(TAG, "LoadingButton composed")
    Button(
        enabled = state == LoadingState.Idle,
        onClick = onClick,
        modifier = modifier
    ) {
        val text =
            if (state == LoadingState.Idle)
                stringResource(id = R.string.fetch_quote_button_text)
            else
                stringResource(id = R.string.fetch_quote_button_text_loading)
        Text(text = text)
    }
}

@Preview
@Composable
fun LoadingButtonPreviewIdle() {
    QuoteOfDayTheme {
        LoadingButton(
            state = LoadingState.Idle,
            onClick = { },
            modifier = Modifier.Companion.padding(all = 16.dp)
        )
    }
}

@Preview
@Composable
fun LoadingButtonPreviewLoading() {
    QuoteOfDayTheme {
        LoadingButton(
            state = LoadingState.Loading,
            onClick = { },
            modifier = Modifier.Companion.padding(all = 16.dp)
        )
    }
}
