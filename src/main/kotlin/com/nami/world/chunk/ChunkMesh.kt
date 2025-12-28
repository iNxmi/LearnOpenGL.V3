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

        for ((position, _) in filtered) {
            val x = position.x
            val y = position.y
            val z = position.z

            // +X
            if (filtered[Vector3i(position).add(1, 0, 0)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x + 1, y, z),
                    v1 = Vector3i(x + 1, y + 1, z),
                    v2 = Vector3i(x + 1, y + 1, z + 1),
                    v3 = Vector3i(x + 1, y, z + 1),
                    normal = Vector3i(1, 0, 0)
                )
            }

            // -X
            if (filtered[Vector3i(position).add(-1, 0, 0)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x, y, z + 1),
                    v1 = Vector3i(x, y + 1, z + 1),
                    v2 = Vector3i(x, y + 1, z),
                    v3 = Vector3i(x, y, z),
                    normal = Vector3i(-1, 0, 0)
                )
            }

            // +Y
            if (filtered[Vector3i(position).add(0, 1, 0)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x, y + 1, z),
                    v1 = Vector3i(x, y + 1, z + 1),
                    v2 = Vector3i(x + 1, y + 1, z + 1),
                    v3 = Vector3i(x + 1, y + 1, z),
                    normal = Vector3i(0, 1, 0)
                )
            }

            // -Y
            if (filtered[Vector3i(position).add(0, -1, 0)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x, y, z + 1),
                    v1 = Vector3i(x, y, z),
                    v2 = Vector3i(x + 1, y, z),
                    v3 = Vector3i(x + 1, y, z + 1),
                    normal = Vector3i(0, -1, 0)
                )
            }

            // +Z
            if (filtered[Vector3i(position).add(0, 0, 1)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x + 1, y, z + 1),
                    v1 = Vector3i(x + 1, y + 1, z + 1),
                    v2 = Vector3i(x, y + 1, z + 1),
                    v3 = Vector3i(x, y, z + 1),
                    normal = Vector3i(0, 0, 1)
                )
            }

            // -Z
            if (filtered[Vector3i(position).add(0, 0, -1)] == null) {
                addQuad(
                    vertices = vertices,
                    v0 = Vector3i(x, y, z),
                    v1 = Vector3i(x, y + 1, z),
                    v2 = Vector3i(x + 1, y + 1, z),
                    v3 = Vector3i(x + 1, y, z),
                    normal = Vector3i(0, 0, -1)
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
        v3: Vector3i,
        normal: Vector3i
    ) {
        // triangle 1
        vertices.add(v0.x.toFloat()); vertices.add(v0.y.toFloat()); vertices.add(v0.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())
        vertices.add(v1.x.toFloat()); vertices.add(v1.y.toFloat()); vertices.add(v1.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())
        vertices.add(v2.x.toFloat()); vertices.add(v2.y.toFloat()); vertices.add(v2.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())

        // triangle 2
        vertices.add(v2.x.toFloat()); vertices.add(v2.y.toFloat()); vertices.add(v2.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())
        vertices.add(v3.x.toFloat()); vertices.add(v3.y.toFloat()); vertices.add(v3.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())
        vertices.add(v0.x.toFloat()); vertices.add(v0.y.toFloat()); vertices.add(v0.z.toFloat()); vertices.add(normal.x.toFloat()); vertices.add(normal.y.toFloat()); vertices.add(normal.z.toFloat())
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