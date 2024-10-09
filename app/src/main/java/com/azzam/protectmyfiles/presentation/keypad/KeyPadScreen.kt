package com.azzam.protectmyfiles.presentation.keypad

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.customViews.KeyPadButton
import com.azzam.protectmyfiles.presentation.ui.theme.White
import com.azzam.protectmyfiles.presentation.ui.theme.titleMedium
import com.azzam.protectmyfiles.util.KEYPAD_CIRCLE_SIZE
import com.azzam.protectmyfiles.util.extensions.getKeypadButtonSize
import com.azzam.protectmyfiles.util.extensions.observeAsEvents
import com.azzam.protectmyfiles.util.navOptionsNoBack

@Composable
fun KeyPadScreen(
    appState: AppState,
    keypadViewModel: KeypadViewModel,
    modifier: Modifier = Modifier
) {

    observeAsEvents(flow = keypadViewModel.showErrorMessage) { event ->
        Toast.makeText(
            appState.context,
            appState.context.getString(event.message),
            Toast.LENGTH_LONG
        ).show()
    }
    observeAsEvents(flow = keypadViewModel.navigateToNextPage) { route ->
        appState.navigate(route, appState.navController.navOptionsNoBack())
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        val (tvEnterPasscodeAgain, code, keypad) = createRefs()


        Text(
            text = stringResource(id = R.string.please_enter_the_passcode_again),
            style = titleMedium,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .alpha(if (keypadViewModel.keypadScreenState.value.isEnteringForSecondTime) 1f else 0f)
                .constrainAs(tvEnterPasscodeAgain) {
                    top.linkTo(parent.top, 60.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Row(
            modifier = Modifier
                .constrainAs(code) {
                    top.linkTo(tvEnterPasscodeAgain.bottom, 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            (1..4).forEach { i ->
                if (keypadViewModel.keypadScreenState.value.enteredCode.length >= i) {
                    Canvas(modifier = Modifier.size(KEYPAD_CIRCLE_SIZE.dp), onDraw = {
                        drawCircle(color = Color.White)
                    })
                } else {
                    Canvas(modifier = Modifier.size(KEYPAD_CIRCLE_SIZE.dp), onDraw = {
                        drawCircle(color = Color.Gray)
                    })
                }
            }
        }

        Column(
            modifier = Modifier
                .constrainAs(keypad) {
                    top.linkTo(code.bottom, 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                (1..3).forEach { digit ->
                    KeyPadButton(
                        title = "$digit",
                        size = getKeypadButtonSize(context = appState.context)
                    ) {
                        keypadViewModel.updateCode("$digit")
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                (4..6).forEach { digit ->
                    KeyPadButton(
                        title = "$digit",
                        size = getKeypadButtonSize(context = appState.context)
                    ) {
                        keypadViewModel.updateCode("$digit")
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                (7..9).forEach { digit ->
                    KeyPadButton(
                        title = "$digit",
                        size = getKeypadButtonSize(context = appState.context)
                    ) {
                        keypadViewModel.updateCode("$digit")
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.size(size = getKeypadButtonSize(context = appState.context)))

                KeyPadButton(title = "0", size = getKeypadButtonSize(context = appState.context)) {
                    keypadViewModel.updateCode("0")
                }
                KeyPadButton(title = "X", size = getKeypadButtonSize(context = appState.context)) {
                    keypadViewModel.deleteDigit()
                }
            }
        }
    }
}