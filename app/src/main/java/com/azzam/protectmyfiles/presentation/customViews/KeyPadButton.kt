package com.azzam.protectmyfiles.presentation.customViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.util.HexagonShape

@Composable
fun KeyPadButton(
    title: String,
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: (title: String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .clip(HexagonShape())
            .clickable {
                onClick.invoke(title)
            }
    ) {
        val (btnBg, tvText) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_keypad_btn),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .constrainAs(btnBg) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = title,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(tvText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}