package com.nami.server

import com.nami.core.Block
import com.nami.core.asID
import com.nami.core.world.IChunk
import com.nami.core.world.IChunk.Companion.SIZE
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3i

class Chunk(
    val position: Vector3i,
    val noise: JNoise
) : IChunk {

    //id 0..(size.x * size.y * size.z - 1)
    private val blocks = mutableMapOf<Long, Block>()

    init {
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val position = Vector3i(x, y, z)
                    val id = position.asID(SIZE)
//                    val value = noise.evaluateNoise(x.toDouble(), y.toDouble(), z.toDouble())

                    var block: Block? = null

                    if (y == 0)
                        block = Block.BEDROCK

                    if ((1..60).contains(y))
                        block = Block.STONE

                    if ((61..63).contains(y))
                        block = Block.DIRT

                    if (y == 64)
                        block = Block.GRASS


                    if (block != null)
                        blocks[id] = block
                }
    }

    override fun getBlocks(): Map<Long, Block> = blocks

}