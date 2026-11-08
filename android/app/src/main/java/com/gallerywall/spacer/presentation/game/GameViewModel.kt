package com.gallerywall.spacer.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.PlanDraft
import com.gallerywall.spacer.domain.model.SavedWall
import com.gallerywall.spacer.domain.model.SnapGuide
import com.gallerywall.spacer.domain.repository.PlanRepository
import com.gallerywall.spacer.domain.repository.SettingsRepository
import com.gallerywall.spacer.domain.repository.WallRepository
import com.gallerywall.spacer.domain.usecase.ComputeNailPositionsUseCase
import com.gallerywall.spacer.domain.usecase.SnapFrameUseCase
import com.gallerywall.spacer.domain.usecase.SolveArrangementUseCase
import com.gallerywall.spacer.domain.usecase.ValidateArrangementUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private data class GameLocal(
    val selectedFrameId: Int = -1,
    val dragging: Boolean = false,
    val guides: List<SnapGuide> = emptyList()
)

sealed interface GameEvent {

    data object Snapped : GameEvent

    data class OpenResult(val planId: String) : GameEvent
}

class GameViewModel(
    private val planRepository: PlanRepository,
    private val wallRepository: WallRepository,
    private val settingsRepository: SettingsRepository,
    private val solveArrangement: SolveArrangementUseCase,
    private val computeNails: ComputeNailPositionsUseCase,
    private val validateArrangement: ValidateArrangementUseCase,
    private val snapFrame: SnapFrameUseCase
) : ViewModel() {

    private val local = MutableStateFlow(GameLocal())

    private val uiState = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = uiState.asStateFlow()

    private val events = Channel<GameEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    private var rawX = 0f
    private var rawY = 0f
    private var guideSignature = ""

    init {
        viewModelScope.launch {
            combine(planRepository.plan, local) { plan, transient -> build(plan, transient) }
                .collect { uiState.value = it }
        }
    }

    fun startDrag(frameId: Int) {
        ensurePlacements()
        val plan = planRepository.plan.value
        val placed = plan.placements.firstOrNull { it.frameId == frameId } ?: return
        rawX = placed.xCm
        rawY = placed.yCm
        guideSignature = ""
        local.value = local.value.copy(selectedFrameId = frameId, dragging = true)
    }

    fun drag(frameId: Int, deltaXCm: Float, deltaYCm: Float) {
        val plan = planRepository.plan.value
        val spec = plan.frames.firstOrNull { it.id == frameId } ?: return
        rawX = (rawX + deltaXCm).coerceIn(0f, (plan.wall.widthCm - spec.widthCm).coerceAtLeast(0f))
        rawY = (rawY + deltaYCm).coerceIn(0f, (plan.wall.heightCm - spec.heightCm).coerceAtLeast(0f))
        val others = plan.placements
            .filter { it.frameId != frameId }
            .mapNotNull { placed ->
                val other = plan.frames.firstOrNull { it.id == placed.frameId }
                if (other == null) null else placed to other
            }
        val result = snapFrame(
            moving = PlacedFrame(frameId, rawX, rawY),
            movingSpec = spec,
            others = others,
            wall = plan.wall,
            eyeHeightCm = plan.eyeHeightCm,
            toleranceCm = GameConfig.SNAP_TOLERANCE_CM
        )
        planRepository.update { current ->
            current.copy(
                placements = current.placements.map {
                    if (it.frameId == frameId) result.placed else it
                }
            )
        }
        val signature = result.guides.joinToString(",") { it.axis.name + it.positionCm.roundToInt() }
        if (signature.isNotEmpty() && signature != guideSignature) {
            viewModelScope.launch { events.send(GameEvent.Snapped) }
        }
        guideSignature = signature
        local.value = local.value.copy(guides = result.guides, dragging = true)
    }

    fun endDrag() {
        guideSignature = ""
        local.value = local.value.copy(dragging = false, guides = emptyList())
    }

    fun commitDrag() {
        if (local.value.dragging) {
            endDrag()
        }
    }

    fun recentre() {
        val eyeHeight = settingsRepository.settings.value.eyeHeightCm
        planRepository.update { plan ->
            plan.copy(
                eyeHeightCm = eyeHeight,
                placements = solveArrangement(
                    plan.wall,
                    plan.frames,
                    plan.layoutStyle,
                    plan.gapCm,
                    eyeHeight
                )
            )
        }
        local.value = GameLocal()
    }

    fun calculateNails() {
        ensurePlacements()
        val plan = planRepository.plan.value
        val nails = computeNails(plan.frames, plan.placements)
        if (nails.isEmpty()) return
        if (validateArrangement(plan.wall, plan.frames, plan.placements, nails) != ArrangementIssue.None) return
        val id = plan.savedWallId ?: ("plan-" + System.currentTimeMillis().toString())
        wallRepository.save(
            SavedWall(
                id = id,
                wall = plan.wall,
                frames = plan.frames,
                placements = plan.placements,
                nails = nails,
                layoutStyle = plan.layoutStyle,
                gapCm = plan.gapCm,
                eyeHeightCm = plan.eyeHeightCm
            )
        )
        planRepository.update { it.copy(savedWallId = id) }
        viewModelScope.launch { events.send(GameEvent.OpenResult(id)) }
    }

    private fun ensurePlacements() {
        planRepository.update { plan ->
            if (plan.placements.isNotEmpty()) {
                plan
            } else {
                plan.copy(
                    placements = solveArrangement(
                        plan.wall,
                        plan.frames,
                        plan.layoutStyle,
                        plan.gapCm,
                        plan.eyeHeightCm
                    )
                )
            }
        }
    }

    private fun build(plan: PlanDraft, transient: GameLocal): GameUiState {
        val placements = plan.placements.ifEmpty {
            solveArrangement(plan.wall, plan.frames, plan.layoutStyle, plan.gapCm, plan.eyeHeightCm)
        }
        val byId = plan.frames.associateBy { it.id }
        var left = plan.wall.widthCm
        var right = 0f
        placements.forEach { placed ->
            val frame = byId[placed.frameId]
            if (frame != null) {
                left = minOf(left, placed.xCm)
                right = maxOf(right, placed.xCm + frame.widthCm)
            }
        }
        val nails = computeNails(plan.frames, placements)
        val issue = validateArrangement(plan.wall, plan.frames, placements, nails)
        val invalidId = if (issue == ArrangementIssue.None) {
            NO_FRAME
        } else {
            validateArrangement.offendingFrameId(plan.wall, plan.frames, placements)
        }
        return GameUiState(
            wallWidthCm = plan.wall.widthCm,
            wallHeightCm = plan.wall.heightCm,
            furnitureTopCm = plan.wall.furnitureTopCm,
            eyeHeightCm = plan.eyeHeightCm,
            wallWidthLabel = plan.wall.widthCm.roundToInt(),
            wallHeightLabel = plan.wall.heightCm.roundToInt(),
            eyeHeightLabel = plan.eyeHeightCm.roundToInt(),
            frames = plan.frames,
            placements = placements,
            guides = transient.guides,
            selectedFrameId = transient.selectedFrameId,
            invalidFrameId = invalidId,
            dragging = transient.dragging,
            spreadLabel = (right - left).coerceAtLeast(0f).roundToInt(),
            gapLabel = plan.gapCm.roundToInt(),
            frameCount = plan.frames.size,
            issue = issue,
            canCalculate = issue == ArrangementIssue.None && placements.isNotEmpty()
        )
    }

    private companion object {
        const val NO_FRAME = -1
    }
}
