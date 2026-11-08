package com.gallerywall.spacer.data.repository

import com.gallerywall.spacer.data.local.WallPreferences
import com.gallerywall.spacer.data.sample.SampleData
import com.gallerywall.spacer.domain.model.SavedWall
import com.gallerywall.spacer.domain.repository.WallRepository
import com.gallerywall.spacer.domain.usecase.ComputeNailPositionsUseCase
import com.gallerywall.spacer.domain.usecase.SolveArrangementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WallRepositoryImpl(
    private val preferences: WallPreferences,
    private val solveArrangement: SolveArrangementUseCase,
    private val computeNails: ComputeNailPositionsUseCase,
    private val defaultEyeHeightCm: Float
) : WallRepository {

    private val state = MutableStateFlow(initialWalls())

    override val walls: StateFlow<List<SavedWall>> = state.asStateFlow()

    override fun save(wall: SavedWall) {
        val updated = state.value.toMutableList()
        val index = updated.indexOfFirst { it.id == wall.id }
        if (index >= 0) {
            updated[index] = wall
        } else {
            updated.add(0, wall)
        }
        state.value = updated
        preferences.store(updated)
    }

    override fun byId(id: String): SavedWall? = state.value.firstOrNull { it.id == id }

    private fun initialWalls(): List<SavedWall> {
        val stored = preferences.load()
        if (stored.isNotEmpty() || preferences.isSeeded()) return stored
        val seeded = SampleData.seedWalls.map { blueprint ->
            val placements = solveArrangement(
                blueprint.wall,
                blueprint.frames,
                blueprint.layoutStyle,
                blueprint.gapCm,
                defaultEyeHeightCm
            )
            SavedWall(
                id = blueprint.id,
                wall = blueprint.wall,
                frames = blueprint.frames,
                placements = placements,
                nails = computeNails(blueprint.frames, placements),
                layoutStyle = blueprint.layoutStyle,
                gapCm = blueprint.gapCm,
                eyeHeightCm = defaultEyeHeightCm
            )
        }
        preferences.store(seeded)
        preferences.markSeeded()
        return seeded
    }
}
