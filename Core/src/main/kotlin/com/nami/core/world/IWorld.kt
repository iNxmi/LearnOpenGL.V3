package com.nami.core.world

interface IWorld {

    fun getChunks(): Map<Long, IChunk>

}