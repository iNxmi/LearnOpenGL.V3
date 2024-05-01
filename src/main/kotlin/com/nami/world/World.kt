package com.nami.world

import com.nami.resource.Resource
import com.nami.scene.SceneTime
import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import com.nami.world.chunk.Chunk
import com.nami.world.chunk.ChunkManager
import mu.KotlinLogging
import org.joml.*
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL33.*
import java.util.*

class World(val seed: Long) {
    val log = KotlinLogging.logger { }

    companion object {
        @JvmStatic
        val SIZE = Vector3i(512, 32, 512)
        const val WATER_LEVEL = 64
    }

    val chunkManager = ChunkManager(this)

    val player = Player(this)

    val chunkRadius = Vector3i(5)

    init {
        log.debug { "World Seed: $seed" }

//        player.transform.position.set(
//            (SIZE.x / 2 * Chunk.SIZE.x).toDouble(),
//            0.0,
//            (SIZE.z / 2 * Chunk.SIZE.z).toDouble()
//        )

//        for (x in 0 until width)
//            for (y in 0 until height)
//                for (z in 0 until depth) {
//                    log.debug { Vector3i(x, y, z) }
//                    chunkManager.generate(Vector3i(x, y, z))
//                }
    }

    fun update(time: SceneTime) {
        val color = Vector3f(0.45f, 0.84f, 1f).mul(0.85f)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update(time)

        for (x in -chunkRadius.x until chunkRadius.x)
            for (y in -chunkRadius.y until chunkRadius.y)
                for (z in -chunkRadius.z until chunkRadius.z) {
                    val pos = Vector3i(
                        player.transform.position.x.toInt() / Chunk.SIZE.x + x,
                        player.transform.position.y.toInt() / Chunk.SIZE.y + y,
                        player.transform.position.z.toInt() / Chunk.SIZE.z + z
                    )

                    val chunk = chunkManager.get(pos) ?: chunkManager.generate(pos)
                    chunk?.update()
                }
    }

    var drawCalls = 0

    private val chunks = TreeMap<Double, Chunk>()
    fun render() {
        drawCalls = 0

        val shaderTerrain = Resource.shader.get("chunk_terrain").bind()
        shaderTerrain.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderTerrain.uniform.set("u_specular_exponent", 8.0f)

        shaderTerrain.uniform.set("u_projection_matrix", player.camera.projection())
        shaderTerrain.uniform.set("u_view_matrix", player.camera.view())

        shaderTerrain.uniform.set("u_camera_position", player.transform.position)

        val shaderFoliage = Resource.shader.get("chunk_foliage").bind()
        shaderFoliage.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFoliage.uniform.set("u_specular_exponent", 8.0f)

        shaderFoliage.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFoliage.uniform.set("u_view_matrix", player.camera.view())

        shaderFoliage.uniform.set("u_camera_position", player.transform.position)

        shaderFoliage.uniform.set("u_time", glfwGetTime().toFloat())

        val shaderFluid = Resource.shader.get("chunk_fluid").bind()
        shaderFluid.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFluid.uniform.set("u_specular_exponent", 8.0f)

        shaderFluid.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFluid.uniform.set("u_view_matrix", player.camera.view())

        shaderFluid.uniform.set("u_camera_position", player.transform.position)

        chunks.clear()
        for (x in -chunkRadius.x until chunkRadius.x)
            for (y in -chunkRadius.y until chunkRadius.y)
                for (z in -chunkRadius.z until chunkRadius.z) {
                    val pos = Vector3i(
                        player.transform.position.x.toInt() / Chunk.SIZE.x + x,
                        player.transform.position.y.toInt() / Chunk.SIZE.y + y,
                        player.transform.position.z.toInt() / Chunk.SIZE.z + z
                    )

                    val chunk = chunkManager.get(pos) ?: continue

                    val distance = Vector3d(pos).add(Vector3f(Chunk.SIZE).div(2.0f)).mul(Vector3d(Chunk.SIZE))
                        .sub(player.transform.position).length()
                    chunks[distance] = chunk
                }

        //Draw solid stuff
        for ((_, chunk) in chunks) {
            val model = Matrix4f().translate(
                (chunk.position.x * Chunk.SIZE.x).toFloat(),
                (chunk.position.y * Chunk.SIZE.y).toFloat(),
                (chunk.position.z * Chunk.SIZE.z).toFloat()
            )

            if (chunk.meshTerrain.indices > 0) {
                shaderTerrain.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(chunk.meshTerrain.vao)
                GL11.glDrawElements(GL_TRIANGLES, chunk.meshTerrain.indices, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }

            if (chunk.meshFoliage.indices > 0) {
                shaderFoliage.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(chunk.meshFoliage.vao)
                GL11.glDrawElements(GL_TRIANGLES, chunk.meshFoliage.indices, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }
        }

        //Draw transparent stuff
        for ((_, chunk) in chunks) {
            if (chunk.meshFluid.indices <= 0)
                continue

            shaderFluid.bind().uniform.set(
                "u_model_matrix",
                Matrix4f().translate(
                    (chunk.position.x * Chunk.SIZE.x).toFloat(),
                    (chunk.position.y * Chunk.SIZE.y).toFloat(),
                    (chunk.position.z * Chunk.SIZE.z).toFloat()
                )
            )

            glBindVertexArray(chunk.meshFluid.vao)
            GL11.glDrawElements(GL_TRIANGLES, chunk.meshFluid.indices, GL_UNSIGNED_INT, 0)
            glBindVertexArray(0)
        }

        Resource.shader.unbind()
    }

    fun getChunkRelativePosition(position: Vector3i): Vector3i {
        return Vector3i(position).sub(getChunkPosition(position).mul(Chunk.SIZE))
    }

    fun getChunkPosition(position: Vector3i): Vector3i {
        return Vector3i(position.x / Chunk.SIZE.x, position.y / Chunk.SIZE.y, position.z / Chunk.SIZE.z)
    }

    fun getBlock(position: Vector3i): Block? {
        val chunk = chunkManager.get(getChunkPosition(position)) ?: return null
        return chunk.getBlock(getChunkRelativePosition(position))
    }

    fun setBlock(position: Vector3i, block: BlockTemplate?): Boolean {
        val chunk = chunkManager.get(getChunkPosition(position)) ?: return false
        chunk.setBlock(getChunkRelativePosition(position), block)
        return true
    }

    fun getHeight(pos: Vector2i, startHeight: Int): Int {
        for (y in startHeight downTo 0)
            if (getBlock(Vector3i(pos.x, y, pos.y)) != null)
                return y

        return 0
    }

    fun getBiome(position: Vector3i): Biome? {
        val chunk = chunkManager.get(getChunkPosition(position)) ?: return null

        val chunkRelativePosition = getChunkRelativePosition(position)
        return chunk.getBiome(Vector2i(chunkRelativePosition.x, chunkRelativePosition.z))
    }

}