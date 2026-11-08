package com.gallerywall.spacer.domain.usecase

import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.WallSpec
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.sqrt

class SolveArrangementUseCase {

    operator fun invoke(
        wall: WallSpec,
        frames: List<FrameSpec>,
        style: LayoutStyle,
        gapCm: Float,
        eyeHeightCm: Float
    ): List<PlacedFrame> {
        if (frames.isEmpty()) return emptyList()
        val raw = when (style) {
            LayoutStyle.ROW -> solveRow(wall, frames, gapCm, eyeHeightCm)
            LayoutStyle.GRID -> solveGrid(wall, frames, gapCm, eyeHeightCm)
            LayoutStyle.CLUSTER -> solveCluster(wall, frames, gapCm, eyeHeightCm)
        }
        return raw.map { it.copy(xCm = round1(it.xCm), yCm = round1(it.yCm)) }
    }

    private fun solveRow(
        wall: WallSpec,
        frames: List<FrameSpec>,
        gapCm: Float,
        eyeHeightCm: Float
    ): List<PlacedFrame> {
        var total = gapCm * (frames.size - 1)
        frames.forEach { total += it.widthCm }
        var cursor = (wall.widthCm - total) / 2f
        return frames.map { frame ->
            val placed = PlacedFrame(frame.id, cursor, eyeHeightCm - frame.heightCm / 2f)
            cursor += frame.widthCm + gapCm
            placed
        }
    }

    private fun solveGrid(
        wall: WallSpec,
        frames: List<FrameSpec>,
        gapCm: Float,
        eyeHeightCm: Float
    ): List<PlacedFrame> {
        val n = frames.size
        val cols = ceil(sqrt(n.toFloat())).toInt().coerceIn(2, 4).coerceAtMost(n)
        val rows = ceil(n.toFloat() / cols).toInt()
        val cellW = frames.maxOf { it.widthCm }
        val cellH = frames.maxOf { it.heightCm }
        val blockW = cols * cellW + gapCm * (cols - 1)
        val blockH = rows * cellH + gapCm * (rows - 1)
        val startX = (wall.widthCm - blockW) / 2f
        val topY = eyeHeightCm + blockH / 2f
        val lastRowCount = n - (rows - 1) * cols
        return frames.mapIndexed { i, frame ->
            val col = i % cols
            val row = i / cols
            val rowShift = if (row == rows - 1 && lastRowCount < cols) {
                ((cols - lastRowCount) * (cellW + gapCm)) / 2f
            } else {
                0f
            }
            val cx = startX + rowShift + col * (cellW + gapCm) + cellW / 2f
            val cy = topY - row * (cellH + gapCm) - cellH / 2f
            PlacedFrame(frame.id, cx - frame.widthCm / 2f, cy - frame.heightCm / 2f)
        }
    }

    private fun solveCluster(
        wall: WallSpec,
        frames: List<FrameSpec>,
        gapCm: Float,
        eyeHeightCm: Float
    ): List<PlacedFrame> {
        val order = frames.sortedByDescending { it.widthCm * it.heightCm }
        val anchor = order.first()
        val placed = LinkedHashMap<Int, PlacedFrame>()
        val anchorX = wall.widthCm / 2f - anchor.widthCm / 2f
        val anchorY = eyeHeightCm - anchor.heightCm / 2f
        placed[anchor.id] = PlacedFrame(anchor.id, anchorX, anchorY)
        var left = anchorX
        var right = anchorX + anchor.widthCm
        var bottom = anchorY
        var top = anchorY + anchor.heightCm
        val anchorCx = anchorX + anchor.widthCm / 2f
        val anchorCy = anchorY + anchor.heightCm / 2f

        order.drop(1).forEachIndexed { index, frame ->
            val slot = SLOTS[index % SLOTS.size]
            val px: Float
            val py: Float
            when (slot) {
                Slot.RIGHT -> {
                    px = right + gapCm
                    py = anchorCy - frame.heightCm / 2f
                }
                Slot.LEFT -> {
                    px = left - gapCm - frame.widthCm
                    py = anchorCy - frame.heightCm / 2f
                }
                Slot.ABOVE -> {
                    px = anchorCx - frame.widthCm / 2f
                    py = top + gapCm
                }
                Slot.BELOW -> {
                    px = anchorCx - frame.widthCm / 2f
                    py = bottom - gapCm - frame.heightCm
                }
                Slot.RIGHT_UP -> {
                    px = right + gapCm
                    py = top + gapCm
                }
                Slot.LEFT_DOWN -> {
                    px = left - gapCm - frame.widthCm
                    py = bottom - gapCm - frame.heightCm
                }
                Slot.RIGHT_DOWN -> {
                    px = right + gapCm
                    py = bottom - gapCm - frame.heightCm
                }
                Slot.LEFT_UP -> {
                    px = left - gapCm - frame.widthCm
                    py = top + gapCm
                }
            }
            placed[frame.id] = PlacedFrame(frame.id, px, py)
            left = minOf(left, px)
            right = maxOf(right, px + frame.widthCm)
            bottom = minOf(bottom, py)
            top = maxOf(top, py + frame.heightCm)
        }

        val shiftX = wall.widthCm / 2f - (left + right) / 2f
        val shiftY = eyeHeightCm - (bottom + top) / 2f
        return frames.map { frame ->
            val p = placed.getValue(frame.id)
            PlacedFrame(frame.id, p.xCm + shiftX, p.yCm + shiftY)
        }
    }

    private fun round1(value: Float): Float = round(value * 10f) / 10f

    private enum class Slot {
        RIGHT,
        LEFT,
        ABOVE,
        BELOW,
        RIGHT_UP,
        LEFT_DOWN,
        RIGHT_DOWN,
        LEFT_UP
    }

    private companion object {
        val SLOTS = listOf(
            Slot.RIGHT,
            Slot.LEFT,
            Slot.ABOVE,
            Slot.BELOW,
            Slot.RIGHT_UP,
            Slot.LEFT_DOWN,
            Slot.RIGHT_DOWN,
            Slot.LEFT_UP
        )
    }
}
