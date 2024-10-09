package com.azzam.protectmyfiles.presentation.encryptedFileList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.EncryptedModel
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.EncryptionAnimation
import com.azzam.protectmyfiles.presentation.customViews.bottomSheet.EncryptedItemBottomSheet
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleLarge
import com.azzam.protectmyfiles.util.extensions.observeAsEvents

@Composable
fun EncryptedFileListScreen(
    appState: AppState,
    viewModel: EncryptedFileListViewModel,
    modifier: Modifier = Modifier
) {

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    var selectedEncryptedFile by remember {
        mutableStateOf<EncryptedModel?>(null)
    }
    LaunchedEffect(key1 = true) {
        appState.showBottomBarBySettingValueFromScreen = true
        appState.selectedBottomNavItemRoute = Screens.EncryptedFileListScreen.route
        viewModel.getEncryptedFiles()
    }

    observeAsEvents(flow = viewModel.showErrorMessage) { event ->
        Toast.makeText(
            appState.context,
            event.message,
            Toast.LENGTH_LONG
        ).show()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {

        val (loading, tvEmptyList, list) = createRefs()

        when (viewModel.encryptedFilesState.isLoading) {
            true -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .constrainAs(loading) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }

            false -> {
                when (viewModel.encryptedFilesState.showDecryptionAnimation) {
                    true -> {
                        EncryptionAnimation()
                    }

                    false -> {
                        if (viewModel.encryptedFilesState.encryptedFiles.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.no_data),
                                color = White,
                                style = titleLarge,
                                modifier = Modifier
                                    .constrainAs(tvEmptyList) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .constrainAs(list) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start, 22.dp)
                                        end.linkTo(parent.end, 22.dp)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    },
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                item {
                                    Spacer(modifier = Modifier.height(40.dp))
                                }

                                items(
                                    items = viewModel.encryptedFilesState.encryptedFiles,
                                ) { encryptedFile ->

                                    EncryptedItem(
                                        encryptedModel = encryptedFile,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        encryptedFile?.let {
                                            selectedEncryptedFile = it
                                            isBottomSheetOpen = true
                                        }
                                    }
                                }

                                item {
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isBottomSheetOpen) {
            EncryptedItemBottomSheet(
                appState = appState,
                encryptedModel = selectedEncryptedFile,
                onDecryptClick = { encryptedDto ->
                    encryptedDto?.let { viewModel.showFileFromDB(appState.context, it) }
                },
                onDeleteClick = { encryptedDto ->
                    encryptedDto?.let { viewModel.deleteFileFromDB(it) }
                },
                onDismiss = {
                    isBottomSheetOpen = false
                    selectedEncryptedFile = null
                }
            )
        }
    }
}