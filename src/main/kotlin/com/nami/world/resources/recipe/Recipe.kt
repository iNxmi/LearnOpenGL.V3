package com.nami.world.resources.recipe

import com.nami.resources.recipe.ResourceRecipe
import com.nami.world.resources.item.Item

class Recipe(
    id: String,
    val item: Item,
    val variants: Set<RecipeVariant>
) : ResourceRecipe(id)
