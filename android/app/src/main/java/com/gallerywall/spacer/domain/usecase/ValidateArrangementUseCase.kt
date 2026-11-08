package com.gallerywall.spacer.domain.usecase

import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.NailPosition
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.WallSpec

class ValidateArrangementUseCase {

    operator fun invoke(
        wall: WallSpec,
        frames: List<FrameSpec>,
        placements: List<PlacedFrame>,
        nails: List<NailPosition>
    ): ArrangementIssue {
        if (placements.isEmpty()) return ArrangementIssue.None
        val byId = frames.associateBy { it.id }
        var left = Float.MAX_VALUE
        var right = 0f
        var bottom = Float.MAX_VALUE
        var top = 0f
        placements.forEach { placed ->
            val frame = byId[placed.frameId]
            if (frame != null) {
                left = minOf(left, placed.xCm)
                right = maxOf(right, placed.xCm + frame.widthCm)
                bottom = minOf(bottom, placed.yCm)
                top = maxOf(top, placed.yCm + frame.heightCm)
            }
        }
        if (left < 0f || right > wall.widthCm) return ArrangementIssue.TooWide
        if (top > wall.heightCm) return ArrangementIssue.AboveCeiling
        if (bottom < wall.furnitureTopCm) return ArrangementIssue.OverFurniture
        val offWall = nails.firstOrNull { it.yCm <= 0f || it.yCm >= wall.heightCm }
        return if (offWall != null) ArrangementIssue.NailOffWall(offWall.frameId) else ArrangementIssue.None
    }

    fun offendingFrameId(
        wall: WallSpec,
        frames: List<FrameSpec>,
        placements: List<PlacedFrame>
    ): Int {
        val byId = frames.associateBy { it.id }
        placements.forEach { placed ->
            val frame = byId[placed.frameId]
            if (frame != null) {
                val outside = placed.xCm < 0f ||
                    placed.xCm + frame.widthCm > wall.widthCm ||
                    placed.yCm < wall.furnitureTopCm ||
                    placed.yCm + frame.heightCm > wall.heightCm
                if (outside) return frame.id
            }
        }
        return NO_FRAME
    }

    companion object {
        const val NO_FRAME = -1
    }
}
