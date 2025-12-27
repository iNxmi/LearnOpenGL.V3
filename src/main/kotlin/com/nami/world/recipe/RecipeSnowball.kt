package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.item.ItemBirchLog
import com.nami.world.item.ItemBirchPlanks
import com.nami.world.item.ItemSnow
import com.nami.world.item.ItemSnowball

object RecipeSnowball : Recipe("snowball") {

    override val ingredients = mapOf<Item, Int>(
        ItemSnow to 1
    )

    override val result = mapOf<Item, Int>(
        ItemSnowball to 16
    )

    override val duration = 1.0F

}