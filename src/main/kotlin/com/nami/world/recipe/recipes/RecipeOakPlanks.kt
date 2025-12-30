package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemOakLog
import com.nami.world.item.items.ItemOakPlanks
import com.nami.world.recipe.Recipe

object RecipeOakPlanks : Recipe("oak_planks") {

    override val ingredients = mapOf<Item, Int>(
        ItemOakLog to 1
    )

    override val result = mapOf<Item, Int>(
        ItemOakPlanks to 4
    )

    override val duration = 1.0F

}