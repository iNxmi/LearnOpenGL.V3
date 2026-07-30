package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemCobblestone
import com.nami.world.item.items.ItemFurnace
import com.nami.world.recipe.Recipe

object RecipeFurnace : Recipe("furnace") {

    override val ingredients = mapOf<Item, Int>(
        ItemCobblestone to 64
    )

    override val result = mapOf<Item, Int>(
        ItemFurnace to 1
    )

    override val duration = 15.0F

}