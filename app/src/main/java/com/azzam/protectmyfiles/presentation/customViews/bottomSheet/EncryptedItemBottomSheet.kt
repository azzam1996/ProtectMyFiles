package com.azzam.protectmyfiles.presentation.customViews.bottomSheet

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.CustomAsyncImage
import com.azzam.protectmyfiles.presentation.customViews.LabelValueItem
import com.azzam.protectmyfiles.presentation.ui.theme.Arapawa
import com.azzam.protectmyfiles.presentation.ui.theme.BlackRock
import com.azzam.protectmyfiles.presentation.ui.theme.Red
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleMedium

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptedItemBottomSheet(
    appState: AppState,
    encryptedModel: EncryptedModel?,
    modifier: Modifier = Modifier,
    onDecryptClick: (EncryptedModel?) -> Unit,
    onDeleteClick: (EncryptedModel?) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = {
            onDismiss.invoke()
        },
        containerColor = Arapawa,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            CustomAsyncImage(
                image = encryptedModel?.fileType?.image,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            LabelValueItem(label = "File Name", value = encryptedModel?.name)
            LabelValueItem(
                label = "File Size",
                value = String.format("%3.2f MB.", (encryptedModel?.fileSize ?: 0) / (1024f * 1024f))
            )
            LabelValueItem(
                label = "File Type",
                value = stringResource(id = encryptedModel?.fileType?.type ?: R.string.document)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = BlackRock.copy(alpha = 0.8f),
                    containerColor = Color.Transparent
                ),
                onClick = {
                    onDecryptClick.invoke(encryptedModel)
                    onDismiss.invoke()
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .heightIn(52.dp, 200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = BlackRock.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )

            ) {
                Text(
                    text = stringResource(id = R.string.decrypt),
                    style = titleMedium.copy(color = White),
                )
            }

            Button(
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Red,
                    containerColor = Color.Transparent
                ),
                onClick = {
                    onDeleteClick.invoke(encryptedModel)
                    onDismiss.invoke()
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .heightIn(52.dp, 200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Red,
                        shape = RoundedCornerShape(12.dp)
                    )

            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    style = titleMedium.copy(color = White),
                )
            }
        }
    }
}