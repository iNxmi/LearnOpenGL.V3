package com.nami.world

import com.nami.resources.Resource
import com.nami.resources.texture.TextureAtlas
import com.nami.scene.SceneTime
import com.nami.world.biome.BiomeManager
import com.nami.world.block.Block
import com.nami.world.block.BlockManager
import com.nami.world.chunk.Chunk
import com.nami.world.chunk.ChunkManager
import com.nami.world.player.Player
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i
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

    val biomeManager = BiomeManager(this)
    val blockManager = BlockManager(this)
    val chunkManager = ChunkManager(this)

    val player = Player(this)

    val chunkRadius = Vector3i(6)

    fun update(time: SceneTime) {
        val color = Vector3f(52f / 255f, 146f / 255f, 235f / 255f).mul(1f)
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

    fun render(time: SceneTime) {
        val shaderTerrain = Resource.SHADER.get("chunk_terrain").bind()
        shaderTerrain.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderTerrain.uniform.set("u_specular_exponent", 8.0f)

        shaderTerrain.uniform.set("u_projection_matrix", player.camera.projection())
        shaderTerrain.uniform.set("u_view_matrix", player.camera.view())

        shaderTerrain.uniform.set("u_camera_position", player.transform.position)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
        shaderTerrain.uniform.set("u_texture_diffuse", 0)

        val shaderFoliage = Resource.SHADER.get("chunk_foliage").bind()
        shaderFoliage.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFoliage.uniform.set("u_specular_exponent", 8.0f)

        shaderFoliage.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFoliage.uniform.set("u_view_matrix", player.camera.view())

        shaderFoliage.uniform.set("u_camera_position", player.transform.position)

        shaderFoliage.uniform.set("u_time", time.time)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
        shaderFoliage.uniform.set("u_texture_diffuse", 0)

        val shaderFluid = Resource.SHADER.get("chunk_fluid").bind()
        shaderFluid.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shaderFluid.uniform.set("u_specular_exponent", 8.0f)

        shaderFluid.uniform.set("u_projection_matrix", player.camera.projection())
        shaderFluid.uniform.set("u_view_matrix", player.camera.view())

        shaderFluid.uniform.set("u_camera_position", player.transform.position)

        shaderFluid.uniform.set("u_time", time.time)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
        shaderFluid.uniform.set("u_texture_diffuse", 0)

        val chunks = TreeMap<Float, Chunk>()
        for (x in -chunkRadius.x until chunkRadius.x)
            for (y in -chunkRadius.y until chunkRadius.y)
                for (z in -chunkRadius.z until chunkRadius.z)
                    if (x * x + y * y + z * z <= chunkRadius.x * chunkRadius.y * chunkRadius.z) {
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

            var mesh = chunk.meshes[Block.Layer.SOLID]!!
            if (mesh.indicesCount > 0) {
                shaderTerrain.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(mesh.vao)
                GL11.glDrawElements(GL_TRIANGLES, mesh.indicesCount, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }
        }

        //Draw transparent stuff
        for ((_, chunk) in chunks) {
            val model = Matrix4f().translate(
                (chunk.position.x * Chunk.SIZE.x).toFloat(),
                (chunk.position.y * Chunk.SIZE.y).toFloat(),
                (chunk.position.z * Chunk.SIZE.z).toFloat()
            )

            var mesh = chunk.meshes[Block.Layer.FLUID]!!
            if (mesh.indicesCount > 0) {
                shaderFluid.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(mesh.vao)
                GL11.glDrawElements(GL_TRIANGLES, mesh.indicesCount, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }

            mesh = chunk.meshes[Block.Layer.FOLIAGE]!!
            if (mesh.indicesCount > 0) {
                shaderFoliage.bind().uniform.set("u_model_matrix", model)

                glBindVertexArray(mesh.vao)
                GL11.glDrawElements(GL_TRIANGLES, mesh.indicesCount, GL_UNSIGNED_INT, 0)
                glBindVertexArray(0)
            }
        }

        Resource.SHADER.unbind()
    }

}