package com.nami.world

import com.nami.resource.Resource
import com.nami.scene.SceneTime
import com.nami.world.block.BlockColor
import com.nami.world.block.BlockManager
import com.nami.world.chunk.Chunk
import com.nami.world.chunk.ChunkManager
import mu.KotlinLogging
import org.joml.*
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

    val noiseGenerators = WorldNoiseGenerators(seed)

    val blockManager = BlockManager(this)
    val chunkManager = ChunkManager(this)

    val player = Player(this)

    val chunkRadius = Vector3i(6)

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

                    if (!chunkManager.chunks.containsKey(pos))
                        chunkManager.generate(pos)

                    val chunk = chunkManager.getByChunkPosition(pos)
                    chunk?.update()
                }
        chunkManager.updateGeneration()
        chunkManager.updateSendToGPU()
    }

    var drawCalls = 0

    fun render(time: SceneTime) {
        drawCalls = 0

        val shaderTerrain = Resource.shader.get("chunk_terrain").bind()
        shaderTerrain.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderTerrain.uniform.set("u_specular_exponent", 8.0f)

        shaderTerrain.uniform.set("u_projection_matrix", player.camera.projection())
        shaderTerrain.uniform.set("u_view_matrix", player.camera.view())

        shaderTerrain.uniform.set("u_camera_position", player.transform.position)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_1D, BlockColor.texture)
        shaderTerrain.uniform.set("u_color_atlas", 0)

        val shaderFoliage = Resource.shader.get("chunk_foliage").bind()
        shaderFoliage.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFoliage.uniform.set("u_specular_exponent", 8.0f)

        shaderFoliage.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFoliage.uniform.set("u_view_matrix", player.camera.view())

        shaderFoliage.uniform.set("u_camera_position", player.transform.position)

        shaderFoliage.uniform.set("u_time", time.time)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_1D, BlockColor.texture)
        shaderFoliage.uniform.set("u_color_atlas", 0)

        val shaderFluid = Resource.shader.get("chunk_fluid").bind()
        shaderFluid.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFluid.uniform.set("u_specular_exponent", 8.0f)

        shaderFluid.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFluid.uniform.set("u_view_matrix", player.camera.view())

        shaderFluid.uniform.set("u_camera_position", player.transform.position)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_1D, BlockColor.texture)
        shaderFluid.uniform.set("u_color_atlas", 0)

        val chunks = TreeMap<Float, Chunk>()
        for (x in -chunkRadius.x until chunkRadius.x)
            for (y in -chunkRadius.y until chunkRadius.y)
                for (z in -chunkRadius.z until chunkRadius.z) {
                    val pos = Vector3i(
                        player.transform.position.x.toInt() / Chunk.SIZE.x + x,
                        player.transform.position.y.toInt() / Chunk.SIZE.y + y,
                        player.transform.position.z.toInt() / Chunk.SIZE.z + z
                    )

                    val chunk = chunkManager.getByChunkPosition(pos) ?: continue


                    val distance = Vector3f(pos).add(Vector3f(Chunk.SIZE).div(2.0f)).mul(Vector3f(Chunk.SIZE))
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

            if (chunk.meshTerrain.indicesCount > 0) {
                shaderTerrain.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(chunk.meshTerrain.vao)
                GL11.glDrawElements(GL_TRIANGLES, chunk.meshTerrain.indicesCount, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }

            if (chunk.meshFoliage.indicesCount > 0) {
                shaderFoliage.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(chunk.meshFoliage.vao)
                GL11.glDrawElements(GL_TRIANGLES, chunk.meshFoliage.indicesCount, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }
        }

        //Draw transparent stuff
        for ((_, chunk) in chunks) {
            if (chunk.meshFluid.indicesCount <= 0)
                continue

            shaderFluid.bind().uniform.set(
                "u_model_matrix",
                Matrix4f().translate(
                    (chunk.position.x * Chunk.SIZE.x).toFloat(),
                    (chunk.position.y * Chunk.SIZE.y).toFloat(),
                    (chunk.position.z * Chunk.SIZE.z).toFloat()
                )
            )

            shaderFluid.uniform.set("u_time", time.time)

            glBindVertexArray(chunk.meshFluid.vao)
            GL11.glDrawElements(GL_TRIANGLES, chunk.meshFluid.indicesCount, GL_UNSIGNED_INT, 0)
            glBindVertexArray(0)
        }

        Resource.shader.unbind()
    }

}