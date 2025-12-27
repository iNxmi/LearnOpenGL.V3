package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemBirchWorkbench
import com.nami.world.item.ItemOakPlanks
import com.nami.world.item.ItemOakWorkbench

object RecipeOakWorkbench : Recipe("oak_workbench") {

    override val ingredients = mapOf<Item, Int>(
        ItemOakPlanks to 4
    )

    override val result = mapOf<Item, Int>(
        ItemOakWorkbench to 1
    )

    override val duration = 1.0F

}