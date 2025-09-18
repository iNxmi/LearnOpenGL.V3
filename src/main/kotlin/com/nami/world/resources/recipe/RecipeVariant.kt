package com.nami.world.resources.recipe

import com.nami.world.material.Material
import com.nami.world.resources.item.Item

data class RecipeVariant(
    val workstations: Set<Material>?,
    val amount: Int,
    val duration: Float,
    val ingredients: Map<Item, Int>
)
