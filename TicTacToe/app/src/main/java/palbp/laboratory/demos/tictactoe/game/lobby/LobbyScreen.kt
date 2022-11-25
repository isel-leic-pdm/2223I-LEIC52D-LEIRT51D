package palbp.laboratory.demos.tictactoe.game.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import palbp.laboratory.demos.tictactoe.preferences.UserInfo
import palbp.laboratory.demos.tictactoe.ui.NavigationHandlers
import palbp.laboratory.demos.tictactoe.ui.TopBar
import palbp.laboratory.demos.tictactoe.ui.UserInfoView
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val LobbyScreenTag = "LobbyScreen"

data class LobbyScreenState(
    val players: List<PlayerInfo> = emptyList()
)

@Composable
fun LobbyScreen(
    state: LobbyScreenState = LobbyScreenState(),
    onPlayerSelected: (UserInfo) -> Unit = { },
    onBackRequested: () -> Unit = { },
    onPreferencesRequested: () -> Unit = { }
) {
    TicTacToeTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTag),
            topBar = {
                TopBar(
                    NavigationHandlers(onBackRequested, onPreferencesRequested)
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.lobby_screen_title),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(state.players) {
                        UserInfoView(userInfo = it.info, onPlayerSelected)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TicTacToeTheme {
        LobbyScreen(
            state = LobbyScreenState(players),
        )
    }
}

private val players = buildList {
    repeat(30) {
        add(
            PlayerInfo(
            UserInfo("My Nick $it", "This is my $it moto"))
        )
    }
}