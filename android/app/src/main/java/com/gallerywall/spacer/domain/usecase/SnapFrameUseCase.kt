package com.gallerywall.spacer.domain.usecase

import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.SnapAxis
import com.gallerywall.spacer.domain.model.SnapGuide
import com.gallerywall.spacer.domain.model.WallSpec
import kotlin.math.abs

data class SnapResult(
    val placed: PlacedFrame,
    val guides: List<SnapGuide>
)

private data class Candidate(
    val target: Float,
    val current: Float
)

class SnapFrameUseCase {

    operator fun invoke(
        moving: PlacedFrame,
        movingSpec: FrameSpec,
        others: List<Pair<PlacedFrame, FrameSpec>>,
        wall: WallSpec,
        eyeHeightCm: Float,
        toleranceCm: Float
    ): SnapResult {
        val movingCx = moving.xCm + movingSpec.widthCm / 2f
        val movingCy = moving.yCm + movingSpec.heightCm / 2f

        val xCandidates = mutableListOf<Candidate>()
        xCandidates.add(Candidate(wall.widthCm / 2f, movingCx))
        val yCandidates = mutableListOf<Candidate>()
        yCandidates.add(Candidate(eyeHeightCm, movingCy))

        others.forEach { pair ->
            val placed = pair.first
            val spec = pair.second
            xCandidates.add(Candidate(placed.xCm + spec.widthCm / 2f, movingCx))
            xCandidates.add(Candidate(placed.xCm, moving.xCm))
            xCandidates.add(Candidate(placed.xCm + spec.widthCm, moving.xCm + movingSpec.widthCm))
            yCandidates.add(Candidate(placed.yCm + spec.heightCm / 2f, movingCy))
            yCandidates.add(Candidate(placed.yCm, moving.yCm))
            yCandidates.add(Candidate(placed.yCm + spec.heightCm, moving.yCm + movingSpec.heightCm))
        }

        val bestX = xCandidates
            .filter { abs(it.target - it.current) <= toleranceCm }
            .minByOrNull { abs(it.target - it.current) }
        val bestY = yCandidates
            .filter { abs(it.target - it.current) <= toleranceCm }
            .minByOrNull { abs(it.target - it.current) }

        val guides = mutableListOf<SnapGuide>()
        var x = moving.xCm
        var y = moving.yCm
        if (bestX != null) {
            x += bestX.target - bestX.current
            guides.add(SnapGuide(SnapAxis.VERTICAL, bestX.target))
        }
        if (bestY != null) {
            y += bestY.target - bestY.current
            guides.add(SnapGuide(SnapAxis.HORIZONTAL, bestY.target))
        }
        return SnapResult(moving.copy(xCm = x, yCm = y), guides)
    }
}
