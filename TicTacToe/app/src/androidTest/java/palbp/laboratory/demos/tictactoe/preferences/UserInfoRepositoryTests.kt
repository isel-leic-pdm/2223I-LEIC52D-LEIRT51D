package palbp.laboratory.demos.tictactoe.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import palbp.laboratory.demos.tictactoe.preferences.domain.UserInfo

@RunWith(AndroidJUnit4::class)
class UserInfoRepositoryTests {

    private val repo by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        UserInfoRepositorySharedPrefs(context)
    }

    @Test
    fun setting_to_null_clears_userInfo() {
        // Arrange
        repo.userInfo = UserInfo("nick", "moto")
        assertNotNull(repo.userInfo)

        // Act
        repo.userInfo = null

        // Assert
        assertNull(repo.userInfo)
    }
}