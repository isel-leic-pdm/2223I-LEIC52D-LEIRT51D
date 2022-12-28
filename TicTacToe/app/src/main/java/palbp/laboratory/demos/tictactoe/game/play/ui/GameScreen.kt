package palbp.laboratory.demos.tictactoe.game.play.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.tictactoe.R
import palbp.laboratory.demos.tictactoe.game.play.domain.Board
import palbp.laboratory.demos.tictactoe.game.play.domain.Coordinate
import palbp.laboratory.demos.tictactoe.game.play.domain.Game
import palbp.laboratory.demos.tictactoe.game.play.domain.Marker
import palbp.laboratory.demos.tictactoe.ui.TopBar
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

internal const val GameScreenTag = "GameScreen"
internal const val GameScreenTitleTag = "GameScreenTitle"
internal const val ForfeitButtonTag = "ForfeitButton"

data class GameScreenState(
    @StringRes val title: Int?,
    val game: Game
)

@Composable
fun GameScreen(
    state: GameScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onForfeitRequested: () -> Unit = { },
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTag),
            topBar = {
                TopBar()
            },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                val titleTextId = when {
                    state.title != null -> state.title
                    state.game.localPlayerMarker == state.game.board.turn ->
                        R.string.game_screen_your_turn
                    else -> R.string.game_screen_opponent_turn
                }
                Text(
                    text = stringResource(id = titleTextId),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.testTag(GameScreenTitleTag)
                )
                BoardView(
                    board = state.game.board,
                    onTileSelected = onMoveRequested,
                    enabled = state.game.localPlayerMarker == state.game.board.turn,
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(1.0f, true)
                        .fillMaxSize()
                )
                Button(
                    onClick = onForfeitRequested,
                    modifier = Modifier.testTag(ForfeitButtonTag)
                ) {
                    Text(text = stringResource(id = R.string.game_screen_forfeit))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    GameScreen(state = GameScreenState(
        title = null,
        Game(localPlayerMarker = Marker.CROSS, board = aBoard)
    ))
}

@Preview(showBackground = true)
@Composable
private fun GameScreenWaiting() {
    GameScreen(state = GameScreenState(
        title = R.string.game_screen_waiting,
        Game(localPlayerMarker = Marker.CROSS, board = Board(Marker.CROSS))
    ))
}

private val aBoard = Board(
    turn = Marker.CIRCLE,
    tiles = listOf(
        listOf(Marker.CROSS, null, Marker.CIRCLE),
        listOf(null, Marker.CROSS, Marker.CIRCLE),
        listOf(Marker.CIRCLE, null, Marker.CROSS)
    )
)

