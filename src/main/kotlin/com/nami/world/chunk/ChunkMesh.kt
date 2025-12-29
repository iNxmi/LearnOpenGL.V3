package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.world.block.Layer
import org.joml.Vector2f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.*

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

    fun generateData(): Pair<FloatArray, IntArray> {
        val vertices = mutableListOf<Vertex>()
        val indices = mutableListOf<Int>()

        val filtered = chunk.voxels.filter { (_, voxel) -> voxel.block?.layer == layer }

        for ((position, _) in filtered) {
            // Face X Positive
            if (!filtered.containsKey(position + Vector3i(1, 0, 0)))
                addFace(vertices, indices, position, Face.EAST)

            // Face X Negative
            if (!filtered.containsKey(position + Vector3i(-1, 0, 0)))
                addFace(vertices, indices, position, Face.WEST)

            // Face Y Positive
            if (!filtered.containsKey(position + Vector3i(0, 1, 0)))
                addFace(vertices, indices, position, Face.TOP)

            // Face Y Negative
            if (!filtered.containsKey(position + Vector3i(0, -1, 0)))
                addFace(vertices, indices, position, Face.BOTTOM)

            // Face Z Positive
            if (!filtered.containsKey(position + Vector3i(0, 0, 1)))
                addFace(vertices, indices, position, Face.NORTH)

            // Face Z Negative
            if (!filtered.containsKey(position + Vector3i(0, 0, -1)))
                addFace(vertices, indices, position, Face.SOUTH)
        }

        return Pair(
            vertices.flatMap { it.toList() }.toFloatArray(),
            indices.toIntArray()
        )
    }

    private fun setBufferData(vertices: FloatArray, indices: IntArray) {
        indexCount = indices.size

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        glBindVertexArray(0)
    }

    private fun addFace(
        vertices: MutableList<Vertex>,
        indices: MutableList<Int>,
        position: Vector3i,
        face: Face
    ) {
        val v0 = Vertex(position + face.offset0, face.normal)
        if (v0 !in vertices)
            vertices.add(v0)

        val v1 = Vertex(position + face.offset1, face.normal)
        if (v1 !in vertices)
            vertices.add(v1)

        val v2 = Vertex(position + face.offset2, face.normal)
        if (v2 !in vertices)
            vertices.add(v2)

        val v3 = Vertex(position + face.offset3, face.normal)
        if (v3 !in vertices)
            vertices.add(v3)

        indices.add(vertices.indexOf(v0))
        indices.add(vertices.indexOf(v1))
        indices.add(vertices.indexOf(v2))

        indices.add(vertices.indexOf(v2))
        indices.add(vertices.indexOf(v3))
        indices.add(vertices.indexOf(v0))
    }

    fun render() {
        if (indexCount <= 0)
            return

        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
        glBindVertexArray(0)
    }

    enum class Face(
        val offset0: Vector3i,
        val offset1: Vector3i,
        val offset2: Vector3i,
        val offset3: Vector3i,
        val normal: Vector3i
    ) {
        // Y Positive
        TOP(
            offset0 = Vector3i(0, 1, 0),
            offset1 = Vector3i(0, 1, 1),
            offset2 = Vector3i(1, 1, 1),
            offset3 = Vector3i(1, 1, 0),
            normal = Vector3i(0, 1, 0)
        ),

        // Y Negative
        BOTTOM(
            offset0 = Vector3i(0, 0, 1),
            offset1 = Vector3i(0, 0, 0),
            offset2 = Vector3i(1, 0, 0),
            offset3 = Vector3i(1, 0, 1),
            normal = Vector3i(0, -1, 0)
        ),

        // Z Positive
        NORTH(
            offset0 = Vector3i(1, 0, 1),
            offset1 = Vector3i(1, 1, 1),
            offset2 = Vector3i(0, 1, 1),
            offset3 = Vector3i(0, 0, 1),
            normal = Vector3i(0, 0, 1)
        ),

        // X Positive
        EAST(
            offset0 = Vector3i(1, 0, 0),
            offset1 = Vector3i(1, 1, 0),
            offset2 = Vector3i(1, 1, 1),
            offset3 = Vector3i(1, 0, 1),
            normal = Vector3i(1, 0, 0)
        ),

        // Z Negative
        SOUTH(
            offset0 = Vector3i(0, 0, 0),
            offset1 = Vector3i(0, 1, 0),
            offset2 = Vector3i(1, 1, 0),
            offset3 = Vector3i(1, 0, 0),
            normal = Vector3i(0, 0, -1)
        ),

        // X Negative
        WEST(
            offset0 = Vector3i(0, 0, 1),
            offset1 = Vector3i(0, 1, 1),
            offset2 = Vector3i(0, 1, 0),
            offset3 = Vector3i(0, 0, 0),
            normal = Vector3i(-1, 0, 0)
        )
    }

    data class Vertex(
        val position: Vector3i,
        val normal: Vector3i
    ) {

        fun toList() = listOf(
            position.x.toFloat(),
            position.y.toFloat(),
            position.z.toFloat(),
            normal.x.toFloat(),
            normal.y.toFloat(),
            normal.z.toFloat()
        )

    }

}