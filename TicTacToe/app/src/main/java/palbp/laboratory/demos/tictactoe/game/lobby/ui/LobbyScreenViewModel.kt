package palbp.laboratory.demos.tictactoe.game.lobby.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import palbp.laboratory.demos.tictactoe.game.lobby.model.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfoRepository

/**
 * View model for the Lobby Screen hosted by [LobbyActivity]
 */
class LobbyScreenViewModel(
    val lobby: Lobby,
    val userInfoRepo: UserInfoRepository
) : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val players = _players.asStateFlow()

    private var lobbyMonitor: Job? = null

    fun enterLobby(): Job? =
        if (lobbyMonitor == null) {
            val localPlayer = PlayerInfo(checkNotNull(userInfoRepo.userInfo))
            lobbyMonitor = viewModelScope.launch {
                lobby.enterAndObserve(localPlayer).collect { currentPlayers ->
                    val otherPlayers = currentPlayers.filter {
                        it != localPlayer
                    }
                    _players.value = otherPlayers
                }
            }
            lobbyMonitor
        } else null

    fun leaveLobby(): Job? = if (lobbyMonitor != null) {
        viewModelScope.launch {
            lobbyMonitor?.cancel()
            lobbyMonitor = null
            lobby.leave()
        }
    } else null
}
