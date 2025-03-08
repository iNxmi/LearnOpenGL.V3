package com.nami.world.chunk

import com.nami.world.World
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.joml.Vector3i
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkSaver(
    val world: World,
    var numberOfThreads: Int
) {

    private val queue = ConcurrentLinkedQueue<Vector3i>()

    fun addToQueue(position: Vector3i) {
        if (!(0 until world.size.x).contains(position.x)) return
        if (!(0 until world.size.y).contains(position.y)) return
        if (!(0 until world.size.z).contains(position.z)) return

        if (queue.contains(position)) return

        queue.add(position)
    }

    private var jobs = mutableListOf<Job>()
    fun update() {
        jobs.removeAll { it.isCompleted }

        val chunkManager = world.chunkManager
        for (position in queue) {
            if (jobs.size >= numberOfThreads)
                break

            val chunk = chunkManager.getByChunkPosition(position) ?: continue
            jobs.add(GlobalScope.launch { chunk.save() })

            queue.remove(position)
        }
    }

}