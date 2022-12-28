package palbp.laboratory.demos.tictactoe.preferences

import android.content.Context
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfo
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfoRepository

/**
 * A user information repository implementation supported in shared preferences
 */
class UserInfoRepositorySharedPrefs(private val context: Context): UserInfoRepository {

    private val userNickKey = "Nick"
    private val userMotoKey = "Moto"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    override var userInfo: UserInfo?
        get() {
            val savedNick = prefs.getString(userNickKey, null)
            return if (savedNick != null)
                UserInfo(savedNick, prefs.getString(userMotoKey, null))
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userNickKey)
                    .remove(userMotoKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userNickKey, value.nick)
                    .putString(userMotoKey, value.moto)
                    .apply()
        }
}