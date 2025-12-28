package com.nami.world.chunk

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

    fun generateVertices(): FloatArray {
        val vertices = mutableListOf<Float>()

        val filtered = chunk.voxels.filter { (_, voxel) -> voxel.block?.layer == layer }
        for ((position, voxel) in filtered) {
            val block = voxel.block

            val x = position.x
            val y = position.y
            val z = position.z

            // +X
            if (filtered[Vector3i(position).add(1, 0, 0)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x + 1, y, z),
                    Vector3i(x + 1, y + 1, z),
                    Vector3i(x + 1, y + 1, z + 1),
                    Vector3i(x + 1, y, z + 1)
                )
            }

            // -X
            if (filtered[Vector3i(position).add(-1, 0, 0)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x, y, z + 1),
                    Vector3i(x, y + 1, z + 1),
                    Vector3i(x, y + 1, z),
                    Vector3i(x, y, z)
                )
            }

            // +Y
            if (filtered[Vector3i(position).add(0, 1, 0)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x, y + 1, z),
                    Vector3i(x, y + 1, z + 1),
                    Vector3i(x + 1, y + 1, z + 1),
                    Vector3i(x + 1, y + 1, z)
                )
            }

            // -Y
            if (filtered[Vector3i(position).add(0, -1, 0)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x, y, z + 1),
                    Vector3i(x, y, z),
                    Vector3i(x + 1, y, z),
                    Vector3i(x + 1, y, z + 1)
                )
            }

            // +Z
            if (filtered[Vector3i(position).add(0, 0, 1)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x + 1, y, z + 1),
                    Vector3i(x + 1, y + 1, z + 1),
                    Vector3i(x, y + 1, z + 1),
                    Vector3i(x, y, z + 1)
                )
            }

            // -Z
            if (filtered[Vector3i(position).add(0, 0, -1)] == null) {
                addQuad(
                    vertices,
                    Vector3i(x, y, z),
                    Vector3i(x, y + 1, z),
                    Vector3i(x + 1, y + 1, z),
                    Vector3i(x + 1, y, z)
                )
            }

        }

        return vertices.toFloatArray()
    }

    private fun addQuad(
        vertices: MutableList<Float>,
        v0: Vector3i,
        v1: Vector3i,
        v2: Vector3i,
        v3: Vector3i
    ) {
        // triangle 1
        vertices.add(v0.x.toFloat()); vertices.add(v0.y.toFloat()); vertices.add(v0.z.toFloat())
        vertices.add(v1.x.toFloat()); vertices.add(v1.y.toFloat()); vertices.add(v1.z.toFloat())
        vertices.add(v2.x.toFloat()); vertices.add(v2.y.toFloat()); vertices.add(v2.z.toFloat())

        // triangle 2
        vertices.add(v2.x.toFloat()); vertices.add(v2.y.toFloat()); vertices.add(v2.z.toFloat())
        vertices.add(v3.x.toFloat()); vertices.add(v3.y.toFloat()); vertices.add(v3.z.toFloat())
        vertices.add(v0.x.toFloat()); vertices.add(v0.y.toFloat()); vertices.add(v0.z.toFloat())
    }

    private fun setBufferData(vertices: FloatArray) {
        vertexCount = vertices.size / 3

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)

        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0L)
        glEnableVertexAttribArray(0)

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