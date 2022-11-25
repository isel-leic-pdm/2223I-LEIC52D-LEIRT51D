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
import kotlinx.coroutines.flow.flow
import palbp.laboratory.demos.tictactoe.game.lobby.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.UserInfo
import palbp.laboratory.demos.tictactoe.preferences.UserInfoRepository

/**
 * The service locator to be used in the instrumented tests.
 */
class TicTacToeTestApplication : DependenciesContainer, Application() {
    override var userInfoRepo: UserInfoRepository =
        mockk {
            every { userInfo } returns UserInfo("nick", "moto")
        }

    override val lobby: Lobby
        get() = mockk(relaxed = true) {
            val localPlayer = PlayerInfo(UserInfo("test", "test moto"))
            coEvery { enterAndObserve(localPlayer) } returns flow {
                listOf(
                    localPlayer,
                    PlayerInfo(UserInfo("nick1", "moto1")),
                    PlayerInfo(UserInfo("nick2", "moto2"))
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
