package com.azzam.protectmyfiles.presentation.savedPasswords

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.presentation.ui.theme.Biscay
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleLarge
import com.azzam.protectmyfiles.util.extensions.getValueOrNoData

@Composable
fun SavedPasswordItem(
    passwordModel: PasswordModel?,
    modifier: Modifier = Modifier,
    action: (PasswordModel?) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = Biscay,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                action.invoke(passwordModel)
            }
            .padding(15.dp)
    ) {
        Text(
            text = getValueOrNoData(value = passwordModel?.name),
            color = White,
            style = titleLarge.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = getValueOrNoData(value = passwordModel?.username),
            color = White,
            style = titleLarge,
        )
    }
}