package com.nami.world.resources.recipe

import com.nami.world.resources.block.Block
import com.nami.world.resources.item.Item

data class RecipeVariant(
    val workstations: Set<Block>?,
    val amount: Int,
    val duration: Float,
    val ingredients: Map<Item, Int>
)
