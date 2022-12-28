package palbp.laboratory.demos.tictactoe

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import palbp.laboratory.demos.tictactoe.game.lobby.domain.Challenge
import palbp.laboratory.demos.tictactoe.game.lobby.domain.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.domain.PlayerInfo
import palbp.laboratory.demos.tictactoe.game.lobby.domain.RosterUpdated
import palbp.laboratory.demos.tictactoe.game.play.domain.*
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfo
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfoRepository

val localTestPlayer = PlayerInfo(UserInfo("local"))

val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3) {
        add(PlayerInfo(UserInfo("remote $it", "moto")))
    }
}

/**
 * The service locator to be used in the instrumented tests.
 */
class TicTacToeTestApplication : DependenciesContainer, Application() {

    override var userInfoRepo: UserInfoRepository =
        mockk(relaxed = true) {
            every { userInfo } returns localTestPlayer.info
        }

    override var lobby: Lobby =
        mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            every { enterAndObserve(capture(localPlayer)) } returns flow {
                emit(RosterUpdated(
                    buildList {
                        add(localPlayer.captured)
                        addAll(otherTestPlayersInLobby)
                    }
                ))
            }

            coEvery { enter(capture(localPlayer)) } answers {
                buildList {
                    add(localPlayer.captured)
                    addAll(otherTestPlayersInLobby)
                }
            }

            val opponent = slot<PlayerInfo>()
            coEvery { issueChallenge(capture(opponent)) } answers {
                Challenge(
                    challenger = localPlayer.captured,
                    challenged = opponent.captured
                )
            }

            coEvery { leave() } returns Unit
        }

    override var match: Match =
        mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            val challenge = slot<Challenge>()
            coEvery { start(capture(localPlayer), capture(challenge)) } answers {
                flow {
                    val localMarker = getLocalPlayerMarker(localPlayer.captured, challenge.captured)
                    emit(GameStarted(Game(localPlayerMarker = localMarker, board = Board())))
                }
            }
        }

    val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }
    }
}

@Suppress("unused")
class TicTacToeTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TicTacToeTestApplication::class.java.name, context)
    }
}
