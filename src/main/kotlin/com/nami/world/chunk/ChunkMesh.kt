package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.graphics.UV
import com.nami.resources.texture.TextureAtlas
import com.nami.world.block.Face
import com.nami.world.block.Layer
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ChunkMesh(
    val chunk: Chunk,
    val layer: Layer
) {

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()

    var indexCount = 0

    init {
        generate()
    }

    fun generate() {
        val (vertices, indices) = generateData()
        setBufferData(vertices, indices)
    }

    fun generateData(): Pair<FloatBuffer, IntBuffer> {
        val vertexMap = mutableMapOf<Vertex, Int>()

        val verticesArrayList = ArrayList<Float>()
        val indicesArrayList = ArrayList<Int>()

        val filtered = chunk.voxels.filter { (_, voxel) -> voxel.block?.layer == layer }

        for ((position, voxel) in filtered) {
            val block = voxel.block

            // Face X Positive
            if (!filtered.containsKey(position + Vector3i(1, 0, 0)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.EAST, TextureAtlas.getUV(block!!.textures[Face.EAST]!!) )

            // Face X Negative
            if (!filtered.containsKey(position + Vector3i(-1, 0, 0)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.WEST, TextureAtlas.getUV(block!!.textures[Face.WEST]!!))

            // Face Y Positive
            if (!filtered.containsKey(position + Vector3i(0, 1, 0)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.TOP, TextureAtlas.getUV(block!!.textures[Face.TOP]!!))

            // Face Y Negative
            if (!filtered.containsKey(position + Vector3i(0, -1, 0)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.BOTTOM, TextureAtlas.getUV(block!!.textures[Face.BOTTOM]!!))

            // Face Z Positive
            if (!filtered.containsKey(position + Vector3i(0, 0, 1)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.NORTH, TextureAtlas.getUV(block!!.textures[Face.NORTH]!!))

            // Face Z Negative
            if (!filtered.containsKey(position + Vector3i(0, 0, -1)))
                addFace(vertexMap, verticesArrayList, indicesArrayList, position, Face.SOUTH, TextureAtlas.getUV(block!!.textures[Face.SOUTH]!!))
        }

        val vertices = BufferUtils.createFloatBuffer(verticesArrayList.size)
        for (vertex in verticesArrayList)
            vertices.put(vertex)
        vertices.flip()

        val indices = BufferUtils.createIntBuffer(indicesArrayList.size)
        for (index in indicesArrayList)
            indices.put(index)
        indices.flip()

        return Pair(
            vertices,
            indices
        )
    }

    private fun setBufferData(vertices: FloatBuffer, indices: IntBuffer) {
        indexCount = indices.limit()

        glBindVertexArray(vao)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.SIZE_BYTES, 6L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(2)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        glBindVertexArray(0)
    }

    private fun addFace(
        vertexMap: MutableMap<Vertex, Int>,
        vertices: ArrayList<Float>,
        indices: ArrayList<Int>,
        position: Vector3i,
        face: Face,
        uv: UV
    ) {
        val v0 = Vertex(position + face.offset0, face.normal, uv.position + Vector2f(uv.size.x, 0f))
        if (!vertexMap.containsKey(v0)) {
            vertexMap[v0] = vertexMap.size
            vertices.addAll(v0.toList())
        }

        val v1 = Vertex(position + face.offset1, face.normal, uv.position + uv.size)
        if (!vertexMap.containsKey(v1)) {
            vertexMap[v1] = vertexMap.size
            vertices.addAll(v1.toList())
        }

        val v2 = Vertex(position + face.offset2, face.normal, uv.position + Vector2f(0f, uv.size.y))
        if (!vertexMap.containsKey(v2)) {
            vertexMap[v2] = vertexMap.size
            vertices.addAll(v2.toList())
        }

        val v3 = Vertex(position + face.offset3, face.normal,uv.position )
        if (!vertexMap.containsKey(v3)) {
            vertexMap[v3] = vertexMap.size
            vertices.addAll(v3.toList())
        }

        indices.add(vertexMap[v0]!!)
        indices.add(vertexMap[v1]!!)
        indices.add(vertexMap[v2]!!)

        indices.add(vertexMap[v2]!!)
        indices.add(vertexMap[v3]!!)
        indices.add(vertexMap[v0]!!)
    }

    fun render() {
        if (indexCount <= 0)
            return

        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
        glBindVertexArray(0)
    }

    data class Vertex(
        val position: Vector3i,
        val normal: Vector3i,
        val uvs: Vector2f
    ) {

        fun toList() = listOf(
            position.x.toFloat(),
            position.y.toFloat(),
            position.z.toFloat(),
            normal.x.toFloat(),
            normal.y.toFloat(),
            normal.z.toFloat(),
            uvs.x,
            uvs.y
        )

    }

}