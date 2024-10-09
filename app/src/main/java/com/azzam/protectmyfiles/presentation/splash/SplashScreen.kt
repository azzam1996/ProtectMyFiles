package com.azzam.protectmyfiles.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.util.navOptionsNoBack
import timber.log.Timber

@Composable
fun SplashScreen(
    appState: AppState,
    splashViewModel: SplashViewModel,
    modifier: Modifier = Modifier
) {

    if (splashViewModel.navigateToNextScreen.value != null) {
        val route = splashViewModel.navigateToNextScreen.value
        Timber.v("navigateToNextScreen : ${splashViewModel.navigateToNextScreen.value}")
        splashViewModel.setNavigateToNextScreen(null)
        route?.let {
            appState.navigate(it, appState.navController.navOptionsNoBack())
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (logo) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp, 240.dp)
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}