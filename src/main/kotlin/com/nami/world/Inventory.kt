package com.nami.world

import com.nami.world.block.BlockTemplate

class Inventory {

    val map = mutableMapOf<BlockTemplate, Int>()

    fun add(block: BlockTemplate, count: Int) {
        if (map[block] == null)
            map[block] = 0

        map[block] = map[block]!! + count
    }

    fun remove(block: BlockTemplate, count: Int) {
        if (map[block] == null)
            map[block] = 0

        if (count(block) >= count)
            map[block] = map[block]!! - count
    }

    fun count(block: BlockTemplate): Int {
        if (map[block] == null)
            return 0

        return map[block]!!
    }

}