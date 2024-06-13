package com.nami.resources.item

import com.nami.resources.Resource
import com.nami.world.inventory.item.Item

enum class Items(val id: String) {

    LEAF("leaf"),
    ACORN("acorn"),
    STICK("stick"),
    HAND("hand");

    companion object {
        @JvmStatic
        fun get(id: String): Item {
            return Resource.ITEM.get(id)
        }

    }

    init {
        if (!Resource.ITEM.map.containsKey(id))
            throw IllegalStateException("Failed to create ITEM with id '$id'")
    }

    fun get(): Item {
        return Resource.ITEM.get(id)
    }

}