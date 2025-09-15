package com.nami.world.chunk

import com.nami.world.World
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.*

class ChunkGenerator(
    val world: World,
    var numberOfThreads: Int
) {

    private val log = KotlinLogging.logger {}

    private val queue = mutableSetOf<Vector3i>()
    private val finished = mutableSetOf<Vector3i>()

    fun addToQueue(position: Vector3i) {
        if (world.chunkManager.getByChunkPosition(position) != null) return

        if (!(0 until world.size.x).contains(position.x)) return
        if (!(0 until world.size.y).contains(position.y)) return
        if (!(0 until world.size.z).contains(position.z)) return

        if (finished.contains(position)) return
        if (queue.contains(position)) return

        queue.add(position)
    }

    private var jobs = mutableListOf<Job>()
    fun update() {
//        log.debug { "queue.size=${queue.size}" }

        jobs.removeAll { it.isCompleted }

        val positions = TreeMap<Float, Vector3i>()
        for (position in queue) {
            val distance = Vector3f(world.player.transform.position)
                .sub(
                    Vector3f(position)
                        .mul(Vector3f(Chunk.SIZE))
                        .add(
                            Vector3f(Chunk.SIZE)
                                .div(2f)
                        )
                ).length()

            positions[distance] = position
        }

        val chunkManager = world.chunkManager
        for ((distance, position) in positions) {
            if (jobs.size >= numberOfThreads)
                break

            jobs.add(GlobalScope.launch {
                val chunk = Chunk(world, position)
                chunkManager.setChunk(position, chunk)

                for (z in -1..1)
                    for (y in -1..1)
                        for (x in -1..1)
                            chunkManager.meshGenerator.addToQueue(Vector3i(position).add(x, y, z))
            })

            finished.add(position)
            queue.remove(position)
        }
    }

}