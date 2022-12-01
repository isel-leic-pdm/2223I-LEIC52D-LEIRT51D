package palbp.laboratory.demos.tictactoe.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import palbp.laboratory.demos.tictactoe.R
import palbp.laboratory.demos.tictactoe.ui.theme.TicTacToeTheme

const val MainScreenTag = "MainScreen"
const val PlayButtonTag = "PlayButton"

@Composable
fun MainScreen(onStartRequested: () -> Unit = { }) {
    TicTacToeTheme {
        Surface(
            modifier = Modifier.fillMaxSize().testTag(MainScreenTag),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
                Image(
                    painter = painterResource(id = R.drawable.im_tic_tac_toe),
                    contentDescription = "",
                    modifier = Modifier.sizeIn(
                        80.dp, 80.dp,
                        200.dp, 200.dp
                    )
                )

                Button(
                    onClick = onStartRequested,
                    modifier = Modifier.testTag(PlayButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.start_game_button_text)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}