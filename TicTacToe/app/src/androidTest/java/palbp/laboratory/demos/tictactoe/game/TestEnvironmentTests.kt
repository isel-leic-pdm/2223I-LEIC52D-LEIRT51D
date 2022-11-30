@file:OptIn(ExperimentalCoroutinesApi::class)

package palbp.laboratory.demos.tictactoe.game

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import palbp.laboratory.demos.tictactoe.TicTacToeTestApplication

class TestEnvironmentTests {

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TicTacToeTestApplication
    }

    @Test
    fun firestore_emulator_is_running(): Unit = runTest {
        val testDocPath = "test/id"
        val actJob = launch {
            app.emulatedFirestoreDb
                .document(testDocPath)
                .set(hashMapOf("property" to "value"))
                .await()
        }
        actJob.join()
        Assert.assertTrue(actJob.isCompleted)

        // Test succeeded. Let's clean up
        app.emulatedFirestoreDb.document(testDocPath).delete().await()
    }
}