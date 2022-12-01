package palbp.laboratory.demos.tictactoe.preferences.model

/**
 * Represents the user information.
 *
 * @property [nick] the user's nick name
 * @property [moto] the user's moto, if he has one
 */
data class UserInfo(val nick: String, val moto: String? = null) {
    init {
        require(validateUserInfoParts(nick, moto))
    }
}

/**
 * Returns a [UserInfo] instance with the received values or null, if those
 * values are invalid.
 */
fun userInfoOrNull(nick: String, moto: String?): UserInfo? =
    if (validateUserInfoParts(nick, moto))
        UserInfo(nick, moto)
    else
        null

/**
 * Checks whether the received values are acceptable as [UserInfo]
 * instance fields.
 */
fun validateUserInfoParts(nick: String, moto: String?) =
    nick.isNotBlank() && moto?.isNotBlank() ?: true
