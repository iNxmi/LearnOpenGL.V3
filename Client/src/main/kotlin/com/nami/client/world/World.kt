package com.nami.client.world

import com.nami.core.world.IChunk
import com.nami.core.world.IWorld

class World : IWorld {

    private val chunks = mutableMapOf<Long, Chunk>()

    override fun getChunks(): Map<Long, IChunk> = chunks

}