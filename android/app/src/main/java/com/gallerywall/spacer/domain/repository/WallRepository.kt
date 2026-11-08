package com.gallerywall.spacer.domain.repository

import com.gallerywall.spacer.domain.model.SavedWall
import kotlinx.coroutines.flow.StateFlow

interface WallRepository {

    val walls: StateFlow<List<SavedWall>>

    fun save(wall: SavedWall)

    fun byId(id: String): SavedWall?
}
