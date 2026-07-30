package com.nami.world.recipe.recipes

import com.nami.world.item.Item
import com.nami.world.item.items.ItemSnow
import com.nami.world.item.items.ItemSnowball
import com.nami.world.recipe.Recipe

object RecipeSnowball : Recipe("snowball") {

    override val ingredients = mapOf<Item, Int>(
        ItemSnow to 1
    )

    override val result = mapOf<Item, Int>(
        ItemSnowball to 16
    )

    override val duration = 1.0F

}