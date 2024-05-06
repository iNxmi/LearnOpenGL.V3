package com.nami.world

import com.nami.world.block.Block

class Inventory {

    val map = mutableMapOf<Block.Template, Int>()

    fun add(block: Block.Template, count: Int) {
        if (map[block] == null)
            map[block] = 0

        map[block] = map[block]!! + count
    }

    fun remove(block: Block.Template, count: Int) {
        if (map[block] == null)
            map[block] = 0

        if (count(block) >= count)
            map[block] = map[block]!! - count
    }

    fun count(block: Block.Template): Int {
        if (map[block] == null)
            return 0

        return map[block]!!
    }

}