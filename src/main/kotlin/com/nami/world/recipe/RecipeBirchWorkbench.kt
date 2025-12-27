package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemBirchWorkbench

object RecipeBirchWorkbench : Recipe("birch_workbench") {

    override val ingredients = mapOf<Item, Int>(
        ItemBirchPlanks to 4
    )

    override val result = mapOf<Item, Int>(
        ItemBirchWorkbench to 1
    )

    override val duration = 1.0F

}