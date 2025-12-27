package com.nami.world

import com.nami.world.item.Item

data class Drop(
    val items: Item,
    val amount: IntRange = 1..1,
    val probability: Float = 1.0f
)