package palbp.laboratory.demos.tictactoe.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import palbp.laboratory.demos.tictactoe.DependenciesContainer
import palbp.laboratory.demos.tictactoe.TAG
import palbp.laboratory.demos.tictactoe.game.GameActivity
import palbp.laboratory.demos.tictactoe.preferences.PreferencesActivity
import palbp.laboratory.demos.tictactoe.utils.viewModelInit

class LobbyActivity : ComponentActivity() {

    private val viewModelPull by viewModels<LobbyScreenViewModelPull> {
        viewModelInit {
            val lobby = (application as DependenciesContainer).lobbyPull
            LobbyScreenViewModelPull(lobby)
        }
    }

    private val viewModel by viewModels<LobbyScreenViewModel> {
        viewModelInit {
            val lobby = (application as DependenciesContainer).lobby
            LobbyScreenViewModel(lobby)
        }
    }

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
            // val players = viewModelPull.players
            val players by viewModel.players.collectAsState()
            LobbyScreen(
                state = LobbyScreenState(players),
                onBackRequested = { finish() },
                onPreferencesRequested = {
                    PreferencesActivity.navigate(this, finishOnSave = true)
                },
                onPlayerSelected = { GameActivity.navigate(this) }
            )
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                Log.v(TAG, "LobbyActivity.onStart()")
                viewModel.enterLobby()
                //viewModelPull.enterLobby()
            }

            override fun onStop(owner: LifecycleOwner) {
                Log.v(TAG, "LobbyActivity.onStop()")
                viewModel.enterLobby()
                //viewModelPull.leaveLobby()
            }
        })
    }
}