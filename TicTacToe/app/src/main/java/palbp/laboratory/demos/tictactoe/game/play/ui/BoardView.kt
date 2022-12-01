package palbp.laboratory.demos.tictactoe.game.play.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.tictactoe.game.play.model.BOARD_SIDE
import palbp.laboratory.demos.tictactoe.game.play.model.Board
import palbp.laboratory.demos.tictactoe.game.play.model.Coordinate
import palbp.laboratory.demos.tictactoe.game.play.model.Marker
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun BoardView(
    board: Board,
    onTileSelected: (at: Coordinate) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        repeat(BOARD_SIDE) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1.0f, fill = true)
            ) {
                repeat(BOARD_SIDE) { column ->
                    val at = Coordinate(row, column)
                    TileView(
                        move = board[at],
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        onSelected = { onTileSelected(at) },
                    )
                    if (column != BOARD_SIDE - 1) { VerticalSeparator() }
                }
            }
            if (row != BOARD_SIDE - 1) { HorizontalSeparator() }
        }
    }
}

@Composable
private fun HorizontalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(MaterialTheme.colors.secondary)
    )
}

@Composable
private fun VerticalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxHeight()
        .width(8.dp)
        .background(MaterialTheme.colors.secondary)
    )
}


@Preview(showBackground = true)
@Composable
private fun EmptyBoardViewPreview() {
    TicTacToeTheme {
        BoardView(board = Board(), onTileSelected = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun NonEmptyBoardViewPreview() {
    TicTacToeTheme {
        BoardView(board = aBoard, onTileSelected = { })
    }
}

private val aBoard = Board(
    turn = Marker.CIRCLE,
    tiles = listOf(
        listOf(Marker.CROSS, null, Marker.CIRCLE),
        listOf(null, Marker.CROSS, Marker.CIRCLE),
        listOf(Marker.CIRCLE, null, Marker.CROSS)
    )
)

