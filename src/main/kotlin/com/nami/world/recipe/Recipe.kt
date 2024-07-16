package com.nami.world.recipe

import com.nami.resources.recipe.ResourceRecipe
import com.nami.world.inventory.item.Item

class Recipe(
    id: String,
    val item: Item,
    val variants: List<RecipeVariant>
) : ResourceRecipe(id)
