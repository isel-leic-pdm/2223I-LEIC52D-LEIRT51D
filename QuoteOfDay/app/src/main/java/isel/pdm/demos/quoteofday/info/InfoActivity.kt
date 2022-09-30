package isel.pdm.demos.quoteofday.info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.pdm.demos.quoteofday.R
import isel.pdm.demos.quoteofday.TAG

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InfoScreen(
                onBackRequested = { finish() },
                onOpenUrlRequested = { uri -> openURL(uri) },
                onSendEmailRequested = { openSendEmail() },
                socials = socialLinks
            )
        }
    }

    private fun openURL(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openSendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(authorEmail))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }

            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}

private val socialLinks = listOf(
    SocialInfo(
        link = Uri.parse("https://www.linkedin.com/in/palbp/"),
        imageId = R.drawable.ic_linkedin
    ),
    SocialInfo(
        link = Uri.parse("https://www.twitch.tv/paulo_pereira"),
        imageId = R.drawable.ic_twitch
    ),
    SocialInfo(
        link = Uri.parse("https://www.youtube.com/channel/UCetmdF6qGnMAdZP32i8AnbA"),
        imageId = R.drawable.ic_youtube
    )
)

private const val authorEmail = "paulo.pereira@isel.pt"
private const val emailSubject = "About the QuoteOfDay App"
