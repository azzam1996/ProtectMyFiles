package com.azzam.protectmyfiles.presentation.home

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.CustomAsyncImage
import com.azzam.protectmyfiles.presentation.customViews.EncryptionAnimation
import com.azzam.protectmyfiles.presentation.customViews.LabelValueItem
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.presentation.ui.theme.BlackRock
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleMedium
import com.azzam.protectmyfiles.util.TYPE_ANY_FILE
import com.azzam.protectmyfiles.util.UIEvent
import com.azzam.protectmyfiles.util.extensions.ArrangementLazyColumnLastItemToBottom
import com.azzam.protectmyfiles.util.extensions.dashedBorder
import com.azzam.protectmyfiles.util.extensions.observeAsEvents

@Composable
fun EncryptionScreen(
    appState: AppState,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    observeAsEvents(flow = homeViewModel.showMessage) { event ->
        when (event) {
            is UIEvent.ShowStringMessage -> {
                Toast.makeText(
                    appState.context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            is UIEvent.ShowStringResource -> {
                Toast.makeText(
                    appState.context,
                    appState.context.getString(event.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(key1 = true) {
        appState.showBottomBarBySettingValueFromScreen = true
        appState.selectedBottomNavItemRoute = Screens.HomeScreen.route
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (homeViewModel.isEncryptionRunning) {
            EncryptionAnimation()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                verticalArrangement = ArrangementLazyColumnLastItemToBottom,
            ) {
                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        homeViewModel.handleActivityResult(appState.context, it.data?.data)
                    }

                Spacer(modifier = Modifier.height(60.dp))

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .dashedBorder(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = TYPE_ANY_FILE
                            }

                            launcher.launch(intent)
                        }

                ) {
                    val (ivKey, tvMessage) = createRefs()

                    Image(
                        painter = painterResource(id = R.drawable.ic_key),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier
                            .size(90.dp)
                            .constrainAs(ivKey) {
                                top.linkTo(parent.top, 10.dp)
                                start.linkTo(parent.start, 10.dp)
                                end.linkTo(parent.end, 10.dp)
                            }
                    )

                    Text(
                        text = stringResource(id = R.string.please_select_file_to_encrypt),
                        style = titleMedium,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .constrainAs(tvMessage) {
                                top.linkTo(ivKey.bottom, 20.dp)
                                start.linkTo(parent.start, 10.dp)
                                end.linkTo(parent.end, 10.dp)
                                bottom.linkTo(parent.bottom, 20.dp)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                if (homeViewModel.fileMetaData != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(10.dp)
                    ) {

                        Spacer(modifier = Modifier.height(20.dp))

                        CustomAsyncImage(
                            image = homeViewModel.image ?: R.drawable.placeholder,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        LabelValueItem(
                            label = "File Name",
                            value = homeViewModel.fileMetaData?.fullFileName
                        )
                        LabelValueItem(
                            label = "File Size",
                            value = homeViewModel.fileMetaData?.fileSize()
                        )
                        LabelValueItem(
                            label = "File Type",
                            value = stringResource(
                                id = homeViewModel.fileMetaData?.fileType?.type ?: R.string.document
                            )
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = BlackRock.copy(alpha = 0.8f),
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            homeViewModel.fileMetaData?.let {
                                homeViewModel.insertEncryptedDto(appState.context, it)
                            }
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
                            text = stringResource(id = R.string.encrypt),
                            style = titleMedium.copy(color = White),
                        )
                    }
                }
            }
        }
    }
}