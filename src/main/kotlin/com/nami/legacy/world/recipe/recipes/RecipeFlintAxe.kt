package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemFlint
import com.nami.world.item.items.ItemFlintAxe
import com.nami.world.item.items.ItemStick
import com.nami.world.recipe.Recipe

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