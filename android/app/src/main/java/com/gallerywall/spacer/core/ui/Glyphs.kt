package com.gallerywall.spacer.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BackGlyph(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp
) {
    Canvas(modifier.size(size)) {
        val stroke = 2.dp.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(
            color = color,
            start = Offset(w * 0.58f, h * 0.22f),
            end = Offset(w * 0.30f, h * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.30f, h * 0.5f),
            end = Offset(w * 0.58f, h * 0.78f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.30f, h * 0.5f),
            end = Offset(w * 0.76f, h * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ForwardGlyph(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp
) {
    Canvas(modifier.size(size)) {
        val stroke = 2.dp.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(
            color = color,
            start = Offset(w * 0.40f, h * 0.24f),
            end = Offset(w * 0.66f, h * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.66f, h * 0.5f),
            end = Offset(w * 0.40f, h * 0.76f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun MinusGlyph(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp
) {
    Canvas(modifier.size(size)) {
        val stroke = 2.dp.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(
            color = color,
            start = Offset(w * 0.22f, h * 0.5f),
            end = Offset(w * 0.78f, h * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun PlusGlyph(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp
) {
    Canvas(modifier.size(size)) {
        val stroke = 2.dp.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(
            color = color,
            start = Offset(w * 0.22f, h * 0.5f),
            end = Offset(w * 0.78f, h * 0.5f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w * 0.5f, h * 0.22f),
            end = Offset(w * 0.5f, h * 0.78f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}
