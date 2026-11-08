package com.gallerywall.spacer.domain.usecase

import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.NailPosition
import com.gallerywall.spacer.domain.model.PlacedFrame
import kotlin.math.round

class ComputeNailPositionsUseCase {

    operator fun invoke(
        frames: List<FrameSpec>,
        placements: List<PlacedFrame>
    ): List<NailPosition> {
        val byId = frames.associateBy { it.id }
        val raw = placements.mapNotNull { placed ->
            val frame = byId[placed.frameId] ?: return@mapNotNull null
            NailPosition(
                frameId = frame.id,
                index = 0,
                xCm = round1(placed.xCm + frame.widthCm / 2f),
                yCm = round1(placed.yCm + frame.heightCm - frame.hangerDropCm),
                done = false
            )
        }
        val ordered = raw.sortedWith(
            compareByDescending<NailPosition> { it.yCm }.thenBy { it.xCm }
        )
        return ordered.mapIndexed { position, nail -> nail.copy(index = position + 1) }
    }

    private fun round1(value: Float): Float = round(value * 10f) / 10f
}
