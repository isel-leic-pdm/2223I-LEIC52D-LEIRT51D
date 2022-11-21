package palbp.laboratory.demos.tictactoe.ui

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
import palbp.laboratory.demos.tictactoe.preferences.UserInfo
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val UserInfoViewTag = "UserInfoView"

@Composable
fun UserInfoView(
    userInfo: UserInfo,
    onUserSelected: (UserInfo) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserSelected(userInfo) }
            .testTag(UserInfoViewTag)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = userInfo.nick,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (userInfo.moto != null) {
                Text(
                    text = userInfo.moto,
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
        UserInfoView(
            userInfo = UserInfo("My Nick"),
            onUserSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoViewPreview() {
    TicTacToeTheme {
        UserInfoView(
            userInfo = UserInfo("My Nick", "This is my moto"),
            onUserSelected = { }
        )
    }
}
