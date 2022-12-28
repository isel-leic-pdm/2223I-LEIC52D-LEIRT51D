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
import palbp.laboratory.demos.tictactoe.game.play.domain.BoardResult
import palbp.laboratory.demos.tictactoe.game.play.domain.HasWinner
import palbp.laboratory.demos.tictactoe.game.play.domain.Marker
import palbp.laboratory.demos.tictactoe.game.play.domain.Tied
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val MatchEndedDialogTag = "MatchEndedDialog"
const val MatchEndedDialogDismissButtonTag = "MatchEndedDialogDismissButton"

@Composable
fun MatchEndedDialog(
    localPLayerMarker: Marker,
    result: BoardResult,
    onDismissRequested: () -> Unit = { }
) {
    val dialogTextId = when {
        result is Tied -> R.string.match_ended_dialog_text_tied_match
        result is HasWinner && result.winner == localPLayerMarker -> R.string.match_ended_dialog_text_local_won
        else -> R.string.match_ended_dialog_text_opponent_won
    }

    AlertDialog(
        onDismissRequest = onDismissRequested,
        buttons = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onDismissRequested,
                    modifier = Modifier.testTag(MatchEndedDialogDismissButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.match_ended_ok_button),
                        fontSize = 14.sp
                    )
                }
            }
        },
        title = { Text(stringResource(id = R.string.match_ended_dialog_title)) },
        text = { Text(stringResource(id = dialogTextId)) },
        modifier = Modifier.testTag(MatchEndedDialogTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogTiedPreview() {
    TicTacToeTheme {
        MatchEndedDialog(
            localPLayerMarker = Marker.CROSS,
            result = Tied()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogLocalWonPreview() {
    TicTacToeTheme {
        MatchEndedDialog(
            localPLayerMarker = Marker.CROSS,
            result = HasWinner(winner = Marker.CROSS)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogOpponentWonPreview() {
    TicTacToeTheme {
        MatchEndedDialog(
            localPLayerMarker = Marker.CROSS,
            result = HasWinner(winner = Marker.CIRCLE)
        )
    }
}
