package com.nami.world.inventory

import com.nami.world.inventory.item.Item
import org.joml.Vector2i

class Inventory(val size: Vector2i) {

    val map = mutableMapOf<Item.Instance, Int>()
    var weight = 0f

    fun add(item: Item.Instance, count: Int): Int {
        if (count <= 0) return get(item)

        weight += item.template.weight * count
        val newCount = count + get(item)

        if (map.containsKey(item))
            map.remove(item)

        if (newCount > 0)
            map[item] = newCount

        return newCount
    }

    fun remove(item: Item.Instance, count: Int): Int {
        if (count <= 0) return get(item)

        weight -= item.template.weight * count
        val newCount = get(item) - count

        if (newCount <= 0) {
            map.remove(item)
            return 0
        }

        if (map.containsKey(item))
            map.remove(item)

        if (newCount > 0)
            map[item] = newCount

        return newCount
    }

    fun get(item: Item.Instance): Int {
        return map.getOrDefault(item, 0)
    }

}