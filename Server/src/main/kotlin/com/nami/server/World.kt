package com.nami.server

import com.nami.core.Time
import com.nami.core.asID
import com.nami.core.world.IChunk
import com.nami.core.world.IWorld
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3i

class World(
    val size: Vector3i,
    val seed: Long,
    val waterLevel: Int
) : IWorld {

    val time = Time()

    private val chunks = mutableMapOf<Long, Chunk>()

    val noise = JNoise.newBuilder()
        .perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY)
        .build()

    init {
        for (z in 0 until size.z)
            for (y in 0 until size.y)
                for (x in 0 until size.x) {
                    val position = Vector3i(x, y, z)
                    chunks[position.asID(size)] = Chunk(position, noise)
                }
    }

    override fun getChunks(): Map<Long, IChunk> = chunks
    fun getChunk(position: Vector3i): IChunk = getChunk(position.asID(size))
    fun getChunk(id: Long): IChunk = getChunks()[id]!!

    fun update(delta: Float) {
        time.update(delta)
    }

}