package m.tech.jetbinance.screen.core

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import m.tech.jetbinance.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp

private val reactPopupHeight = 80.dp
private val reactPopupPaddingVertical = 24.dp
private val reactIconDefault = 36.dp
private val reactIconZoomIn = 64.dp
private val reactIconZoomOut = 28.dp
private val reactIconPadding = 16.dp
private val reactRowPaddingStart = 24.dp
private const val NONE_HOVERING_REACT = -1

@Composable
fun ReactPopup(
    popupViewRect: Rect,
    onDismissRequest: () -> Unit,
    onReactSelected: (react: Int) -> Unit
) {
    val cutOffHeight =
        with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() / 5 }
    val popupHeight = with(LocalDensity.current) { reactPopupHeight.toPx() }
    val popupPaddingVertical = with(LocalDensity.current) { reactPopupPaddingVertical.toPx() }
    val reactSizeDefault = with(LocalDensity.current) { reactIconDefault.toPx() }
    val reactSizeZoomIn = with(LocalDensity.current) { reactIconZoomIn.toPx() }
    val reactSizeZoomOut = with(LocalDensity.current) { reactIconZoomOut.toPx() }
    val reactRowPaddingStart = with(LocalDensity.current) { reactRowPaddingStart.toPx() }
    val reactPadding = with(LocalDensity.current) { reactIconPadding.toPx() }
    var showBelow by remember {
        mutableStateOf(false)
    }

    val (x: Float, y: Float) = if (popupViewRect.top - (popupHeight + popupPaddingVertical) < cutOffHeight) {
        // show below
        showBelow = true
        popupViewRect.left to (popupViewRect.bottom + popupPaddingVertical)
    } else {
        // show above
        showBelow = false
        popupViewRect.left to (popupViewRect.top - popupHeight - popupPaddingVertical)
    }
    var isLongClick by remember { mutableStateOf(false) }
    var hoveringReact by remember { mutableStateOf(NONE_HOVERING_REACT) }

    val sizeLike by animateDpAsState(
        calculateIconSize(
            hoveringReact == 0,
            isLongClick,
            hoveringReact == NONE_HOVERING_REACT
        ), label = ""
    )
    val sizeLaugh by animateDpAsState(
        calculateIconSize(
            hoveringReact == 1,
            isLongClick,
            hoveringReact == NONE_HOVERING_REACT
        ), label = ""
    )
    val sizeLove by animateDpAsState(
        calculateIconSize(
            hoveringReact == 2,
            isLongClick,
            hoveringReact == NONE_HOVERING_REACT
        ), label = ""
    )
    val sizeCheer by animateDpAsState(
        calculateIconSize(
            hoveringReact == 3,
            isLongClick,
            hoveringReact == NONE_HOVERING_REACT
        ), label = ""
    )
    val sizeRocket by animateDpAsState(
        calculateIconSize(
            hoveringReact == 4,
            isLongClick,
            hoveringReact == NONE_HOVERING_REACT
        ), label = ""
    )
    val sizePlaceholder by animateDpAsState(
        if (hoveringReact == NONE_HOVERING_REACT) {
            52.dp
        } else if (isLongClick) {
            44.dp
        } else {
            52.dp
        }, label = ""
    )

    Popup(
        alignment = Alignment.TopStart,
        offset = IntOffset(x.toInt(), y.toInt()),
        onDismissRequest = onDismissRequest,
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(292.dp, sizePlaceholder)
                    .background(
                        color = Color(0xFF29313c),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .align(if (showBelow) Alignment.TopStart else Alignment.BottomStart)
            )
            Row(
                verticalAlignment = if (showBelow) Alignment.Top else Alignment.Bottom,
                modifier = Modifier
                    .height(80.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                isLongClick = true
                            },
                            onDragEnd = {
                                isLongClick = false
                                if (hoveringReact != NONE_HOVERING_REACT) {
                                    onReactSelected(hoveringReact)
                                }
                                hoveringReact = NONE_HOVERING_REACT
                            },
                            onDrag = { change, _ ->
                                hoveringReact =
                                    calculateTapIndex(
                                        offsetX = change.position.x,
                                        reactSizeDefault = reactSizeDefault,
                                        reactSizeZoomIn = reactSizeZoomIn,
                                        reactSizeZoomOut = reactSizeZoomOut,
                                        reactPadding = reactPadding,
                                        reactRowPaddingStart = reactRowPaddingStart,
                                        selectIndex = hoveringReact
                                    )
                            }
                        )
                    }
                    .padding(vertical = 8.dp, horizontal = 24.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .size(sizeLike)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =  null,
                            onClick = {
                                onReactSelected(0)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_react_like),
                    contentDescription = "like",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(reactIconPadding))
                Icon(
                    modifier = Modifier
                        .size(sizeLaugh)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =  null,
                            onClick = {
                                onReactSelected(1)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_react_laugh),
                    contentDescription = "laugh",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(reactIconPadding))
                Icon(
                    modifier = Modifier
                        .size(sizeLove)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =  null,
                            onClick = {
                                onReactSelected(2)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_react_love),
                    contentDescription = "love",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(reactIconPadding))
                Icon(
                    modifier = Modifier
                        .size(sizeCheer)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =  null,
                            onClick = {
                                onReactSelected(3)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_react_cheer),
                    contentDescription = "cheer",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(reactIconPadding))
                Icon(
                    modifier = Modifier
                        .size(sizeRocket)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication =  null,
                            onClick = {
                                onReactSelected(4)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_react_rocket),
                    contentDescription = "rocket",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

private fun calculateIconSize(isZoom: Boolean, isLongClick: Boolean, useDefault: Boolean): Dp {
    return if (useDefault) {
        reactIconDefault
    } else if (isZoom) {
        reactIconZoomIn
    } else if (isLongClick) {
        reactIconZoomOut
    } else {
        reactIconDefault
    }
}

private fun calculateTapIndex(
    offsetX: Float,
    reactSizeDefault: Float,
    reactSizeZoomIn: Float,
    reactSizeZoomOut: Float,
    reactPadding: Float,
    reactRowPaddingStart: Float,
    selectIndex: Int?
): Int {
    var endBoundaryOfReact = reactRowPaddingStart

    // hovering on padding start zone
    if (offsetX < endBoundaryOfReact) return -1

    for (i in 0 until 5) {
        val reactSize = when (selectIndex) {
            NONE_HOVERING_REACT -> {
                // normal
                reactSizeDefault
            }
            i -> {
                // react is zooming
                reactSizeZoomIn
            }
            else -> {
                // long click -> zoom out react
                reactSizeZoomOut
            }
        }
        // tap zone = from start to react size + padding / 2
        // react1<padding>react2<padding>react3<padding>........
        // (tapzoneee1)(tapzoneeeee2)(tapzoneeeeeee3)(...........)
        endBoundaryOfReact += (reactSize + reactPadding / 2)

        if (offsetX <= endBoundaryOfReact) return i

        // append the left padding after calculated tap zone above
        endBoundaryOfReact += reactPadding / 2
    }

    return -1
}
