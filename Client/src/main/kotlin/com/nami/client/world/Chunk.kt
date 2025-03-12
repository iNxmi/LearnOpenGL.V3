package com.nami.client.world

import com.nami.core.Block
import com.nami.core.world.IChunk

class Chunk(
    private val blocks: Map<Long, Block> = mutableMapOf()
) : IChunk {

    override fun getBlocks(): Map<Long, Block> = blocks

}