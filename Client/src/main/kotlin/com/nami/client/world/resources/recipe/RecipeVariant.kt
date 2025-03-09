package com.nami.client.world.resources.recipe

import com.nami.client.world.resources.block.Block
import com.nami.client.world.resources.item.Item

data class RecipeVariant(
    val workstations: Set<Block>?,
    val amount: Int,
    val duration: Float,
    val ingredients: Map<Item, Int>
)
