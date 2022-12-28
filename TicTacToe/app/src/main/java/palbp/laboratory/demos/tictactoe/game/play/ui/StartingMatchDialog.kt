package palbp.laboratory.demos.tictactoe.game.play.ui

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
import androidx.compose.ui.unit.sp
import palbp.laboratory.demos.tictactoe.R
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val StartingMatchDialogTag = "StartingMatchDialog"

@Composable
fun StartingMatchDialog(

    onCancelRequested: () -> Unit = { }
) {
    AlertDialog(
        onDismissRequest = { },
        buttons = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onCancelRequested
                ) {
                    Text(
                        text = stringResource(id = R.string.starting_match_cancel_button),
                        fontSize = 14.sp
                    )
                }
            }
        },
        title = { Text(stringResource(id = R.string.starting_match_dialog_title)) },
        text = { Text(stringResource(id = R.string.starting_match_dialog_text)) },
        modifier = Modifier.testTag(StartingMatchDialogTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun PendingChallengeDialogPreview() {
    TicTacToeTheme {
        StartingMatchDialog()
    }
}
