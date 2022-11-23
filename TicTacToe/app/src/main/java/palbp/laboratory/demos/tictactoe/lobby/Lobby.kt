package palbp.laboratory.demos.tictactoe.lobby

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import palbp.laboratory.demos.tictactoe.preferences.UserInfo
import java.util.*

data class PlayerInfo(val info: UserInfo, val id: UUID = UUID.randomUUID())

interface Lobby {
    suspend fun getPlayers(): List<PlayerInfo>
    fun enter(player: PlayerInfo): Flow<List<PlayerInfo>>
    fun leave()
}

class FakeLobby : Lobby {
    private var count = 1

    private fun getCurrentListOfPlayers(): List<PlayerInfo> {
        val list = buildList {
            repeat(5) {
                add(
                    PlayerInfo(
                        UserInfo("Nick $it", "$count: $it moto")
                    )
                )
            }
        }
        count += 1
        return list
    }

    override suspend fun getPlayers(): List<PlayerInfo> = getCurrentListOfPlayers()

    override fun enter(player: PlayerInfo): Flow<List<PlayerInfo>> {
        return flow {
            while(true) {
                delay(5000)
                emit(getCurrentListOfPlayers())
            }
        }
    }

    override fun leave() { }
}