package palbp.laboratory.demos.tictactoe.game.lobby

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import palbp.laboratory.demos.tictactoe.TicTacToeTestApplication
import palbp.laboratory.demos.tictactoe.game.lobby.model.Lobby
import palbp.laboratory.demos.tictactoe.game.lobby.model.PlayerInfo
import palbp.laboratory.demos.tictactoe.preferences.model.UserInfo

val otherTestPlayersInLobby: List<PlayerInfo> = buildList {
    repeat(3 ) {
        add(PlayerInfo(UserInfo("remote $it", "moto")))
    }
}

class PopulatedFirebaseLobby : TestRule {

    val app: TicTacToeTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TicTacToeTestApplication
    }

    val lobby: Lobby = LobbyFirebase(app.emulatedFirestoreDb)

    private suspend fun populateLobby() {
        val results = otherTestPlayersInLobby.map {
            app.emulatedFirestoreDb
                .collection(LOBBY)
                .document(it.id.toString())
                .set(it.info.toDocumentContent())
        }
        results.forEach { it.await() }
    }

    private suspend fun emptyLobby() {
        val results = otherTestPlayersInLobby.map {
            app.emulatedFirestoreDb
                .collection(LOBBY)
                .document(it.id.toString())
                .delete()
        }
        results.forEach { it.await() }
    }


    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    populateLobby()
                    test.evaluate()
                }
                finally {
                    emptyLobby()
                }
            }
        }
}
