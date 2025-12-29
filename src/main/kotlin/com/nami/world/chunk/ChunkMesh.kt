package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.world.block.Layer
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.*

class ChunkMesh(
    val chunk: Chunk,
    val layer: Layer
) {

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    var vertexCount = 0

    init {
        generate()
    }

    fun generate() {
        val vertices = generateVertices()
        setBufferData(vertices)
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

    fun generateVertices(): FloatArray {
        val vertices = mutableListOf<Float>()
        val indices = mutableListOf<Int>()

        val filtered = chunk.voxels.filter { (_, voxel) -> voxel.block?.layer == layer }

        for ((position, _) in filtered) {
            // Face X Positive
            if (filtered[position + Vector3i(1, 0, 0)] == null)
                addFace(vertices, indices, position, Face.EAST)

            // Face X Negative
            if (filtered[position + Vector3i(-1, 0, 0)] == null)
                addFace(vertices, indices, position, Face.WEST)

            // Face Y Positive
            if (filtered[position + Vector3i(0, 1, 0)] == null)
                addFace(vertices, indices, position, Face.TOP)

            // Face Y Negative
            if (filtered[position + Vector3i(0, -1, 0)] == null)
                addFace(vertices, indices, position, Face.BOTTOM)

            // Face Z Positive
            if (filtered[position + Vector3i(0, 0, 1)] == null)
                addFace(vertices, indices, position, Face.NORTH)

            // Face Z Negative
            if (filtered[position + Vector3i(0, 0, -1)] == null)
                addFace(vertices, indices, position, Face.SOUTH)
        }

        return vertices.toFloatArray()
    }

    private fun addFace(
        vertices: MutableList<Float>,
        indices: MutableList<Int>,
        position: Vector3i,
        face: Face
    ) {
        val v0 = Vertex(Vector3i(position).add(face.offset0), face.normal)
//        if (v0 !in vertices)
//            vertices.add(v0)

        val v1 = Vertex(Vector3i(position).add(face.offset1), face.normal)
//        if (v1 !in vertices)
//            vertices.add(v1)

        val v2 = Vertex(Vector3i(position).add(face.offset2), face.normal)
//        if (v2 !in vertices)
//            vertices.add(v2)

        val v3 = Vertex(Vector3i(position).add(face.offset3), face.normal)
//        if (v3 !in vertices)
//            vertices.add(v3)

//        indices.add(vertices.indexOf(v0))
//        indices.add(vertices.indexOf(v1))
//        indices.add(vertices.indexOf(v2))
//
//        indices.add(vertices.indexOf(v2))
//        indices.add(vertices.indexOf(v3))
//        indices.add(vertices.indexOf(v0))

        vertices.addAll(v0.toArray().toTypedArray())
        vertices.addAll(v1.toArray().toTypedArray())
        vertices.addAll(v2.toArray().toTypedArray())

        vertices.addAll(v2.toArray().toTypedArray())
        vertices.addAll(v3.toArray().toTypedArray())
        vertices.addAll(v0.toArray().toTypedArray())
    }

    private fun setBufferData(vertices: FloatArray) {
        vertexCount = vertices.size / 3

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)

        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun render() {
        if (vertexCount <= 0)
            return

        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
        glBindVertexArray(0)
    }

}