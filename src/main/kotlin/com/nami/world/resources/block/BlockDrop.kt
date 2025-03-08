package com.nami.world.resources.block

import com.nami.world.resources.item.Item
import kotlin.math.roundToInt

data class BlockDrop(
    val item: Item,
    val min: Int,
    val max: Int,
    val rate: Float
) {

    fun count(): Int {
        if (Math.random() > rate)
            return 0

        return (Math.random() * (max - min).toFloat() + min.toFloat()).roundToInt()
    }

}