package palbp.laboratory.demos.tictactoe

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import palbp.laboratory.demos.tictactoe.game.lobby.model.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfo
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfoRepository

val localTestPlayer = PlayerInfo(UserInfo("local"))

/**
 * The service locator to be used in the instrumented tests.
 */
class TicTacToeTestApplication : DependenciesContainer, Application() {

    override var userInfoRepo: UserInfoRepository =
        mockk(relaxed = true) {
            every { userInfo } returns localTestPlayer.info
        }

    override val lobby: Lobby
        get() = mockk(relaxed = true) {
            val localPlayer = slot<PlayerInfo>()
            every { enterAndObserve(capture(localPlayer)) } returns flow {
                emit(
                    listOf(
                        localPlayer.captured,
                        PlayerInfo(UserInfo("nick1", "moto1")),
                        PlayerInfo(UserInfo("nick2", "moto2"))
                    )
                )
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
