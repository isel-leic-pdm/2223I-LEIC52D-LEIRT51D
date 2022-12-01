package palbp.laboratory.demos.tictactoe.game.lobby.model

import palbp.laboratory.demos.tictactoe.preferences.model.UserInfo
import java.util.*

/**
 * Data type that characterizes the player information while he's in the lobby
 * @property info   The information entered by the user
 * @property id     An identifier used to distinguish players in the lobby
 */
data class PlayerInfo(val info: UserInfo, val id: UUID = UUID.randomUUID())