package com.nami.world.chunk

import com.nami.Time
import com.nami.resources.Resources
import com.nami.resources.texture.TextureAtlas
import com.nami.world.entity.Player
import com.nami.world.resources.block.Block
import com.nami.world.material.Face
import com.nami.world.material.Layer
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer


class Mesh(private val chunk: Chunk, val layer: Layer) {

    val world = chunk.world
    val blockManager = world.blockManager

    var vao = 0
        private set
    var vbo = 0
        private set
    var bufferVbo: FloatBuffer? = null
        private set
    var ebo = 0
        private set
    var bufferEbo: IntBuffer? = null
        private set
    var indicesCount = 0
        private set

    private val noise = JNoise.newBuilder()
        .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(world.seed).build())
        .scale(1.0 / 2.0)
        .addModifier { v: Double -> ((v + 1) / 2.0) * 0.15 + 0.85 }
        .build()

    fun generate() {
        val generate = blockManager.faces[layer]!!.filterKeys {
            it.x in (chunk.position.x * Chunk.SIZE.x until (chunk.position.x + 1) * Chunk.SIZE.x) &&
                    it.y in (chunk.position.y * Chunk.SIZE.y until (chunk.position.y + 1) * Chunk.SIZE.y) &&
                    it.z in (chunk.position.z * Chunk.SIZE.z until (chunk.position.z + 1) * Chunk.SIZE.z)
        }

        val size = generate.map { it.value.size }.sum()
        if (size <= 0)
            return

        //size * triangles_fer_face * points_per_triangle * (number_components_position + number_components_normal + number_components_uv + number_components_color + number_components_brightness)
        bufferVbo = MemoryUtil.memAllocFloat(size * 2 * 3 * (3 + 3 + 2 + 3 + 1))
        bufferEbo = MemoryUtil.memAllocInt(size * 2 * 3)

        var index = 0
        for ((position, faces) in generate) {
            val chunkBlockPosition = Vector3f(position).sub(Vector3f(chunk.position).mul(Vector3f(Chunk.SIZE)))
            val block = blockManager.getBlock(position) ?: continue

            val bright = noise.evaluateNoise(
                position.x.toDouble(),
                position.y.toDouble(),
                position.z.toDouble()
            ).toFloat() * block.health

            for (face in faces) {
                val texture: String = when (face) {
                    Face.TOP -> block.template.textures[0]
                    Face.BOTTOM -> block.template.textures[1]
                    Face.NORTH -> block.template.textures[2]
                    Face.EAST -> block.template.textures[3]
                    Face.WEST -> block.template.textures[4]
                    Face.SOUTH -> block.template.textures[5]
                }
                var color = Vector3f(1f)
                if (setOf(
                        "block.grass_top",
                        "block.oak_leaves",
                        "block.birch_leaves",
                        "block.jungle_leaves"
                    ).contains(texture)
                )
                    color = Vector3f(0f, 0.8f, 0f)

                val uv = TextureAtlas.getUV(texture)

                var pos = Vector3f(face.triangle0[0]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs0[0].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs0[0].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++

                pos = Vector3f(face.triangle0[1]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs0[1].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs0[1].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++

                pos = Vector3f(face.triangle0[2]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs0[2].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs0[2].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++

                pos = Vector3f(face.triangle1[0]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs1[0].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs1[0].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++

                pos = Vector3f(face.triangle1[1]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs1[1].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs1[1].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++

                pos = Vector3f(face.triangle1[2]).add(chunkBlockPosition)
                bufferVbo!!.put(pos.x)
                bufferVbo!!.put(pos.y)
                bufferVbo!!.put(pos.z)
                bufferVbo!!.put(face.normal.x)
                bufferVbo!!.put(face.normal.y)
                bufferVbo!!.put(face.normal.z)
                bufferVbo!!.put((uv.position.x + uv.size.x * face.uvs1[2].x).coerceIn(0f, 1f))
                bufferVbo!!.put((uv.position.y + uv.size.y * face.uvs1[2].y).coerceIn(0f, 1f))
                bufferVbo!!.put(color.x)
                bufferVbo!!.put(color.y)
                bufferVbo!!.put(color.z)
                bufferVbo!!.put(bright)
                bufferEbo!!.put(index)
                index++
            }
        }

        bufferVbo!!.flip()
        bufferEbo!!.flip()
    }

    fun upload() {
        if (bufferVbo == null || bufferVbo!!.limit() <= 0)
            return
        if (bufferEbo == null || bufferEbo!!.limit() <= 0)
            return

        if (vao == 0) vao = glGenVertexArrays()
        glBindVertexArray(vao)

        if (vbo == 0) vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, bufferVbo, GL_DYNAMIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 12 * Float.SIZE_BYTES, 0L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 12 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 12 * Float.SIZE_BYTES, 6L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(3)
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 12 * Float.SIZE_BYTES, 8L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(4)
        glVertexAttribPointer(4, 1, GL_FLOAT, false, 12 * Float.SIZE_BYTES, 11L * Float.SIZE_BYTES)

        if (ebo == 0) ebo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferEbo, GL_DYNAMIC_DRAW)

        glBindVertexArray(0)

        indicesCount = bufferEbo!!.limit()

        MemoryUtil.memFree(bufferVbo)
        MemoryUtil.memFree(bufferEbo)

        bufferVbo = null
        bufferEbo = null
    }

    fun render(player: Player, time: Time) {
        if (indicesCount <= 0)
            return

        val shader = Resources.SHADER.get("chunk.solid").bind()
        shader.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shader.uniform.set("u_specular_exponent", 8.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection())
        shader.uniform.set("u_view_matrix", player.camera.view())

        shader.uniform.set("u_camera_position", player.transform.position)

        shader.uniform.set("u_time", time.time)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
        shader.uniform.set("u_texture_diffuse", 0)

        val model = Matrix4f().translate(
            (chunk.position.x * Chunk.SIZE.x).toFloat(),
            (chunk.position.y * Chunk.SIZE.y).toFloat(),
            (chunk.position.z * Chunk.SIZE.z).toFloat()
        )

        shader.uniform.set("u_model_matrix", model)

        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)

        Resources.SHADER.unbind()
    }

}