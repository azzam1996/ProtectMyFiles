package com.azzam.protectmyfiles.presentation.customViews

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.azzam.protectmyfiles.R
import com.azzam.protectmyfiles.presentation.AppState
import com.azzam.protectmyfiles.presentation.navigation.Screens
import com.azzam.protectmyfiles.presentation.rememberAppState
import com.azzam.protectmyfiles.presentation.ui.theme.Violet
import com.azzam.protectmyfiles.presentation.ui.theme.White
import kotlinx.parcelize.Parcelize
import timber.log.Timber


@SuppressLint("MutableCollectionMutableState")
@Composable
fun CustomBottomNav(
    appState: AppState,
    modifier: Modifier = Modifier,
    action: (BottomNavBarItem) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .height(if (appState.showBottomBarBySettingValueFromScreen) 50.dp else 0.dp)
            .background(White.copy(alpha = 0.9f))
            .padding(horizontal = 10.dp)
    ) {

        val (navItemsRow) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(navItemsRow) {
                    bottom.linkTo(parent.bottom, 5.dp)
                }
        ) {
            bottomNavItems.forEachIndexed() { index, item ->
                BottomNavItem(
                    item = item.copy(isSelected = appState.selectedBottomNavItemRoute == item.route),
                    action = { selectedItem ->
                        Timber.v("You Pressed ${selectedItem.toString()}")
                        action.invoke(selectedItem)
                    })
                if (index != bottomNavItems.lastIndex) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(
    item: BottomNavBarItem,
    action: (BottomNavBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (icon, selectedCircle) = createRefs()

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(if (item.isSelected) Violet else Color.Gray),
            modifier = Modifier
                .size(30.dp, 30.dp)
                .clickable {
                    action.invoke(item)
                }
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(if (item.isSelected) selectedCircle.top else parent.bottom)
                }
        )

        if (item.isSelected) {
            Canvas(
                modifier = Modifier
                    .size(5.dp)
                    .constrainAs(selectedCircle) {
                        top.linkTo(icon.bottom, 2.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                onDraw = {
                    drawCircle(color = Violet)
                })
        }
    }
}


val bottomNavItems = mutableListOf(
    BottomNavBarItem(
        route = Screens.HomeScreen.route,
        title = "Home",
        icon = R.drawable.ic_home,
        isSelected = true
    ),

    BottomNavBarItem(
        route = Screens.EncryptedFileListScreen.route,
        title = "Encrypted File List",
        icon = R.drawable.ic_list
    ),

    BottomNavBarItem(
        route = Screens.SavedPasswordsScreen.route,
        title = "Saved Passwords",
        icon = R.drawable.ic_key
    ),
)

@Parcelize
data class BottomNavBarItem(
    val route: String,
    val title: String,
    val icon: Int,
    val isSelected: Boolean = false
) : Parcelable

@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    // BottomNavItem(item = bottomNavItems[0])
    CustomBottomNav(rememberAppState()) {}
}