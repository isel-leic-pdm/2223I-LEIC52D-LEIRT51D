package palbp.laboratory.demos.tictactoe

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TicTacToeTestApplicationTests {

    @Test
    fun instrumented_tests_use_application_test_context() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("palbp.laboratory.demos.tictactoe", context.packageName)
        assertTrue(
            "Make sure the tests runner is correctly configured in build.gradle\n" +
                    "defaultConfig { testInstrumentationRunner <test runner class name> }",
            context.applicationContext is TicTacToeTestApplication
        )
    }

    @Test
    fun application_context_contains_dependencies() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(context.applicationContext is DependenciesContainer)
    }
}