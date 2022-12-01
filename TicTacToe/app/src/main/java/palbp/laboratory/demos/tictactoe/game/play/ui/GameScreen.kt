package palbp.laboratory.demos.tictactoe.game.play.ui

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
import palbp.laboratory.demos.tictactoe.game.lobby.ui.LobbyScreenTag
import palbp.laboratory.demos.tictactoe.game.play.model.Board
import palbp.laboratory.demos.tictactoe.game.play.model.Coordinate
import palbp.laboratory.demos.tictactoe.game.play.model.Game
import palbp.laboratory.demos.tictactoe.game.play.model.Marker
import palbp.laboratory.demos.tictactoe.ui.NavigationHandlers
import palbp.laboratory.demos.tictactoe.ui.TopBar
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val GameScreenTag = "GameScreen"

data class GameScreenState(
    val game: Game
)

@Composable
fun GameScreen(
    state: GameScreenState,
    onMoveRequested: (Coordinate) -> Unit = { },
    onForfeitRequested: () -> Unit = { },
    onBackRequested: () -> Unit = { },
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(onBackRequested)
                )
            },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                val turnTextId =
                    if (state.game.localPlayer == state.game.board.turn)
                        R.string.game_screen_your_turn
                    else R.string.game_screen_opponent_turn
                Text(
                    text = stringResource(id = turnTextId),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primaryVariant
                )
                BoardView(
                    board = state.game.board,
                    onTileSelected = { },
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(1.0f, true)
                        .fillMaxSize()
                )
                Button(onClick = onForfeitRequested) {
                    Text(text = "Forfeit")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    GameScreen(state = GameScreenState(Game(Marker.CROSS, aBoard)))
}

private val aBoard = Board(
    turn = Marker.CIRCLE,
    tiles = listOf(
        listOf(Marker.CROSS, null, Marker.CIRCLE),
        listOf(null, Marker.CROSS, Marker.CIRCLE),
        listOf(Marker.CIRCLE, null, Marker.CROSS)
    )
)

