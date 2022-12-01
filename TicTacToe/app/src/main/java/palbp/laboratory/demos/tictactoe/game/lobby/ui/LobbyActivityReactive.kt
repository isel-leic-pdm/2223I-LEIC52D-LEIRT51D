package palbp.laboratory.demos.tictactoe.game.lobby.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import palbp.laboratory.demos.tictactoe.DependenciesContainer
import palbp.laboratory.demos.tictactoe.TAG
import palbp.laboratory.demos.tictactoe.game.lobby.model.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.game.play.ui.GameActivity
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfo
import palbp.laboratory.demos.tictactoe.preferences.ui.PreferencesActivity

/**
 * The screen used to display the list of players in the lobby, that is, available to play.
 * This a reactive version of this screen. An alternative approach is used in [LobbyActivity].
 *
 */
class LobbyActivityReactive : ComponentActivity() {

    private val lobby: Lobby by lazy {
        (application as DependenciesContainer).lobby
    }

    private val localUser: UserInfo by lazy {
        val localUserInfo = (application as DependenciesContainer).userInfoRepo.userInfo
        checkNotNull(localUserInfo)
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LobbyActivityReactive::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            Log.v(TAG, "before repeatOnLifeCycle")
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                lobby.enterAndObserve(PlayerInfo(localUser)).collect {
                    setContent {
                        LobbyScreenContent(it)
                    }
                }
            }
            Log.v(TAG, "After repeatOnLifeCycle")
        }

        setContent {
            LobbyScreenContent()
        }
    }

    @Composable
    private fun LobbyScreenContent(players: List<PlayerInfo> = emptyList()) {
        LobbyScreen(
            state = LobbyScreenState(players),
            onPlayerSelected = {
                GameActivity.navigate(this)
            },
            onBackRequested = { finish() },
            onPreferencesRequested = {
                PreferencesActivity.navigate(this, finishOnSave = true)
            }
        )
    }
}