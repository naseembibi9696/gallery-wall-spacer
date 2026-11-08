package com.gallerywall.spacer.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.domain.model.ArrangementIssue
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.PlanDraft
import com.gallerywall.spacer.domain.repository.PlanRepository
import com.gallerywall.spacer.domain.usecase.ComputeNailPositionsUseCase
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

private data class SetupLocal(
    val focused: MeasureTarget = MeasureTarget.WIDTH,
    val selectedFrameId: Int = 0,
    val dialogVisible: Boolean = false,
    val draftWidthCm: Int = 30,
    val draftHeightCm: Int = 40,
    val draftDropCm: Int = 3
)

sealed interface SetupEvent {

    data object OpenGame : SetupEvent
}

class SetupViewModel(
    private val planRepository: PlanRepository,
    private val solveArrangement: SolveArrangementUseCase,
    private val computeNails: ComputeNailPositionsUseCase,
    private val validateArrangement: ValidateArrangementUseCase
) : ViewModel() {

    private val local = MutableStateFlow(SetupLocal())

    private val uiState = MutableStateFlow(SetupUiState())
    val state: StateFlow<SetupUiState> = uiState.asStateFlow()

    private val events = Channel<SetupEvent>(Channel.BUFFERED)
    val eventFlow = events.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(planRepository.plan, local) { plan, transient -> build(plan, transient) }
                .collect { uiState.value = it }
        }
    }

    fun focus(target: MeasureTarget) {
        local.value = local.value.copy(focused = target)
    }

    fun adjustMeasure(step: Int) {
        val delta = step * GameConfig.MEASURE_STEP_CM
        planRepository.update { plan ->
            val wall = when (local.value.focused) {
                MeasureTarget.WIDTH -> plan.wall.copy(
                    widthCm = (plan.wall.widthCm + delta)
                        .coerceIn(GameConfig.MIN_WALL_WIDTH_CM, GameConfig.MAX_WALL_WIDTH_CM)
                )

                MeasureTarget.HEIGHT -> plan.wall.copy(
                    heightCm = (plan.wall.heightCm + delta)
                        .coerceIn(GameConfig.MIN_WALL_HEIGHT_CM, GameConfig.MAX_WALL_HEIGHT_CM)
                )

                MeasureTarget.FURNITURE -> plan.wall.copy(
                    furnitureTopCm = (plan.wall.furnitureTopCm + delta)
                        .coerceIn(GameConfig.MIN_FURNITURE_CM, GameConfig.MAX_FURNITURE_CM)
                )
            }
            plan.copy(wall = wall)
        }
    }

    fun selectFrame(id: Int) {
        local.value = local.value.copy(selectedFrameId = id)
    }

    fun adjustFrameSize(step: Int) {
        val delta = step * GameConfig.FRAME_STEP_CM
        val id = currentFrameId()
        planRepository.update { plan ->
            plan.copy(
                frames = plan.frames.map { frame ->
                    if (frame.id == id) {
                        frame.copy(
                            widthCm = (frame.widthCm + delta)
                                .coerceIn(GameConfig.MIN_FRAME_CM, GameConfig.MAX_FRAME_CM),
                            heightCm = (frame.heightCm + delta)
                                .coerceIn(GameConfig.MIN_FRAME_CM, GameConfig.MAX_FRAME_CM)
                        )
                    } else {
                        frame
                    }
                }
            )
        }
    }

    fun adjustFrameDrop(step: Int) {
        val delta = step * GameConfig.FRAME_STEP_CM
        val id = currentFrameId()
        planRepository.update { plan ->
            plan.copy(
                frames = plan.frames.map { frame ->
                    if (frame.id == id) {
                        frame.copy(
                            hangerDropCm = (frame.hangerDropCm + delta)
                                .coerceIn(GameConfig.MIN_DROP_CM, GameConfig.MAX_DROP_CM)
                        )
                    } else {
                        frame
                    }
                }
            )
        }
    }

    fun removeSelectedFrame() {
        val id = currentFrameId()
        planRepository.update { plan ->
            if (plan.frames.size <= 1) {
                plan
            } else {
                plan.copy(frames = plan.frames.filterNot { it.id == id })
            }
        }
        local.value = local.value.copy(selectedFrameId = 0)
    }

    fun openAddFrameDialog() {
        local.value = local.value.copy(dialogVisible = true)
    }

    fun dismissAddFrameDialog() {
        local.value = local.value.copy(dialogVisible = false)
    }

    fun adjustDraftWidth(step: Int) {
        local.value = local.value.copy(
            draftWidthCm = (local.value.draftWidthCm + step)
                .coerceIn(GameConfig.MIN_FRAME_CM.toInt(), GameConfig.MAX_FRAME_CM.toInt())
        )
    }

    fun adjustDraftHeight(step: Int) {
        local.value = local.value.copy(
            draftHeightCm = (local.value.draftHeightCm + step)
                .coerceIn(GameConfig.MIN_FRAME_CM.toInt(), GameConfig.MAX_FRAME_CM.toInt())
        )
    }

    fun adjustDraftDrop(step: Int) {
        local.value = local.value.copy(
            draftDropCm = (local.value.draftDropCm + step)
                .coerceIn(GameConfig.MIN_DROP_CM.toInt(), GameConfig.MAX_DROP_CM.toInt())
        )
    }

    fun confirmAddFrame() {
        val transient = local.value
        planRepository.update { plan ->
            val nextId = (plan.frames.maxOfOrNull { it.id } ?: 0) + 1
            plan.copy(
                frames = plan.frames + FrameSpec(
                    id = nextId,
                    widthCm = transient.draftWidthCm.toFloat(),
                    heightCm = transient.draftHeightCm.toFloat(),
                    hangerDropCm = transient.draftDropCm.toFloat(),
                    spriteIndex = plan.frames.size % SPRITE_COUNT
                )
            )
        }
        local.value = transient.copy(dialogVisible = false)
    }

    fun setLayoutStyle(style: LayoutStyle) {
        planRepository.update { it.copy(layoutStyle = style) }
    }

    fun adjustGap(step: Int) {
        val delta = step * GameConfig.GAP_STEP_CM
        planRepository.update { plan ->
            plan.copy(
                gapCm = (plan.gapCm + delta)
                    .coerceIn(GameConfig.MIN_GAP_CM, GameConfig.MAX_GAP_CM)
            )
        }
    }

    fun placeFrames() {
        if (!uiState.value.canPlace) return
        planRepository.update { plan ->
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
        viewModelScope.launch { events.send(SetupEvent.OpenGame) }
    }

    private fun currentFrameId(): Int {
        val selected = local.value.selectedFrameId
        val plan = planRepository.plan.value
        return if (plan.frames.any { it.id == selected }) {
            selected
        } else {
            plan.frames.firstOrNull()?.id ?: 0
        }
    }

    private fun build(plan: PlanDraft, transient: SetupLocal): SetupUiState {
        val selectedId = if (plan.frames.any { it.id == transient.selectedFrameId }) {
            transient.selectedFrameId
        } else {
            plan.frames.firstOrNull()?.id ?: 0
        }
        val selected = plan.frames.firstOrNull { it.id == selectedId }
        val placements = solveArrangement(
            plan.wall,
            plan.frames,
            plan.layoutStyle,
            plan.gapCm,
            plan.eyeHeightCm
        )
        val nails = computeNails(plan.frames, placements)
        val issue = validateArrangement(plan.wall, plan.frames, placements, nails)
        return SetupUiState(
            widthCm = plan.wall.widthCm.roundToInt(),
            heightCm = plan.wall.heightCm.roundToInt(),
            furnitureTopCm = plan.wall.furnitureTopCm.roundToInt(),
            focused = transient.focused,
            frames = plan.frames.map { frame ->
                FrameChipItem(
                    id = frame.id,
                    widthCm = frame.widthCm.roundToInt(),
                    heightCm = frame.heightCm.roundToInt(),
                    dropCm = frame.hangerDropCm.roundToInt(),
                    spriteIndex = frame.spriteIndex,
                    selected = frame.id == selectedId
                )
            },
            selectedFrameId = selectedId,
            selectedWidthCm = selected?.widthCm?.roundToInt() ?: 0,
            selectedHeightCm = selected?.heightCm?.roundToInt() ?: 0,
            selectedDropCm = selected?.hangerDropCm?.roundToInt() ?: 0,
            layoutStyle = plan.layoutStyle,
            gapCm = plan.gapCm.roundToInt(),
            canRemoveFrame = plan.frames.size > 1,
            issue = issue,
            canPlace = issue == ArrangementIssue.None && plan.frames.isNotEmpty(),
            dialogVisible = transient.dialogVisible,
            draftWidthCm = transient.draftWidthCm,
            draftHeightCm = transient.draftHeightCm,
            draftDropCm = transient.draftDropCm
        )
    }

    private companion object {
        const val SPRITE_COUNT = 5
    }
}
