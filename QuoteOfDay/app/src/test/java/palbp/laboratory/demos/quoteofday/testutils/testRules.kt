@file:Suppress("unused")

package palbp.laboratory.demos.quoteofday.testutils

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class MockWebServerRule : TestRule {

    val server: MockWebServer
        get() = _server

    private lateinit var _server: MockWebServer

    override fun apply(test: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                MockWebServer().use {
                    _server = it
                    it.start()
                    test.evaluate()
                }
            }
        }
}