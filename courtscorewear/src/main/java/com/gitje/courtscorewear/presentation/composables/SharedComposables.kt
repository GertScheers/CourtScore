package com.gitje.courtscorewear.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.gitje.courtscorewear.R
import kotlinx.coroutines.launch

@Composable
fun GameFinishedScreen(wonTeam: Int, backToStart: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Team ${if (wonTeam == 1) "1" else "2"} wins!", fontSize = 26.sp)
        Text(if (wonTeam == 1) "Better luck next time!" else "Congratulations!")
        Button(onClick = { backToStart() }) { Text("Return") }
    }
}

@Composable
fun ServerPickerScreen(setServer: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Who will start?")
        Button(
            onClick = { setServer(1) },
            modifier = Modifier.fillMaxWidth(0.6f),
        ) { Text("Opponent") }
        Button(
            onClick = { setServer(2) },
            modifier = Modifier.fillMaxWidth(0.6f),
        ) { Text("You") }
    }
}


@Composable
fun AnimatedScoreText(
    score: String,
    popTrigger: Int,
    rootCenter: Offset,
    modifier: Modifier = Modifier,
    fontSize: TextUnit? = null,
) {
    val elemCenter = remember { mutableStateOf(Offset.Zero) }
    val transXAnim = remember { Animatable(0f) }
    val transYAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(1f) }

    LaunchedEffect(popTrigger) {
        if (popTrigger <= 0) return@LaunchedEffect

        val dx = rootCenter.x - elemCenter.value.x
        val dy = rootCenter.y - elemCenter.value.y

        scaleAnim.snapTo(10f)
        transXAnim.snapTo(dx)
        transYAnim.snapTo(dy)

        val duration = 600
        launch {
            scaleAnim.animateTo(
                1f,
                tween(durationMillis = duration, easing = FastOutSlowInEasing),
            )
        }
        launch {
            transXAnim.animateTo(
                0f,
                tween(durationMillis = duration, easing = FastOutSlowInEasing),
            )
        }
        launch {
            transYAnim.animateTo(
                0f,
                tween(durationMillis = duration, easing = FastOutSlowInEasing),
            )
        }
    }

    Text(
        text = score,
        modifier =
            modifier
                .onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInWindow()
                    elemCenter.value = Offset(pos.x + coordinates.size.width / 2f, pos.y + coordinates.size.height / 2f)
                }.graphicsLayer {
                    translationX = transXAnim.value
                    translationY = transYAnim.value
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                },
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = fontSize ?: TextUnit.Unspecified
    )
}


@Composable
fun HeartRateDisplay(
    heartRate: Double,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Red
        )
        Spacer(Modifier.width(8.dp))
        Text("${heartRate.toInt()}")
    }
}