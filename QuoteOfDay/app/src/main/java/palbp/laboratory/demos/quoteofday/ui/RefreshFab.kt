package palbp.laboratory.demos.quoteofday.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class RefreshingState { Idle, Refreshing }

@Composable
fun RefreshButton(onClick: () -> Unit, state: RefreshingState = RefreshingState.Idle) {
    val minSize = 64.dp
    if (state == RefreshingState.Refreshing) {
        CircularProgressIndicator(
            Modifier.defaultMinSize(
                minWidth = minSize - 8.dp,
                minHeight = minSize - 8.dp
            )
        )
    }
    else {
        Button(
            onClick = onClick,
            enabled = state == RefreshingState.Idle,
            shape = CircleShape,
            modifier = Modifier
                .defaultMinSize(minWidth = minSize, minHeight = minSize)
                .testTag("RefreshButton"),
        ){
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Localized description",
            )
        }
    }
}

@Composable
fun RefreshFab(onClick: () -> Unit, state: RefreshingState = RefreshingState.Idle) {
    Button(
        onClick = onClick,
        enabled = state == RefreshingState.Idle,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 64.dp)
            .testTag("RefreshFab"),
    ){
        if (state == RefreshingState.Refreshing) {
            val transition = rememberInfiniteTransition()
            val rotation by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        delayMillis = 50,
                        durationMillis = 750,
                        easing = FastOutSlowInEasing
                    )
                )
            )
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Localized description",
                modifier = Modifier.rotate(rotation)
            )
        }
        else {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Localized description",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdleRefreshButtonPreview() {
    RefreshButton(onClick = { }, state = RefreshingState.Idle)
}

@Preview(showBackground = true)
@Composable
private fun RefreshingRefreshButtonPreview() {
    RefreshButton(onClick = { }, state = RefreshingState.Refreshing)
}

@Preview(showBackground = true)
@Composable
private fun IdleRefreshFabPreview() {
    RefreshFab(onClick = { }, state = RefreshingState.Idle)
}

@Preview(showBackground = true)
@Composable
private fun RefreshingRefreshFabPreview() {
    RefreshFab(onClick = { }, state = RefreshingState.Refreshing)
}

@Preview(showBackground = true)
@Composable
private fun RefreshFabPreview() {
    var refreshing by remember { mutableStateOf(RefreshingState.Idle) }
    LaunchedEffect(key1 = refreshing, block = {
        if (refreshing == RefreshingState.Refreshing) {
            delay(3000)
            refreshing = RefreshingState.Idle
        }
    })
    RefreshFab(
        onClick = {
            refreshing =
                if (refreshing == RefreshingState.Idle) RefreshingState.Refreshing
                else RefreshingState.Idle
        },
        state = refreshing
    )
}
