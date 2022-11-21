package palbp.laboratory.demos.tictactoe.lobby

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import palbp.laboratory.demos.tictactoe.TAG
import palbp.laboratory.demos.tictactoe.preferences.UserInfo

interface Lobby {
    val players: Flow<List<UserInfo>>
}


interface LobbyPullStyle {
    suspend fun getPlayers(): List<UserInfo>
}

class FakeLobby : LobbyPullStyle, Lobby {
    private var count = 1

    private fun getCurrentListOfPlayers(): List<UserInfo> {
        val list = buildList {
            repeat(5) {
                add(UserInfo("My Nick $it", "$count This is my $it moto"))
            }
        }
        count += 1
        return list
    }

    override suspend fun getPlayers(): List<UserInfo> = getCurrentListOfPlayers()

    override val players: Flow<List<UserInfo>>
        get() = flow {
            Log.v(TAG, "flow block")

            Log.v(TAG, "while(true): STARTS")
            while(true) {
                delay(5000)
                Log.v(TAG, "Lobby is emitting to the flow the version $count of the players' list")
                Log.v(TAG, "before emit")
                emit(getCurrentListOfPlayers())
                Log.v(TAG, "after emit")
            }
        }
}