package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemCobblestone
import com.nami.world.item.ItemFurnace

object RecipeFurnace : Recipe("furnace") {

    override val ingredients = mapOf<Item, Int>(
        ItemCobblestone to 64
    )

    override val result = mapOf<Item, Int>(
        ItemFurnace to 1
    )

    override val duration = 15.0F

}