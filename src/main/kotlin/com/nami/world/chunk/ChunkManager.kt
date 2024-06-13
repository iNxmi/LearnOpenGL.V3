package com.nami.world.chunk

import com.nami.world.World
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkManager(val world: World) {

    companion object {
        const val CHUNK_GENERATION_THREADS = 8
    }

    private val generationQueue = ConcurrentLinkedQueue<Vector3i>()
    private val updateMeshQueue = ConcurrentLinkedQueue<Runnable>()
    val chunks = mutableMapOf<Vector3i, Chunk?>()

    fun generate(position: Vector3i) {
        if (!(0 until World.SIZE.x).contains(position.x) || !(0 until World.SIZE.y).contains(position.y) || !(0 until World.SIZE.z).contains(
                position.z
            )
        )
            chunks[position] = null

        if (!chunks.containsKey(position) && !generationQueue.contains(position))
            generationQueue.add(position)
    }

    fun sendToGPU(position: Vector3i, runnable: Runnable) {
        updateMeshQueue.add(runnable)
    }

    fun getByChunkPosition(position: Vector3i): Chunk? {
        return chunks[position]
    }

    fun getByBlockPosition(position: Vector3i): Chunk? {
        return getByChunkPosition(
            Vector3i(
                position.x / Chunk.SIZE.x,
                position.y / Chunk.SIZE.y,
                position.z / Chunk.SIZE.z
            )
        )
    }

    private var jobs = MutableList<Job?>(CHUNK_GENERATION_THREADS) { null }
    fun updateGeneration() {
        if (generationQueue.isEmpty())
            return

        for (i in jobs.indices) {
            if (generationQueue.isEmpty())
                break

            val job = jobs[i]
            if (job != null && !job.isCompleted)
                continue

            var pair: Pair<Vector3i, Float>? = null
            for (position in generationQueue) {

                val distance =
                    Vector3f(world.player.transform.position).sub(
                        Vector3f(position).mul(Vector3f(Chunk.SIZE)).add(Vector3f(Chunk.SIZE).mul(0.5f))
                    )
                        .length()

                if (distance > 200) {
                    generationQueue.remove(position)
                    continue
                }

                val p = Pair(position, distance)

                if (pair == null || pair.second > p.second)
                    pair = p
            }

            if (pair == null)
                break

            val position = pair.first
            generationQueue.remove(position)

            jobs[i] = GlobalScope.launch {
                val chunk = Chunk(world, Vector3i(pair.first))
                chunks[pair.first] = chunk

                for (z in -1..1)
                    for (y in -1..1)
                        for (x in -1..1)
                            getByChunkPosition(Vector3i(pair.first).add(x, y, z))?.updateMesh()
            }
        }
    }

    fun updateSendToGPU() {
        for (i in updateMeshQueue.indices)
            updateMeshQueue.remove().run()
    }


}