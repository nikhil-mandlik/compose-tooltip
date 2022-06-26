package com.nikhil.here.compose_tooltip.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout


@Composable
fun MyToolTip(
    modifier: Modifier = Modifier,
    arrowPosition: TipPosition,
    arrowHeight: Float,
    tipColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(getTooltipShape(arrowPosition, 30f))
            .background(tipColor)
    ) {
        Layout(
            content = content
        ) { measurables, constraints ->

            val tooltipContentconstraints = constraints.copy(
                maxHeight = constraints.maxHeight - arrowHeight.toInt()
            )

            val placeables = measurables.map {
                it.measure(tooltipContentconstraints)
            }

            val width = placeables.maxOf { it.width }
            val height = placeables.sumOf { it.height }

            layout(width = width, height = height) {
                var y = if (arrowPosition is TipPosition.TopCenter || arrowPosition is TipPosition.Top){
                    arrowHeight
                } else {
                    0
                }

                placeables.forEach {
                    it.placeRelative(0,y.toInt())
                    y = it.height
                }
            }
        }
    }
}


sealed class TipPosition(val percent: Float) {
    object TopCenter : TipPosition(0f)
    object BottomCenter : TipPosition(0f)
    data class Top(val marginPercent: Float = 0.2f) : TipPosition(marginPercent)
    data class Bottom(val marginPercent: Float = 0.2f) : TipPosition(marginPercent)
}

fun getTooltipShape(arrowPosition: TipPosition, arrowHeight: Float): GenericShape {
    val tooltipShape = GenericShape { size, _ ->
        val arrowWidth = 0.1f * size.width
        if (arrowPosition is TipPosition.Top || arrowPosition is TipPosition.TopCenter) {
            val arrowStartPosition = if (arrowPosition is TipPosition.TopCenter) {
                (size.width - arrowWidth) / 2
            } else {
                (size.width * arrowPosition.percent)
            }
            reset()
            lineTo(0f, arrowHeight)
            lineTo(arrowStartPosition, arrowHeight)
            lineTo(arrowStartPosition + arrowWidth / 2, 0f)
            lineTo(arrowStartPosition + arrowWidth, arrowHeight)
            lineTo(size.width, arrowHeight)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            lineTo(0f, arrowHeight)
            close()
        } else {
            val arrowRightPosition = if (arrowPosition is TipPosition.BottomCenter) {
                (size.width + arrowWidth) / 2
            } else {
                (size.width * arrowPosition.percent) + arrowWidth
            }
            reset()
            lineTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - arrowHeight)

            lineTo(arrowRightPosition, size.height - arrowHeight)

            lineTo(arrowRightPosition - arrowWidth / 2, size.height)
            lineTo(arrowRightPosition - arrowWidth, size.height - arrowHeight)
            lineTo(0f, size.height - arrowHeight)
            lineTo(0f, 0f)
            close()
        }
    }
    return tooltipShape
}