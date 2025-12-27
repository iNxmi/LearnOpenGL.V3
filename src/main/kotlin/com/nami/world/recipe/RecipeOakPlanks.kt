package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemOakLog
import com.nami.world.item.ItemOakPlanks

object RecipeOakPlanks : Recipe("oak_planks") {

    override val ingredients = mapOf<Item, Int>(
        ItemOakLog to 1
    )

    override val result = mapOf<Item, Int>(
        ItemOakPlanks to 4
    )

    override val duration = 1.0F

}