package com.example.jawlah.presentation.util


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.border(
    width: Dp,
    color: Color,
    top: Boolean = false,
    bottom: Boolean = false,
    start: Boolean = false,
    end: Boolean = false
) = this.then(
    drawBehind {
        if (top) {
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = width.toPx()
            )
        }
        if (bottom) {
            drawLine(
                color = color,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = width.toPx()
            )
        }
        if (start) {
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = width.toPx()
            )
        }
        if (end) {
            drawLine(
                color = color,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = width.toPx()
            )
        }
    }
)