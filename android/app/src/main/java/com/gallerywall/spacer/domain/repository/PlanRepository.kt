package com.gallerywall.spacer.domain.repository

import com.gallerywall.spacer.domain.model.PlanDraft
import kotlinx.coroutines.flow.StateFlow

interface PlanRepository {

    val plan: StateFlow<PlanDraft>

    fun update(transform: (PlanDraft) -> PlanDraft)
}
