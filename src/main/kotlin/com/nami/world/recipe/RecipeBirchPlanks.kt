package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks

object RecipeBirchPlanks : Recipe("birch_planks") {

    override val ingredients = mapOf<Item, Int>(
        ItemBirchLog to 1
    )

    override val result = mapOf<Item, Int>(
        ItemBirchPlanks to 4
    )

    override val duration = 1.0F

}