package isel.pdm.demos.quoteofday.info

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.pdm.demos.quoteofday.ui.TopBar
import isel.pdm.demos.quoteofday.ui.theme.QuoteOfDayTheme
import isel.pdm.demos.quoteofday.R

data class SocialInfo(val link: Uri, @DrawableRes val imageId: Int)

@Composable
fun InfoScreen(
    onBackRequested: () -> Unit = { },
    onSendEmailRequested: () -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { },
    socials: Iterable<SocialInfo> = emptyList()
) {
    QuoteOfDayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onBackRequested = { onBackRequested() }) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Author(onSendEmailRequested = onSendEmailRequested)
                Socials(
                    socials = socials,
                    onOpenUrlRequested = onOpenUrlRequested
                )
            }
        }
    }
}

@Composable
fun Author(onSendEmailRequested: () -> Unit = { }) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSendEmailRequested() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_author),
            contentDescription = null
        )
        Text(text = "Paulo Pereira", style = MaterialTheme.typography.h5)
        Icon(imageVector = Icons.Default.Email, contentDescription = null)
    }
}

@Composable
fun Socials(
    onOpenUrlRequested: (Uri) -> Unit = { },
    socials: Iterable<SocialInfo>
) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        socials.forEach {
            Social(id = it.imageId, onClick = { onOpenUrlRequested(it.link) })
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun Social(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}

/*
@Preview
@Composable
fun InfoScreenPreview() {
    val socialsPreview = listOf(
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
    InfoScreen(socials = socialsPreview)
}*/