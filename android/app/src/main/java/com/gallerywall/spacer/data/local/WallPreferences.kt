package com.gallerywall.spacer.data.local

import android.content.Context
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.NailPosition
import com.gallerywall.spacer.domain.model.PlacedFrame
import com.gallerywall.spacer.domain.model.SavedWall
import com.gallerywall.spacer.domain.model.WallSpec

class WallPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun load(): List<SavedWall> {
        val raw = prefs.getString(KEY_WALLS, null).orEmpty()
        if (raw.isBlank()) return emptyList()
        return raw.split(RECORD).mapNotNull { decode(it) }
    }

    fun store(walls: List<SavedWall>) {
        prefs.edit().putString(KEY_WALLS, walls.joinToString(RECORD) { encode(it) }).apply()
    }

    fun isSeeded(): Boolean = prefs.getBoolean(KEY_SEEDED, false)

    fun markSeeded() {
        prefs.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    private fun encode(wall: SavedWall): String {
        val head = listOf(
            wall.id,
            wall.wall.name,
            wall.wall.widthCm.toString(),
            wall.wall.heightCm.toString(),
            wall.wall.furnitureTopCm.toString(),
            wall.layoutStyle.name,
            wall.gapCm.toString(),
            wall.eyeHeightCm.toString()
        ).joinToString(FIELD)
        val frames = wall.frames.joinToString(ITEM) { frame ->
            listOf(
                frame.id.toString(),
                frame.widthCm.toString(),
                frame.heightCm.toString(),
                frame.hangerDropCm.toString(),
                frame.spriteIndex.toString()
            ).joinToString(PART)
        }
        val placements = wall.placements.joinToString(ITEM) { placed ->
            listOf(
                placed.frameId.toString(),
                placed.xCm.toString(),
                placed.yCm.toString()
            ).joinToString(PART)
        }
        val nails = wall.nails.joinToString(ITEM) { nail ->
            listOf(
                nail.frameId.toString(),
                nail.index.toString(),
                nail.xCm.toString(),
                nail.yCm.toString(),
                if (nail.done) "1" else "0"
            ).joinToString(PART)
        }
        return listOf(head, frames, placements, nails).joinToString(SECTION)
    }

    private fun decode(raw: String): SavedWall? {
        val sections = raw.split(SECTION)
        if (sections.size < 4) return null
        val head = sections[0].split(FIELD)
        if (head.size < 8) return null
        val style = LayoutStyle.entries.firstOrNull { it.name == head[5] } ?: LayoutStyle.ROW
        val frames = sections[1].split(ITEM).mapNotNull { item ->
            val parts = item.split(PART)
            if (parts.size < 5) {
                null
            } else {
                FrameSpec(
                    id = parts[0].toIntOrNull() ?: return@mapNotNull null,
                    widthCm = parts[1].toFloatOrNull() ?: return@mapNotNull null,
                    heightCm = parts[2].toFloatOrNull() ?: return@mapNotNull null,
                    hangerDropCm = parts[3].toFloatOrNull() ?: return@mapNotNull null,
                    spriteIndex = parts[4].toIntOrNull() ?: 0
                )
            }
        }
        val placements = sections[2].split(ITEM).mapNotNull { item ->
            val parts = item.split(PART)
            if (parts.size < 3) {
                null
            } else {
                PlacedFrame(
                    frameId = parts[0].toIntOrNull() ?: return@mapNotNull null,
                    xCm = parts[1].toFloatOrNull() ?: return@mapNotNull null,
                    yCm = parts[2].toFloatOrNull() ?: return@mapNotNull null
                )
            }
        }
        val nails = sections[3].split(ITEM).mapNotNull { item ->
            val parts = item.split(PART)
            if (parts.size < 5) {
                null
            } else {
                NailPosition(
                    frameId = parts[0].toIntOrNull() ?: return@mapNotNull null,
                    index = parts[1].toIntOrNull() ?: return@mapNotNull null,
                    xCm = parts[2].toFloatOrNull() ?: return@mapNotNull null,
                    yCm = parts[3].toFloatOrNull() ?: return@mapNotNull null,
                    done = parts[4] == "1"
                )
            }
        }
        return SavedWall(
            id = head[0],
            wall = WallSpec(
                name = head[1],
                widthCm = head[2].toFloatOrNull() ?: return null,
                heightCm = head[3].toFloatOrNull() ?: return null,
                furnitureTopCm = head[4].toFloatOrNull() ?: 0f
            ),
            frames = frames,
            placements = placements,
            nails = nails,
            layoutStyle = style,
            gapCm = head[6].toFloatOrNull() ?: 6f,
            eyeHeightCm = head[7].toFloatOrNull() ?: 145f
        )
    }

    private companion object {
        const val FILE_NAME = "gallery_wall_walls"
        const val KEY_WALLS = "walls"
        const val KEY_SEEDED = "seeded"
        const val RECORD = "\n"
        const val SECTION = "#"
        const val FIELD = "|"
        const val ITEM = ";"
        const val PART = ":"
    }
}
