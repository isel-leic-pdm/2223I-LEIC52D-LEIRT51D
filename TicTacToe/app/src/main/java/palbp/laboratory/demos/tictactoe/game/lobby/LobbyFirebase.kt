package palbp.laboratory.demos.tictactoe.game.lobby

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import palbp.laboratory.demos.tictactoe.game.lobby.model.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfo
import java.util.*

const val LOBBY = "lobby"
private const val NICK_FIELD = "nick"
private const val MOTO_FIELD = "moto"

/**
 * Sum type that characterizes the lobby state
 */
sealed class LobbyState
class InUse(val localPlayerDocRef: DocumentReference): LobbyState()
class InUseWithFlow(val scope: ProducerScope<List<PlayerInfo>>) : LobbyState()
object Idle : LobbyState()

/**
 * Implementation of the Game's lobby using Firebase's Firestore
 */
class LobbyFirebase(private val db: FirebaseFirestore) : Lobby {

    private var state: LobbyState = Idle

    private suspend fun addLocalPlayer(localPlayer: PlayerInfo): DocumentReference {
        val docRef = db.collection(LOBBY).document(localPlayer.id.toString())
        docRef
            .set(localPlayer.info.toDocumentContent())
            .await()
        return docRef
    }

    override suspend fun getPlayers(): List<PlayerInfo> {
        try {
            val result = db.collection(LOBBY).get().await()
            return result.map { it.toPlayerInfo() }
        }
        catch (e: Throwable) {
            throw UnreachableLobbyException()
        }
    }

    override suspend fun enter(localPlayer: PlayerInfo): List<PlayerInfo> {
        check(state == Idle)
        try {
            state = InUse(addLocalPlayer(localPlayer))
            return getPlayers()
        }
        catch (e: Throwable) {
            throw UnreachableLobbyException()
        }
    }

    override fun enterAndObserve(localPlayer: PlayerInfo): Flow<List<PlayerInfo>> {
        check(state == Idle)
        return callbackFlow {
            state = InUseWithFlow(this)
            var localPlayerDocRef: DocumentReference? = null
            var subscription: ListenerRegistration? = null
            try {
                localPlayerDocRef = addLocalPlayer(localPlayer)
                subscription = db.collection(LOBBY).addSnapshotListener { snapshot, error ->
                    when {
                        error != null -> close(error)
                        snapshot != null -> trySend(snapshot.toPlayerList())
                    }
                }
            }
            catch (e: Exception) {
                close(e)
            }

            awaitClose {
                subscription?.remove()
                localPlayerDocRef?.delete()
            }
        }
    }

    override suspend fun leave() {
        when (val currentState = state) {
            is InUseWithFlow -> currentState.scope.close()
            is InUse -> currentState.localPlayerDocRef.delete().await()
            is Idle -> throw IllegalStateException()
        }
        state = Idle
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


fun QuerySnapshot.toPlayerList() = map { it.toPlayerInfo() }

/**
 * [UserInfo] extension function used to convert an instance to a map of key-value
 * pairs containing the object's properties, to be used as a payload of
 */
fun UserInfo.toDocumentContent() = mapOf(
    NICK_FIELD to nick,
    MOTO_FIELD to moto
)

class UnreachableLobbyException : Exception()