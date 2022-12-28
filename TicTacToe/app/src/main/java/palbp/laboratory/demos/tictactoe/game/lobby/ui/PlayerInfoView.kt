package palbp.laboratory.demos.tictactoe.game.lobby.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfo
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val PlayerInfoViewTag = "PlayerInfoView"

@Composable
fun PlayerInfoView(
    playerInfo: PlayerInfo,
    onPlayerSelected: (PlayerInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerSelected(playerInfo) }
            .testTag(PlayerInfoViewTag)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = playerInfo.info.nick,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (playerInfo.info.moto != null) {
                Text(
                    text = playerInfo.info.moto,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewNoMotoPreview() {
    TicTacToeTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick")),
            onPlayerSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewPreview() {
    TicTacToeTheme {
        PlayerInfoView(
            playerInfo = PlayerInfo(UserInfo("My Nick", "This is my moto")),
            onPlayerSelected = { }
        )
    }
}
