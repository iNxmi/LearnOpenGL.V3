package com.nami.world.recipe

import com.nami.world.item.Item

abstract class Recipe(val id: String) {

    companion object {
        val set = mutableSetOf<Recipe>()
        val map = set.associateBy { it.id }
        fun get(id: String) = map[id]
    }

    init {
        set.add(this)
    }

    abstract val ingredients: Map<Item, Int>
    abstract val result: Map<Item, Int>
    abstract val duration: Float
    open val success: Float = 1.0F

}