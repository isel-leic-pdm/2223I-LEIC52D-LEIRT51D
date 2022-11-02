package palbp.laboratory.demos.quoteofday.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.quoteofday.ui.theme.QuoteOfDayTheme

@Composable
fun ErrorAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onDismiss: () -> Unit = { }
) {
    ErrorAlertImpl(
        title = stringResource(id = title),
        message = stringResource(id = message),
        buttonText = stringResource(id = buttonText),
        onDismiss = onDismiss
    )
}

@Composable
private fun ErrorAlertImpl(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        buttons = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onDismiss
                ) {
                    Text(text = buttonText)
                }
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message) },
        modifier = Modifier.testTag("ErrorAlert")
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorAlertImplPreview() {
    QuoteOfDayTheme {
        ErrorAlertImpl(
            title = "Error accessing server",
            message = "Could not ...",
            buttonText = "OK",
            onDismiss = { }
        )
    }
}

