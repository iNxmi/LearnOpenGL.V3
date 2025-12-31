package com.nami.world.recipe

import com.nami.world.item.Item
import com.nami.world.recipe.recipes.*

abstract class Recipe(val id: String) {

    companion object {
        val set by lazy {
            setOf(
                RecipeBirchPlanks,
                RecipeBirchWorkbench,
                RecipeFlintAxe,
                RecipeFurnace,
                RecipeOakPlanks,
                RecipeOakWorkbench,
                RecipeSnowball
            )
        }
        val map by lazy { set.associateBy { it.id } }
        fun get(id: String) = map[id]
    }

    abstract val ingredients: Map<Item, Int>
    abstract val result: Map<Item, Int>
    abstract val duration: Float
    open val success: Float = 1.0F

}