package palbp.laboratory.demos.tictactoe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.runner.AndroidJUnitRunner
import io.mockk.every
import io.mockk.mockk
import palbp.laboratory.demos.tictactoe.lobby.Lobby
import palbp.laboratory.demos.tictactoe.lobby.LobbyPullStyle
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

    override val lobbyPull: LobbyPullStyle
        get() = TODO("Not yet implemented")
    override val lobby: Lobby
        get() = TODO("Not yet implemented")
}

@Suppress("unused")
class TicTacToeTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TicTacToeTestApplication::class.java.name, context)
    }
}
