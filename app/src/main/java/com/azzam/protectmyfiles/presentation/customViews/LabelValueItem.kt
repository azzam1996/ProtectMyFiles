package com.azzam.protectmyfiles.presentation.customViews

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.azzam.protectmyfiles.util.extensions.getValueOrNoData

@Composable
fun LabelValueItem(label: String?, value: String?) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("$label : ")
            }

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(getValueOrNoData(value = value))
            }

        },
        color = Color.White,
        modifier = Modifier
            .padding(bottom = 5.dp)
    )
}