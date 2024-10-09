package com.azzam.protectmyfiles.presentation.customViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.azzam.protectmyfiles.R

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    image: Any? = null,
    contentDescription: String? = "",
    size: Dp = 150.dp,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    SubcomposeAsyncImage(
        model = image,
        contentDescription = contentDescription,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(size.div(3))
                )
            }
        },
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            )
            .size(size)
    )
}