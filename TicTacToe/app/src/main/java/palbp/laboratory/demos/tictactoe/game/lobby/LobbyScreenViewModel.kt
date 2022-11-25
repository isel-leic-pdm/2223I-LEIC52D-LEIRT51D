package palbp.laboratory.demos.tictactoe.game.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import palbp.laboratory.demos.tictactoe.TAG
import palbp.laboratory.demos.tictactoe.preferences.UserInfoRepository

/**
 * View model for the Lobby Screen hosted by [LobbyActivity]
 */
class LobbyScreenViewModel(
    private val lobby: Lobby,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val players = _players.asStateFlow()

    private var lobbyMonitor: Job? = null

    fun enterLobby() {
        if (lobbyMonitor == null) {
            val localUserInfo = checkNotNull(userInfoRepository.userInfo)
            lobbyMonitor = viewModelScope.launch {
                Log.v(TAG, "Starting view-model scoped coroutine")
                val lobbyFlow = lobby.enterAndObserve(PlayerInfo(localUserInfo))
                lobbyFlow.collect {
                    Log.v(TAG, "collecting element from flow")
                    _players.value = it
                }
                Log.v(TAG, "Ending view-model scoped coroutine")
            }
        }
    }

    fun leaveLobby() {
        lobbyMonitor?.cancel()
        lobbyMonitor = null
    }
}
