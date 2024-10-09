package com.azzam.protectmyfiles.util.extensions

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.azzam.protectmyfiles.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext


@Composable
fun <T> observeAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifeCycleOwner) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collectLatest(onEvent)
            }
        }
    }
}

@Composable
fun getValueOrNoData(value: String?): String {
    return value ?: stringResource(id = R.string.no_data)
}

@Composable
fun getKeypadButtonSize(context: Context): Dp {
    val screenWidth =
        context.resources.displayMetrics.widthPixels.dp / context.resources.displayMetrics.density
    return if (screenWidth < 380.dp) {
        70.dp
    } else {
        100.dp
    }
}

val ArrangementLazyColumnLastItemToBottom = object : Arrangement.Vertical {
    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        var consumedHeight = 0
        sizes.forEachIndexed { index, height ->
            if (index == sizes.lastIndex) {
                // If it's last item position at the bottom
                outPositions[index] = totalSize - height
            } else {
                // If not last item position below other
                outPositions[index] = consumedHeight
                consumedHeight += height
            }
        }
    }
}