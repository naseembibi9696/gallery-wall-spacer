package com.gallerywall.spacer.data.sample

import com.gallerywall.spacer.core.config.GameConfig
import com.gallerywall.spacer.domain.model.FrameSpec
import com.gallerywall.spacer.domain.model.LayoutStyle
import com.gallerywall.spacer.domain.model.WallSpec

data class WallBlueprint(
    val id: String,
    val wall: WallSpec,
    val frames: List<FrameSpec>,
    val layoutStyle: LayoutStyle,
    val gapCm: Float
)

object SampleData {

    val defaultWall = WallSpec(
        name = "Hallway above the sideboard",
        widthCm = 320f,
        heightCm = 260f,
        furnitureTopCm = 78f
    )

    val defaultFrames = listOf(
        FrameSpec(id = 1, widthCm = 40f, heightCm = 50f, hangerDropCm = 4f, spriteIndex = 0),
        FrameSpec(id = 2, widthCm = 30f, heightCm = 40f, hangerDropCm = 3f, spriteIndex = 1),
        FrameSpec(id = 3, widthCm = 50f, heightCm = 50f, hangerDropCm = 5f, spriteIndex = 2),
        FrameSpec(id = 4, widthCm = 21f, heightCm = 30f, hangerDropCm = 2f, spriteIndex = 3),
        FrameSpec(id = 5, widthCm = 60f, heightCm = 40f, hangerDropCm = 6f, spriteIndex = 4)
    )

    val seedWalls = listOf(
        WallBlueprint(
            id = "hallway-sideboard",
            wall = defaultWall,
            frames = defaultFrames,
            layoutStyle = LayoutStyle.ROW,
            gapCm = GameConfig.DEFAULT_GAP_CM
        ),
        WallBlueprint(
            id = "stairwell-run",
            wall = WallSpec(
                name = "Stairwell run",
                widthCm = 240f,
                heightCm = 300f,
                furnitureTopCm = 0f
            ),
            frames = listOf(
                FrameSpec(id = 1, widthCm = 50f, heightCm = 70f, hangerDropCm = 5f, spriteIndex = 1),
                FrameSpec(id = 2, widthCm = 30f, heightCm = 40f, hangerDropCm = 3f, spriteIndex = 2),
                FrameSpec(id = 3, widthCm = 40f, heightCm = 40f, hangerDropCm = 4f, spriteIndex = 0),
                FrameSpec(id = 4, widthCm = 24f, heightCm = 30f, hangerDropCm = 2f, spriteIndex = 3)
            ),
            layoutStyle = LayoutStyle.CLUSTER,
            gapCm = 8f
        ),
        WallBlueprint(
            id = "bedroom-over-bed",
            wall = WallSpec(
                name = "Bedroom over the bed",
                widthCm = 180f,
                heightCm = 250f,
                furnitureTopCm = 62f
            ),
            frames = listOf(
                FrameSpec(id = 1, widthCm = 40f, heightCm = 40f, hangerDropCm = 4f, spriteIndex = 2),
                FrameSpec(id = 2, widthCm = 40f, heightCm = 40f, hangerDropCm = 4f, spriteIndex = 4),
                FrameSpec(id = 3, widthCm = 40f, heightCm = 40f, hangerDropCm = 4f, spriteIndex = 0),
                FrameSpec(id = 4, widthCm = 40f, heightCm = 40f, hangerDropCm = 4f, spriteIndex = 3)
            ),
            layoutStyle = LayoutStyle.GRID,
            gapCm = 5f
        )
    )
}
