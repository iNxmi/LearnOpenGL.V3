package com.nami.world.chunk

import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW.glfwGetTime
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkMeshGenerator(
    val world: World,
    val manager: ChunkManager,
    val maxTimePerUpdateInMS: Float
) {

    private val queue = ConcurrentLinkedQueue<Vector3i>()

    fun addToQueue(position: Vector3i) {
        if (world.chunkManager.getByChunkPosition(position) == null) return

        if (!(0 until world.size.x).contains(position.x)) return
        if (!(0 until world.size.y).contains(position.y)) return
        if (!(0 until world.size.z).contains(position.z)) return

        if (queue.contains(position)) return

        queue.add(position)
    }

    fun update() {
        val positions = TreeMap<Float, Vector3i>()
        for (position in queue) {
            val distance =
                Vector3f(world.player.transform.position).sub(
                    Vector3f(position).mul(Vector3f(Chunk.SIZE)).add(Vector3f(Chunk.SIZE).div(2f))
                ).length()

            positions[distance] = position
        }

        val startTime = glfwGetTime()
        for ((_, position) in positions) {
            if ((glfwGetTime() - startTime) * 1000.0f >= maxTimePerUpdateInMS)
                break

//            GlobalScope.launch {
                manager.getByChunkPosition(position)?.generateMesh()
                manager.getByChunkPosition(position)?.uploadMesh()
                queue.remove(position)
//            }
        }
    }

}