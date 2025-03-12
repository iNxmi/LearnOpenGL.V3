package com.nami.core.world

import com.nami.core.Block
import org.joml.Vector3i

interface IChunk {

    companion object {
        val SIZE = Vector3i(16, 16, 16)
    }

    fun getBlocks(): Map<Long, Block>

}