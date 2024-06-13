package com.nami.world.chunk

import com.nami.resources.texture.TextureAtlas
import com.nami.world.block.Block
import com.nami.world.block.Face
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3f
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer


class ChunkMesh(private val chunk: Chunk, val layer: Block.Layer) {

    val world = chunk.world
    val blockManager = world.blockManager
    val chunkManager = world.chunkManager
    val biomeManager = world.biomeManager

    var vao = 0
        private set
    var vboPositions = 0
        private set
    var vboNormals = 0
        private set
    var vboUVs = 0
        private set
    var vboColors = 0
        private set
    var vboBrightness = 0
        private set
    var vboHealth = 0
        private set
    var ebo = 0
        private set
    var indicesCount = 0
        private set

    val whiteNoise = JNoise.newBuilder().value(1077, Interpolation.CUBIC, FadeFunction.NONE)
        .scale(1.0)
        .addModifier { v: Double -> ((v + 1) / 2.0) * 0.25 + 0.75 }
        .build()

    fun update() {
        val generate = blockManager.faces[layer]!!.filterKeys {
            it.x in (chunk.position.x * Chunk.SIZE.x until (chunk.position.x + 1) * Chunk.SIZE.x) &&
                    it.y in (chunk.position.y * Chunk.SIZE.y until (chunk.position.y + 1) * Chunk.SIZE.y) &&
                    it.z in (chunk.position.z * Chunk.SIZE.z until (chunk.position.z + 1) * Chunk.SIZE.z)
        }

        var size = 0
        generate.forEach { (k, v) -> size += v.size }

        if (size <= 0)
            return

        val positions = MemoryUtil.memAllocFloat(size * 2 * 3 * 3)
        val normals = MemoryUtil.memAllocFloat(size * 2 * 3 * 3)
        val uvs = MemoryUtil.memAllocFloat(size * 2 * 3 * 2)
        val colors = MemoryUtil.memAllocFloat(size * 2 * 3 * 3)
        val brightness = MemoryUtil.memAllocFloat(size * 2 * 3)
        val health = MemoryUtil.memAllocFloat(size * 2 * 3)
        val indices = MemoryUtil.memAllocInt(size * 2 * 3)

        var index = 0
        generate.forEach { (position, faces) ->
            val chunkBlockPosition = Vector3f(position).sub(Vector3f(chunk.position).mul(Vector3f(Chunk.SIZE)))
            val block = blockManager.getBlock(position)!!

            val bright =
                whiteNoise.evaluateNoise(position.x.toDouble(), position.y.toDouble(), position.z.toDouble()).toFloat()

            faces.forEach { face ->
                val texture: String = when (face) {
                    Face.TOP -> block!!.template.textures[0]
                    Face.BOTTOM -> block!!.template.textures[1]
                    Face.NORTH -> block!!.template.textures[2]
                    Face.EAST -> block!!.template.textures[3]
                    Face.WEST -> block!!.template.textures[4]
                    Face.SOUTH -> block!!.template.textures[5]
                }
                var color = Vector3f(1f)
                if (texture == "blocks.grass_top" || texture == "blocks.leaves")
                    color =
                        Vector3f(0f, 0.8f, 0f)

                val uv = TextureAtlas.getUV(texture)

                var pos = Vector3f(face.triangle0[0]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs0[0].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs0[0].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++

                pos = Vector3f(face.triangle0[1]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs0[1].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs0[1].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++

                pos = Vector3f(face.triangle0[2]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs0[2].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs0[2].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++

                pos = Vector3f(face.triangle1[0]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs1[0].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs1[0].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++

                pos = Vector3f(face.triangle1[1]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs1[1].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs1[1].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++

                pos = Vector3f(face.triangle1[2]).add(chunkBlockPosition)
                positions.put(pos.x)
                positions.put(pos.y)
                positions.put(pos.z)
                normals.put(face.normal.x)
                normals.put(face.normal.y)
                normals.put(face.normal.z)
                uvs.put((uv.position.x + uv.size.x * face.uvs1[2].x).coerceIn(0f, 1f))
                uvs.put((uv.position.y + uv.size.y * face.uvs1[2].y).coerceIn(0f, 1f))
                colors.put(color.x)
                colors.put(color.y)
                colors.put(color.z)
                brightness.put(bright)
                health.put(block.health)
                indices.put(index)
                index++
            }
        }

        positions.flip()
        normals.flip()
        uvs.flip()
        colors.flip()
        brightness.flip()
        health.flip()
        indices.flip()

        chunkManager.sendToGPU(chunk.position) {
            sendToGPU(
                positions,
                normals,
                uvs,
                colors,
                brightness,
                health,
                indices
            )
        }
    }

    fun sendToGPU(
        positions: FloatBuffer,
        normals: FloatBuffer,
        uvs: FloatBuffer,
        colors: FloatBuffer,
        brightness: FloatBuffer,
        health: FloatBuffer,
        indices: IntBuffer
    ) {
        if (vao == 0) vao = glGenVertexArrays()
        glBindVertexArray(vao)

        if (vboPositions == 0) vboPositions = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboPositions)
        glBufferData(GL_ARRAY_BUFFER, positions, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        if (vboNormals == 0) vboNormals = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboNormals)
        glBufferData(GL_ARRAY_BUFFER, normals, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

        if (vboUVs == 0) vboUVs = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboUVs)
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0)

        if (vboColors == 0) vboColors = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboColors)
        glBufferData(GL_ARRAY_BUFFER, colors, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(3)
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0)

        if (vboBrightness == 0) vboBrightness = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboBrightness)
        glBufferData(GL_ARRAY_BUFFER, brightness, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(4)
        glVertexAttribPointer(4, 1, GL_FLOAT, false, 0, 0)

        if (vboHealth == 0) vboHealth = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboHealth)
        glBufferData(GL_ARRAY_BUFFER, health, GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(5)
        glVertexAttribPointer(5, 1, GL_FLOAT, false, 0, 0)

        if (ebo == 0) ebo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW)

        glBindVertexArray(0)

        indicesCount = indices.limit()

        MemoryUtil.memFree(positions)
        MemoryUtil.memFree(normals)
        MemoryUtil.memFree(uvs)
        MemoryUtil.memFree(colors)
        MemoryUtil.memFree(brightness)
        MemoryUtil.memFree(health)
        MemoryUtil.memFree(indices)
    }

}