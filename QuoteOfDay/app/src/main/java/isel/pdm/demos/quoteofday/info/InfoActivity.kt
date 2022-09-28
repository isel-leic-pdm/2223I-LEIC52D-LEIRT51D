package isel.pdm.demos.quoteofday.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InfoScreen(onBackRequested = { finish() })
        }
    }
}