package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.extension.times
import com.nami.graphics.UV
import com.nami.resources.texture.TextureAtlas
import com.nami.world.Player
import com.nami.world.block.Face
import com.nami.world.block.Layer
import mu.KotlinLogging
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.text.NumberFormat

class ChunkMesh(
    val chunk: Chunk,
    val layer: Layer
) {

    //TODO sort faces instead of only chunks

    val world = chunk.world

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()

    val log = KotlinLogging.logger {}

    var indexCount = 0

    fun generate() {
        val (vertices, indices) = generateData()
        setBufferData(vertices, indices)
    }

    fun generateData(): Pair<FloatBuffer, IntBuffer> {

        val faces = mutableSetOf<Pair<Vector3i, Face>>()

        for (z in 0 until Chunk.SIZE.z)
            for (y in 0 until Chunk.SIZE.y)
                for (x in 0 until Chunk.SIZE.x) {
                    val localPosition = Vector3i(x, y, z)

                    if (chunk.voxels[localPosition]?.block == null)
                        continue
                    val currentBlock = chunk.voxels[localPosition]!!.block!!

                    val globalPosition = chunk.position * Chunk.SIZE + localPosition

                    for ((direction, face) in Face.byNormal) {
                        val targetPosition = globalPosition + direction

                        val voxel = world.getVoxel(targetPosition)

                        if (voxel == null)
                            continue

                        if (voxel.block != null && voxel.block.layer == currentBlock.layer)
                            continue

                        faces.add(Pair(localPosition, face))
                    }
                }

        val vertexMap = mutableMapOf<Vertex, Int>()

        val verticesArrayList = ArrayList<Float>()
        val indicesArrayList = ArrayList<Int>()

        faces.forEach { (position, face) ->
            val block = chunk.voxels[position]!!.block

            addFace(
                vertexMap,
                verticesArrayList,
                indicesArrayList,
                position,
                face,
                TextureAtlas.getUV(block!!.textures[face]!!)
            )
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

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        val stride = 8 * Float.SIZE_BYTES

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false,stride, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(2)

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

        val v3 = Vertex(position + face.offset3, face.normal, uv.position)
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