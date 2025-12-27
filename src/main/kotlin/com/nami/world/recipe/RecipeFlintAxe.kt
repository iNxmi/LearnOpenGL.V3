package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemFlint
import com.nami.world.item.ItemFlintAxe
import com.nami.world.item.ItemStick

object RecipeFlintAxe : Recipe("flint_axe") {

    override val ingredients = mapOf<Item, Int>(
        ItemFlint to 3,
        ItemStick to 2
    )

    override val result = mapOf<Item, Int>(
        ItemFlintAxe to 1
    )

    override val duration = 5.0F

}