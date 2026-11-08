package com.gallerywall.spacer.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallerywall.spacer.R
import com.gallerywall.spacer.core.di.AppViewModelFactory
import com.gallerywall.spacer.core.theme.CoralAccent
import com.gallerywall.spacer.core.theme.InkDeep
import com.gallerywall.spacer.core.theme.InkPrimary
import com.gallerywall.spacer.core.theme.PlasterBg
import com.gallerywall.spacer.core.theme.SurfaceMuted

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
) {
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }
    SplashContent()
}

@Composable
private fun SplashContent() {
    val entry = remember { MutableTransitionState(false).apply { targetState = true } }

    val transition = rememberInfiniteTransition(label = "splashLoop")
    val ghostOne by transition.animateFloat(
        initialValue = 0.14f,
        targetValue = 0.30f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse),
        label = "ghostOne"
    )
    val ghostTwo by transition.animateFloat(
        initialValue = 0.30f,
        targetValue = 0.14f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse),
        label = "ghostTwo"
    )
    val ghostThree by transition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse),
        label = "ghostThree"
    )
    val plumb by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "plumb"
    )
    val bracket by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse),
        label = "bracket"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(InkPrimary.copy(alpha = 0.94f), InkDeep)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GhostFrame(width = 96.dp, height = 72.dp, frameAlpha = ghostOne)
                GhostFrame(width = 62.dp, height = 84.dp, frameAlpha = ghostTwo)
                GhostFrame(width = 76.dp, height = 76.dp, frameAlpha = ghostThree)
            }

            Spacer(Modifier.height(30.dp))

            AnimatedVisibility(
                visibleState = entry,
                enter = fadeIn(tween(durationMillis = 700, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = 0.88f,
                        animationSpec = spring(dampingRatio = 0.72f, stiffness = 220f)
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(140.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(26.dp))

            AnimatedVisibility(
                visibleState = entry,
                enter = fadeIn(tween(durationMillis = 700, delayMillis = 260)) +
                    slideInVertically(tween(durationMillis = 700, delayMillis = 260)) { it / 3 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BracketMark()
                        Spacer(Modifier.width(10.dp + bracket.dp))
                        Text(
                            text = stringResource(R.string.splash_title),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Light,
                            fontSize = 28.sp,
                            lineHeight = 34.sp,
                            letterSpacing = 3.5.sp,
                            color = PlasterBg,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.width(10.dp + bracket.dp))
                        BracketMark()
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.splash_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.2.sp,
                        color = SurfaceMuted.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(148.dp)
                    .height(2.dp),
                color = CoralAccent,
                trackColor = SurfaceMuted.copy(alpha = 0.22f)
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_caption),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                letterSpacing = 1.8.sp,
                color = SurfaceMuted.copy(alpha = 0.6f)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(110.dp)
                .padding(horizontal = 24.dp)
        ) {
            val lineY = size.height * 0.5f
            val step = 24.dp.toPx()
            drawLine(
                color = SurfaceMuted.copy(alpha = 0.24f),
                start = Offset(0f, lineY),
                end = Offset(size.width, lineY),
                strokeWidth = 1.dp.toPx()
            )
            var tick = 0f
            while (tick <= size.width) {
                val centre = tick > size.width / 2f - step / 2f && tick < size.width / 2f + step / 2f
                drawLine(
                    color = if (centre) CoralAccent else SurfaceMuted.copy(alpha = 0.24f),
                    start = Offset(tick, lineY),
                    end = Offset(tick, lineY - if (centre) 14.dp.toPx() else 8.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
                tick += step
            }
            drawCircle(
                color = CoralAccent,
                radius = 5.dp.toPx(),
                center = Offset(plumb * size.width, lineY)
            )
        }
    }
}

@Composable
private fun GhostFrame(
    width: Dp,
    height: Dp,
    frameAlpha: Float
) {
    Box(
        modifier = Modifier
            .size(width, height)
            .border(
                width = 1.dp,
                color = SurfaceMuted.copy(alpha = frameAlpha),
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun BracketMark() {
    Box(
        modifier = Modifier
            .size(2.dp, 26.dp)
            .background(CoralAccent.copy(alpha = 0.7f))
    )
}
