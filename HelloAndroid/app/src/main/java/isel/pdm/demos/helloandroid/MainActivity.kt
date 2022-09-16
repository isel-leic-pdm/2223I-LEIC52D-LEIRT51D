package isel.pdm.demos.helloandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import isel.pdm.demos.helloandroid.ui.theme.HelloAndroidTheme

const val TAG = "HelloAndroid"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate starts on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
        setContent {
            HelloAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
        Log.v(TAG, "onCreate ends on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")

    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy on instance = " +
                "${hashCode()} and thread = ${Thread.currentThread().name}")

    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "SLB, O MAIOR !!!",
        fontSize = MaterialTheme.typography.h1.fontSize
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloAndroidTheme {
        Greeting("Android")
    }
}