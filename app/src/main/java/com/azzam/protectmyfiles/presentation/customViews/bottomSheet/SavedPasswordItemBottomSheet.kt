package com.azzam.protectmyfiles.presentation.customViews.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.InputField
import com.azzam.protectmyfiles.presentation.customViews.InputFieldState
import com.azzam.protectmyfiles.presentation.ui.theme.Arapawa
import com.azzam.protectmyfiles.presentation.ui.theme.BlackRock
import com.azzam.protectmyfiles.presentation.ui.theme.Red
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPasswordItemBottomSheet(
    appState: AppState,
    passwordModel: PasswordModel?,
    modifier: Modifier = Modifier,
    onInsertClick: (PasswordModel?) -> Unit,
    onDeleteClick: (PasswordModel?) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val nameHint = stringResource(id = R.string.name)
    val usernameHint = stringResource(id = R.string.username)
    val passwordHint = stringResource(id = R.string.password)

    var nameState by remember {
        mutableStateOf(
            InputFieldState(
                hint = nameHint,
                text = passwordModel?.name ?: ""
            )
        )
    }
    var usernameState by remember {
        mutableStateOf(
            InputFieldState(
                hint = usernameHint,
                text = passwordModel?.username ?: "",
            )
        )
    }
    var passwordState by remember {
        mutableStateOf(
            InputFieldState(
                hint = passwordHint,
                text = passwordModel?.password ?: "",
                isPassword = true,
                inputType = KeyboardType.Password
            )
        )
    }

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
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            InputField(
                state = nameState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { state ->
                nameState = state
            }

            Spacer(modifier = Modifier.height(10.dp))

            InputField(
                state = usernameState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { state ->
                usernameState = state
            }

            Spacer(modifier = Modifier.height(10.dp))

            InputField(
                state = passwordState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { state ->
                passwordState = state
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = BlackRock.copy(alpha = 0.8f),
                    containerColor = Color.Transparent
                ),
                onClick = {
                    if (nameState.text.isEmpty()) {
                        nameState = nameState.copy(error = appState.context.getString(R.string.field_mandatory))
                        return@Button
                    }
                    if (usernameState.text.isEmpty()) {
                        usernameState = usernameState.copy(error = appState.context.getString(R.string.field_mandatory))
                        return@Button
                    }
                    if (passwordState.text.isEmpty()) {
                        passwordState = passwordState.copy(error = appState.context.getString(R.string.field_mandatory))
                        return@Button
                    }
                    val newPasswordModel = PasswordModel(
                        id = passwordModel?.id,
                        name = nameState.text,
                        username = usernameState.text,
                        password = passwordState.text
                    )
                    onInsertClick.invoke(newPasswordModel)
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
                    text = stringResource(id = R.string.save),
                    style = titleMedium.copy(color = White),
                )
            }
            if (passwordModel != null) {
                Button(
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Red,
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        onDeleteClick.invoke(passwordModel)
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
}