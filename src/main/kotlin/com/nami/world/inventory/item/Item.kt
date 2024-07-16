package com.nami.world.inventory.item

import com.nami.resources.item.ResourceItem

class Item(
    id: String,
    private val handlerClass: Class<ItemListener>,

    val weight: Float,
    val tags: List<String>?
) : ResourceItem(id) {

    fun create(): Instance {
        var handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler)
    }

    class Instance (
        val template: Item,
        val handler: ItemListener
    )

}