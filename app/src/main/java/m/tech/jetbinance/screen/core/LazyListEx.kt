package m.tech.jetbinance.screen.core

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import m.tech.jetbinance.screen.home.model.viewdata.HomeViewData

@Composable
fun LazyListState.isScrollingUp(additionalCondition: Boolean = true): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex &&  firstVisibleItemIndex >= HomeViewData.indexOfFeedHeader
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset &&  firstVisibleItemIndex >= HomeViewData.indexOfFeedHeader
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}