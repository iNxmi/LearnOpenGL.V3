package com.nami.world.block

import com.nami.world.inventory.item.Item
import kotlin.math.roundToInt

data class BlockDrop(
    val item: Item,
    val min: Int,
    val max: Int,
    val rate: Float
) {

    fun drop(): Int {
        if (Math.random() > rate)
            return 0

        return (Math.random() * (max - min).toFloat() + min.toFloat()).roundToInt()
    }

}