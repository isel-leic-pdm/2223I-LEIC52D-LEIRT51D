package palbp.laboratory.demos.tictactoe.game.lobby

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.Flow
import palbp.laboratory.demos.tictactoe.preferences.UserInfo
import java.util.*

const val LOBBY = "lobby"
private const val NICK_FIELD = "nick"
private const val MOTO_FIELD = "moto"


class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {
    override suspend fun getPlayers(): List<PlayerInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        TODO("Not yet implemented")
    }

    override fun enterAndObserve(localPlayer: PlayerInfo): Flow<List<PlayerInfo>> {
        TODO("Not yet implemented")
    }

    override fun leave() {
        TODO("Not yet implemented")
    }
}

/**
 * Extension function used to convert player info documents stored in the Firestore DB
 * into [PlayerInfo] instances.
 */
fun QueryDocumentSnapshot.toPlayerInfo() =
    PlayerInfo(
        info = UserInfo(
            nick = data[NICK_FIELD] as String,
            moto = data[MOTO_FIELD] as String?
        ),
        id = UUID.fromString(id),
    )

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties, to be used as a payload of
 */
fun UserInfo.toDocumentContent() = mapOf(
    NICK_FIELD to nick,
    MOTO_FIELD to moto
)

class UnreachableLobbyException : Exception()