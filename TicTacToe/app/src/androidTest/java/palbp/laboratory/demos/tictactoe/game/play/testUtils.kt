package palbp.laboratory.demos.tictactoe.game.play

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import palbp.laboratory.demos.tictactoe.TicTacToeTestApplication
import palbp.laboratory.demos.tictactoe.game.lobby.domain.Challenge
import palbp.laboratory.demos.tictactoe.game.play.adapters.MatchFirebase
import palbp.laboratory.demos.tictactoe.game.play.adapters.ONGOING
import palbp.laboratory.demos.tictactoe.game.play.domain.Match
import palbp.laboratory.demos.tictactoe.localTestPlayer
import palbp.laboratory.demos.tictactoe.otherTestPlayersInLobby

class CleanupMatchesRule : TestRule {

    val app: TicTacToeTestApplication by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TicTacToeTestApplication
    }

    val remoteInitiatedChallenge = Challenge(
        challenger = otherTestPlayersInLobby.first(),
        challenged = localTestPlayer
    )

    val locallyInitiatedChallenge = Challenge(
        challenger = localTestPlayer,
        challenged = otherTestPlayersInLobby.first(),
    )

    val match: Match = MatchFirebase(app.emulatedFirestoreDb)

    private suspend fun cleanup() {
        app.emulatedFirestoreDb
            .collection(ONGOING)
            .get()
            .await()
            .map { it.id }
            .forEach {
                app.emulatedFirestoreDb
                    .collection(ONGOING)
                    .document(it)
                    .delete()
                    .await()
            }
    }

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() = runBlocking {
                try {
                    test.evaluate()
                }
                finally {
                    cleanup()
                }
            }
        }
}
