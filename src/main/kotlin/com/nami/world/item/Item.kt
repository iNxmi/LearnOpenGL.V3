package com.nami.world.item

abstract class Item(val id: String) {

    companion object {
        val set = mutableSetOf<Item>()
        val map = set.associateBy { it.id }

        fun get(id: String) = map[id]
    }

    init {
        set.add(this)
    }

    abstract val weight : Float
    open val tags: Set<String> = setOf()

    open fun onPrimaryUse(): Boolean = false
    open fun onSecondaryUse(): Boolean = false

}

