package palbp.laboratory.demos.tictactoe.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import palbp.laboratory.demos.tictactoe.preferences.PreferencesActivity

class LobbyActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LobbyScreen(
                state = LobbyScreenState(),
                onBackRequested = { finish() },
                onPreferencesRequested = {
                    PreferencesActivity.navigate(this, finishOnSave = true)
                }
            )
        }
    }
}