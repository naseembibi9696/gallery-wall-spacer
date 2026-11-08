package com.gallerywall.spacer.data.repository

import com.gallerywall.spacer.data.sample.SampleData
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.PlanDraft
import com.gallerywall.spacer.domain.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlanRepositoryImpl(
    defaultGapCm: Float,
    defaultEyeHeightCm: Float
) : PlanRepository {

    private val state = MutableStateFlow(
        PlanDraft(
            savedWallId = null,
            wall = SampleData.defaultWall,
            frames = SampleData.defaultFrames,
            placements = emptyList(),
            layoutStyle = LayoutStyle.ROW,
            gapCm = defaultGapCm,
            eyeHeightCm = defaultEyeHeightCm
        )
    )

    override val plan: StateFlow<PlanDraft> = state.asStateFlow()

    override fun update(transform: (PlanDraft) -> PlanDraft) {
        state.update(transform)
    }
}
