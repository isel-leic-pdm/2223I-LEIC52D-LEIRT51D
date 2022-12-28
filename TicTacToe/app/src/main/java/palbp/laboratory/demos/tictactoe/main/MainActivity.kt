package palbp.laboratory.demos.tictactoe.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import palbp.laboratory.demos.tictactoe.DependenciesContainer
import palbp.laboratory.demos.tictactoe.game.lobby.ui.LobbyActivity
import palbp.laboratory.demos.tictactoe.preferences.ui.PreferencesActivity

/**
 * The start screen.
 */
class MainActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(onStartRequested = ::startGame)
        }
    }

    private fun startGame() {
        if (repo.userInfo != null) {
            LobbyActivity.navigate(this)
        }
        else
            PreferencesActivity.navigate(this)
    }
}
