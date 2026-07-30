package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemOakPlanks
import com.nami.world.item.items.ItemOakWorkbench
import com.nami.world.recipe.Recipe

object RecipeOakWorkbench : Recipe("oak_workbench") {

    override val ingredients = mapOf<Item, Int>(
        ItemOakPlanks to 4
    )

    override val result = mapOf<Item, Int>(
        ItemOakWorkbench to 1
    )

    override val duration = 1.0F

}