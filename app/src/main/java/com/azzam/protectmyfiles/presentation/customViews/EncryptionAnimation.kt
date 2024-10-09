package com.azzam.protectmyfiles.presentation.customViews

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.azzam.protectmyfiles.R

@Composable
fun EncryptionAnimation(
    modifier: Modifier = Modifier
) {

    var isPlayed by rememberSaveable {
        mutableStateOf(false)
    }

    val progressAnimation by animateIntAsState(
        targetValue = if (isPlayed) 3 else 0,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 800,
            )
        ),
        label = "",
    )

    LaunchedEffect(key1 = true) {
        isPlayed = true
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(listOfAnimation[progressAnimation]),
            contentDescription = null,
            modifier = modifier
                .size(200.dp)
        )
    }

}

val listOfAnimation = listOf(
    R.drawable.ic_anim_1,
    R.drawable.ic_anim_2,
    R.drawable.ic_anim_3,
    R.drawable.ic_anim_3,
)