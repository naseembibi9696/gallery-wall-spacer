package com.gallerywall.spacer.core.ui

import com.gallerywall.spacer.R

private val SPRITES = listOf(
    R.drawable.sprite_1,
    R.drawable.sprite_2,
    R.drawable.sprite_3,
    R.drawable.sprite_4,
    R.drawable.sprite_5
)

fun spriteFor(index: Int): Int = SPRITES[index.mod(SPRITES.size)]
