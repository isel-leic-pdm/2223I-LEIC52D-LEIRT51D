package palbp.laboratory.demos.tictactoe.game.play.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.tictactoe.R
import palbp.laboratory.demos.tictactoe.game.play.domain.Marker
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

internal const val TileViewTag = "TileView"

@Composable
fun TileView(
    move: Marker?,
    enabled: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(MaterialTheme.colors.background)
        .fillMaxSize(1.0f)
        .padding(12.dp)
        .testTag(TileViewTag)
        .clickable(enabled = move == null && enabled) { onSelected() }
    ) {
        if (move != null) {
            val marker = when (move) {
                Marker.CIRCLE -> R.drawable.circle_red
                Marker.CROSS -> R.drawable.cross_red
            }
            Image(
                painter = painterResource(id = marker),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewCirclePreview() {
    TicTacToeTheme {
        TileView(move = Marker.CIRCLE, enabled = true, onSelected = { })
    }
}


@Preview(showBackground = true)
@Composable
private fun TileViewCrossPreview() {
    TicTacToeTheme {
        TileView(move = Marker.CROSS, enabled = true, onSelected = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun TileViewEmptyPreview() {
    TicTacToeTheme {
        TileView(move = null, enabled = true, onSelected = { })
    }
}
