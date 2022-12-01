package palbp.laboratory.demos.tictactoe.game.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import palbp.laboratory.demos.tictactoe.game.play.model.Board
import palbp.laboratory.demos.tictactoe.game.play.model.Game
import palbp.laboratory.demos.tictactoe.game.play.model.Marker

/**
 * Hosts the screen where the game is played.
 */
class GameActivity: ComponentActivity() {
    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val game = Game(Marker.CROSS, Board())
            GameScreen(state = GameScreenState(game))
        }
    }
}
