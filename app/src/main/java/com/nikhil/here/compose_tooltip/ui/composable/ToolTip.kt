package com.nikhil.here.compose_tooltip.ui.composable


import androidx.annotation.FloatRange
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


sealed class ArrowPosition {
    object TopLeft : ArrowPosition()
    object TopRight : ArrowPosition()
    object TopCenter : ArrowPosition()
    object BottomLeft : ArrowPosition()
    object BottomRight : ArrowPosition()
    object BottomCenter : ArrowPosition()
    class AbsoluteBiasTop(@FloatRange(from = -1.0, to = 1.0) val bias: Float) : ArrowPosition()
    class AbsoluteBiasBottom(@FloatRange(from = -1.0, to = 1.0) val bias: Float) : ArrowPosition()
    class AbsoluteMarginTopLeft(val margin: Int) : ArrowPosition()
    class AbsoluteMarginTopRight(val margin: Int) : ArrowPosition()
    class AbsoluteMarginBottomLeft(val margin: Int) : ArrowPosition()
    class AbsoluteMarginBottomRight(val margin: Int) : ArrowPosition()
}


@Composable
fun MyToolTip(
    modifier: Modifier = Modifier,
    text: String,
    arrowPosition: ArrowPosition,
    textColor: Color,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    backgroundColor: Color,
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    onAnimationComplete: () -> Unit = {},
    onClick: () -> Unit = {}
) {

    /**
     * linearlayout
     * view -> tooltip
     *
     * column -> two views
     * Row ->
     */
    var isVisible by remember {
        mutableStateOf(true)
    }

    val toolTipPosition by derivedStateOf {
        getAlignmentFromPosition(arrowPosition = arrowPosition)
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = modifier.clickable {
            isVisible = false
            onClick()
        }) {
            if (
                arrowPosition == ArrowPosition.TopRight ||
                arrowPosition == ArrowPosition.TopCenter ||
                arrowPosition == ArrowPosition.TopLeft ||
                arrowPosition is ArrowPosition.AbsoluteBiasTop ||
                arrowPosition is ArrowPosition.AbsoluteMarginTopLeft ||
                arrowPosition is ArrowPosition.AbsoluteMarginTopRight
            ) {

                // Arrow
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(
                            color = backgroundColor,
                            shape = TooltipArrow(arrowPosition)
                        )
                        .align(toolTipPosition)
                        .size(10.dp)

                )

                // Text
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            shape = TooltipArrow(arrowPosition = arrowPosition)
                        }
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = text,
                        color = textColor,
                        style = textStyle
                    )
                }
            } else {
                //Text
                Box(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = text,
                        color = textColor,
                        style = textStyle
                    )
                }

                //Arrow
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .background(
                            color = backgroundColor,
                            shape = TooltipArrow(arrowPosition)
                        )
                        .align(toolTipPosition)
                        .size(10.dp)
                )
            }
        }
    }

}

class TooltipArrow(private val arrowPosition: ArrowPosition) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawTip(size = size, position = arrowPosition)
        )
    }
}




fun drawTip(size: Size, position: ArrowPosition): Path {
    fun topToolTip(): Path {
        return Path().apply {
            reset()
            moveTo(x = size.width / 2, y = 0f)
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
            close()
        }
    }

    fun bottomToolTip(): Path {
        return Path().apply {
            reset()
            lineTo(x = size.width / 2, y = size.height)
            lineTo(x = size.width, y = 0f)
            moveTo(x = size.width / 2, y = 0f)
            close()
        }
    }

    return when (position) {
        ArrowPosition.TopLeft,
        ArrowPosition.TopCenter,
        ArrowPosition.TopRight,
        is ArrowPosition.AbsoluteBiasTop,
        is ArrowPosition.AbsoluteMarginTopLeft,
        is ArrowPosition.AbsoluteMarginTopRight -> topToolTip()
        ArrowPosition.BottomLeft,
        ArrowPosition.BottomCenter,
        ArrowPosition.BottomRight,
        is ArrowPosition.AbsoluteBiasBottom,
        is ArrowPosition.AbsoluteMarginBottomLeft,
        is ArrowPosition.AbsoluteMarginBottomRight -> bottomToolTip()
    }
}

private fun getAlignmentFromPosition(arrowPosition: ArrowPosition): Alignment.Horizontal {
    return when (arrowPosition) {
        ArrowPosition.TopLeft, ArrowPosition.BottomLeft -> {
            Alignment.Start
        }
        ArrowPosition.TopCenter, ArrowPosition.BottomCenter -> {
            Alignment.CenterHorizontally
        }
        ArrowPosition.TopRight, ArrowPosition.BottomRight -> {
            Alignment.End
        }
        is ArrowPosition.AbsoluteBiasTop -> {
            BiasAlignment.Horizontal(arrowPosition.bias)
        }
        is ArrowPosition.AbsoluteBiasBottom -> {
            BiasAlignment.Horizontal(arrowPosition.bias)
        }
        is ArrowPosition.AbsoluteMarginTopRight -> {
            Alignment.Horizontal { size, space, layoutDirection ->
                (space.toFloat() - arrowPosition.margin).roundToInt()
            }
        }
        is ArrowPosition.AbsoluteMarginBottomRight -> {
            Alignment.Horizontal { size, space, layoutDirection ->
                (space.toFloat() - arrowPosition.margin).roundToInt()
            }
        }
        is ArrowPosition.AbsoluteMarginBottomLeft -> {
            Alignment.Horizontal { size, space, layoutDirection ->
                arrowPosition.margin
            }
        }
        is ArrowPosition.AbsoluteMarginTopLeft -> {
            Alignment.Horizontal { size, space, layoutDirection ->
                arrowPosition.margin
            }
        }
    }
}
