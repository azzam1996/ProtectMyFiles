package com.azzam.protectmyfiles.presentation.customViews


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.presentation.ui.theme.labelVerySmall
import com.azzam.protectmyfiles.presentation.ui.theme.titleMedium
import timber.log.Timber

@Composable
fun InputField(
    state: InputFieldState,
    modifier: Modifier = Modifier,
    onFocusChange: (() -> Unit)? = null,
    onTextChange: (InputFieldState) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .wrapContentSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (clText, errorText) = createRefs()

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(52.dp, 200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = state.borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .constrainAs(clText) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(if (state.error.isNullOrEmpty()) parent.bottom else errorText.top)
                    }
            ) {
                val (text, hint, icon) = createRefs()

                BasicTextField(
                    value = state.text,
                    onValueChange = { newText ->
                        Timber.v("Next Text : $newText")
                        onTextChange.invoke(state.copy(text = newText, error = null))
                    },
                    textStyle = titleMedium.copy(color = if (state.readOnly) Color.Gray else state.textColor),
                    singleLine = true,
                    readOnly = state.readOnly,
                    visualTransformation = if (state.inputType != KeyboardType.Password) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = state.inputType,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (focusState.hasFocus) {
                                onFocusChange?.invoke()
                                onTextChange.invoke(state.copy(borderColor = Color.White))
                            } else {
                                onTextChange.invoke(state.copy(borderColor = Color.Gray))
                            }
                        }
                        .constrainAs(text) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start, 10.dp)
                            end.linkTo(parent.end, if (state.isPassword) 45.dp else 5.dp)
                            width = Dimension.fillToConstraints
                        }
                )

                if (state.text.isEmpty()) {
                    Text(
                        text = state.hint,
                        style = titleMedium.copy(color = Color.Gray),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .constrainAs(hint) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start, 10.dp)
                                end.linkTo(parent.end, if (state.isPassword) 45.dp else 5.dp)
                                width = Dimension.fillToConstraints
                            }
                    )
                }
                if (state.isPassword) {
                    Image(
                        painter = painterResource(id = if (state.inputType == KeyboardType.Password) R.drawable.ic_eye_off_grey600_24dp else R.drawable.ic_eye_white_24dp),
                        contentDescription = "show/hide password",
                        modifier = Modifier
                            .size(30.dp, 20.dp)
                            .constrainAs(icon) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end, 10.dp)
                                start.linkTo(text.end)
                            }
                            .clickable {
                                onTextChange(
                                    if (state.inputType == KeyboardType.Password) {
                                        state.copy(inputType = KeyboardType.Text)
                                    } else {
                                        state.copy(inputType = KeyboardType.Password)
                                    }
                                )
                            }
                    )
                }
            }

            if (!state.error.isNullOrEmpty()) {
                Text(
                    text = state.error,
                    color = Color.Red,
                    style = labelVerySmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(errorText) {
                            top.linkTo(clText.bottom, 5.dp)
                            start.linkTo(parent.start, 10.dp)
                            end.linkTo(parent.end, 5.dp)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
}

data class InputFieldState(
    val text: String = "",
    val hint: String = "",
    val error: String? = null,
    var inputType: KeyboardType = KeyboardType.Text,
    val borderColor: Color = Color.Gray,
    val textColor: Color = Color.White,
    val readOnly: Boolean = false,
    val isPassword: Boolean = false,
    val isFocused: Boolean = false,
)

@Preview(showBackground = true)
@Composable
fun InputFieldPreview() {
    InputField(
        state = InputFieldState(
            text = "gggggggggggggggg",
            error = "Hi Azzam, we've got new job recommendations for you\\n\" +\n" +
                    "                        \"We want to help find the job that’s right for you – and these roles could be a match.\\n\" +\n" +
                    "                        \"We recommend these jobs based on your profile, past viewed jobs and applications."
        ),
        modifier = Modifier
            .fillMaxWidth()

    ) {

    }
}