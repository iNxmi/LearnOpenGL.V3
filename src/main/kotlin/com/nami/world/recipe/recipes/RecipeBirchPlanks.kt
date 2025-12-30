package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemBirchLog
import com.nami.world.item.items.ItemBirchPlanks
import com.nami.world.recipe.Recipe

object RecipeBirchPlanks : Recipe("birch_planks") {

    override val ingredients = mapOf<Item, Int>(
        ItemBirchLog to 1
    )

    override val result = mapOf<Item, Int>(
        ItemBirchPlanks to 4
    )

    override val duration = 1.0F

}