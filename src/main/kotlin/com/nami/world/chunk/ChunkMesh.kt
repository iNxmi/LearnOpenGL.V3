package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.extension.times
import com.nami.resources.texture.TextureAtlas
import com.nami.world.block.Face
import com.nami.world.block.Layer
import org.joml.Vector2f
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ChunkMesh(
    val chunk: Chunk,
    val layer: Layer
) {

    //TODO sort faces instead of only chunks

    val world = chunk.world

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()

    var indexCount = 0

    fun generate() {
        val (vertices, indices) = generateData()
        setBufferData(vertices, indices)
    }

    private fun getExposedFaces(): Set<Pair<Vector3i, Face>> {
        val exposedFaces = mutableSetOf<Pair<Vector3i, Face>>()
        for (z in 0 until Chunk.SIZE.z)
            for (y in 0 until Chunk.SIZE.y)
                for (x in 0 until Chunk.SIZE.x) {
                    val localPosition = Vector3i(x, y, z)
                    val globalPosition = chunk.position * Chunk.SIZE + localPosition

                    val localVoxel = chunk.voxels[localPosition]!!
                    val localBlock = localVoxel.block ?: continue

                    for ((direction, face) in Face.byNormal) {
                        val globalTargetPosition = globalPosition + direction

                        val globalVoxel = world.getVoxel(globalTargetPosition) ?: continue
                        val globalBlock = globalVoxel.block

                        if (globalBlock != null && globalBlock.layer == localBlock.layer)
                            continue

                        exposedFaces.add(Pair(localPosition, face))
                    }
                }
        return exposedFaces
    }

    fun generateData(): Pair<FloatBuffer, IntBuffer> {
        val exposedFaces = getExposedFaces()
        val vertexMap = mutableMapOf<Vertex, Int>()
        val verticesArrayList = ArrayList<Float>()
        val indicesArrayList = ArrayList<Int>()
        for ((position, face) in exposedFaces) {
            val block = chunk.voxels[position]!!.block

            val uv = TextureAtlas.getUV(block!!.textures[face]!!)

            val vertices = listOf(
                Vertex(position + face.offset0, face.normal, uv.position + Vector2f(uv.size.x, 0.0f)),
                Vertex(position + face.offset1, face.normal, uv.position + uv.size),
                Vertex(position + face.offset2, face.normal, uv.position + Vector2f(0.0f, uv.size.y)),
                Vertex(position + face.offset3, face.normal, uv.position)
            )

            for (vertex in vertices) {
                if (vertexMap.containsKey(vertex))
                    continue

                vertexMap[vertex] = vertexMap.size
                verticesArrayList.addAll(vertex.toList())
            }

            indicesArrayList.add(vertexMap[vertices[0]]!!)
            indicesArrayList.add(vertexMap[vertices[1]]!!)
            indicesArrayList.add(vertexMap[vertices[2]]!!)

            indicesArrayList.add(vertexMap[vertices[2]]!!)
            indicesArrayList.add(vertexMap[vertices[3]]!!)
            indicesArrayList.add(vertexMap[vertices[0]]!!)
        }

        val vertices = BufferUtils.createFloatBuffer(verticesArrayList.size)
        for (vertex in verticesArrayList)
            vertices.put(vertex)
        vertices.flip()

        val indices = BufferUtils.createIntBuffer(indicesArrayList.size)
        for (index in indicesArrayList)
            indices.put(index)
        indices.flip()

        return Pair(vertices, indices)
    }

    private fun setBufferData(vertices: FloatBuffer, indices: IntBuffer) {
        indexCount = indices.limit()

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW)

        val stride = (3 + 3 + 2) * Float.SIZE_BYTES

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(2)

        glBindVertexArray(0)
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