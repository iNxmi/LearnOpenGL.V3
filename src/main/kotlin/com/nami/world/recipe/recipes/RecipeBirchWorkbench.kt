package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemBirchPlanks
import com.nami.world.item.items.ItemBirchWorkbench
import com.nami.world.recipe.Recipe

object RecipeBirchWorkbench : Recipe("birch_workbench") {

    override val ingredients = mapOf<Item, Int>(
        ItemBirchPlanks to 4
    )

    override val result = mapOf<Item, Int>(
        ItemBirchWorkbench to 1
    )

    override val duration = 1.0F

}