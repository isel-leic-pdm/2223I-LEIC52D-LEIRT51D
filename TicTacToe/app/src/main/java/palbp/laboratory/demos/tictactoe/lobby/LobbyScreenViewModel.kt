package palbp.laboratory.demos.tictactoe.lobby

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import palbp.laboratory.demos.tictactoe.TAG
import palbp.laboratory.demos.tictactoe.preferences.UserInfo

/**
 * View model for the Lobby Screen hosted by [LobbyActivity]
 */
class LobbyScreenViewModel(val lobby: Lobby) : ViewModel() {

    private val _players = MutableStateFlow<List<UserInfo>>(emptyList())
    val players = _players.asStateFlow()

    private var lobbyMonitor: Job? = null

    fun enterLobby() {
        if (lobbyMonitor == null) {
            lobbyMonitor = viewModelScope.launch {
                Log.v(TAG, "viewModelScope coroutine")
                lobby.players.collect {
                    Log.v(TAG, "collect block starts")
                    Log.v(TAG, "ViewModel is collecting from the lobby: $it")
                    _players.value = it
                    Log.v(TAG, "collect block ends")
                }
            }
        }
    }

    fun leaveLobby() {
        lobbyMonitor?.cancel()
        lobbyMonitor = null
    }
}

class LobbyScreenViewModelPull(
    private val lobby: LobbyPullStyle
) : ViewModel() {

    private var _players by mutableStateOf<List<UserInfo>>(emptyList())
    val players: List<UserInfo>
        get() = _players

    private var lobbyMonitor: Job? = null

    fun enterLobby() {
        if (lobbyMonitor == null) {
            lobbyMonitor = viewModelScope.launch {
                while (true) {
                    val players = lobby.getPlayers()
                    Log.v(TAG, "ViewModel is pulling from the lobby: $players")
                    _players = players
                    delay(5000)
                }
            }
        }
    }

    fun leaveLobby() {
        lobbyMonitor?.cancel()
        lobbyMonitor = null
    }
}