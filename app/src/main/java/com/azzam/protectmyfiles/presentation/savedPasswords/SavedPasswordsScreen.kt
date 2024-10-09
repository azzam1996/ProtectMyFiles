package com.azzam.protectmyfiles.presentation.savedPasswords

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.bottomSheet.SavedPasswordItemBottomSheet
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleLarge
import com.azzam.protectmyfiles.util.UIEvent
import com.azzam.protectmyfiles.util.extensions.observeAsEvents

@Composable
fun SavedPasswordsScreen(
    appState: AppState,
    viewModel: SavedPasswordsViewModel,
    modifier: Modifier = Modifier
) {

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    var selectedSavedPassword by remember {
        mutableStateOf<PasswordModel?>(null)
    }

    LaunchedEffect(key1 = true) {
        appState.showBottomBarBySettingValueFromScreen = true
        appState.selectedBottomNavItemRoute = Screens.SavedPasswordsScreen.route
        viewModel.getAllSavedPasswords()
    }

    observeAsEvents(flow = viewModel.showErrorMessage) { event ->
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

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (loading, tvEmptyList, list, addNewPassword) = createRefs()

        when (viewModel.state.isLoading) {
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
                when (viewModel.state.savedPasswords.isEmpty()) {
                    true -> {
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
                    }

                    false -> {
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

                            items(viewModel.state.savedPasswords) { savedPassword ->
                                SavedPasswordItem(
                                    passwordModel = savedPassword,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    action = {
                                        selectedSavedPassword = savedPassword
                                        isBottomSheetOpen = true
                                    }
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .constrainAs(addNewPassword) {
                            bottom.linkTo(parent.bottom, 20.dp)
                            end.linkTo(parent.end, 20.dp)
                        }
                        .clickable {
                            selectedSavedPassword = null
                            isBottomSheetOpen = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Password",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }

        if (isBottomSheetOpen) {
            SavedPasswordItemBottomSheet(
                appState = appState,
                passwordModel = selectedSavedPassword,
                onInsertClick = { savedPassword ->
                    savedPassword?.let { viewModel.insertPasswordModel(it) }
                },
                onDeleteClick = { savedPassword ->
                    savedPassword?.let { viewModel.deletePasswordModel(it) }
                },
                onDismiss = {
                    isBottomSheetOpen = false
                    selectedSavedPassword = null
                }
            )
        }
    }
}