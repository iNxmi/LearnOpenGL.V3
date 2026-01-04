package com.nami.world.chunk

import com.nami.extension.plus
import com.nami.extension.times
import com.nami.resources.texture.TextureAtlas
import com.nami.world.Player
import com.nami.world.block.AmbientOcclusion
import com.nami.world.block.Face
import com.nami.world.block.Layer
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ChunkMesh(
    val chunk: Chunk,
    val layer: Layer
) {

    companion object {
        const val INDICES_PER_FACE = 6
    }

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()

    var indexCount = 0

    private val faces = mutableListOf<FaceRange>()
    private lateinit var originalIndices: IntArray

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

                    val localVoxel = chunk.getVoxel(localPosition)
                    val localBlock = localVoxel.block ?: continue

                    if (localBlock.layer != layer)
                        continue

                    for ((direction, face) in Face.byNormal) {
                        val globalTargetPosition = globalPosition + direction

                        if (globalTargetPosition.x < 0 || globalTargetPosition.y < 0 || globalTargetPosition.z < 0)
                            continue

                        val globalVoxel = chunk.world.getVoxel(globalTargetPosition) ?: continue
                        val globalBlock = globalVoxel.block

                        if (globalBlock != null && globalBlock.layer == localBlock.layer)
                            continue

                        exposedFaces.add(Pair(localPosition, face))
                    }
                }

        return exposedFaces
    }

    fun generateData(): Pair<FloatBuffer, IntBuffer> {
        faces.clear()

        val exposedFaces = getExposedFaces()
        val vertexMap = mutableMapOf<Vertex, Int>()
        val verticesArrayList = ArrayList<Float>()
        val indicesArrayList = ArrayList<Int>()
        for ((localPosition, face) in exposedFaces) {
            val block = chunk.getVoxel(localPosition).block

            val uv = TextureAtlas.getUVs(block!!.textures[face]!!)

            val globalPosition = chunk.position * Chunk.SIZE + localPosition + face.normal
            val aoTable = AmbientOcclusion.getTable(face)

            val vertices = listOf(
                Vertex(
                    localPosition + face.offset0,
                    face.normal,
                    uv.position + Vector2f(uv.size.x, 0.0f),
                    computeAoBrightness(globalPosition, aoTable[0])
                ),
                Vertex(
                    localPosition + face.offset1,
                    face.normal,
                    uv.position + uv.size,
                    computeAoBrightness(globalPosition, aoTable[1])
                ),
                Vertex(
                    localPosition + face.offset2,
                    face.normal,
                    uv.position + Vector2f(0.0f, uv.size.y),
                    computeAoBrightness(globalPosition, aoTable[2])
                ),
                Vertex(
                    localPosition + face.offset3,
                    face.normal,
                    uv.position,
                    computeAoBrightness(globalPosition, aoTable[3])
                )
            )

            for (vertex in vertices) {
                if (vertexMap.containsKey(vertex))
                    continue

                vertexMap[vertex] = vertexMap.size
                verticesArrayList.addAll(vertex.toList())
            }

            val a = vertices[0].brightness
            val b = vertices[1].brightness
            val c = vertices[2].brightness
            val d = vertices[3].brightness

            val flip = (a + c) > (b + d)

            val order = if (flip) intArrayOf(0, 1, 2, 2, 3, 0) else intArrayOf(1, 2, 3, 3, 0, 1)
            for (index in order)
                indicesArrayList.add(vertexMap[vertices[index]]!!)

            val localCenter = Vector3f(localPosition).add(0.5f, 0.5f, 0.5f).add(Vector3f(face.normal).mul(0.5f))
            val globalCenter = Vector3f(localCenter).add(Vector3f(chunk.position).mul(Vector3f(Chunk.SIZE)))
            faces += FaceRange(indicesArrayList.size - 6, globalCenter)

            originalIndices = indicesArrayList.toIntArray()
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

    fun sortFaces(player: Player) {
        if (faces.isEmpty())
            return

        faces.sortByDescending {
            it.center.distanceSquared(player.camera.transform.position)
        }

        val sortedIndices = IntArray(originalIndices.size)
        var offset = 0

        for (face in faces) {
            System.arraycopy(originalIndices, face.indexStart, sortedIndices, offset, INDICES_PER_FACE)
            offset += INDICES_PER_FACE
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, sortedIndices)
    }

    private fun setBufferData(vertices: FloatBuffer, indices: IntBuffer) {
        indexCount = indices.limit()

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW)

        val stride = (3 + 3 + 2 + 1) * Float.SIZE_BYTES

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(2)

        glVertexAttribPointer(3, 1, GL_FLOAT, false, stride, 8L * Float.SIZE_BYTES)
        glEnableVertexAttribArray(3)

        glBindVertexArray(0)
    }

    fun render() {
        if (indexCount <= 0)
            return

        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0L)
        glBindVertexArray(0)
    }

    private data class Vertex(
        val position: Vector3i,
        val normal: Vector3i,
        val uvs: Vector2f,
        val brightness: Float
    ) {

        fun toList() = listOf(
            position.x.toFloat(),
            position.y.toFloat(),
            position.z.toFloat(),
            normal.x.toFloat(),
            normal.y.toFloat(),
            normal.z.toFloat(),
            uvs.x,
            uvs.y,
            brightness
        )

    }

    data class FaceRange(
        val indexStart: Int,
        val center: Vector3f
    )

    fun isSolid(globalPosition: Vector3i): Boolean {
        if (globalPosition.x < 0 || globalPosition.y < 0 || globalPosition.z < 0)
            return false

        val voxel = chunk.world.getVoxel(globalPosition) ?: return false
        val block = voxel.block ?: return false
        return block.layer == layer
    }

    fun computeAoBrightness(
        basePosition: Vector3i,
        offsets: AmbientOcclusion.AOOffsets
    ): Float {
        val side1 = isSolid(basePosition + offsets.side1)
        val side2 = isSolid(basePosition + offsets.side2)
        val corner = isSolid(basePosition + offsets.corner)

        val level = if (side1 && side2) {
            3
        } else {
            val s1 = if (side1) 1 else 0
            val s2 = if (side2) 1 else 0
            val c = if (corner) 1 else 0
            s1 + s2 + c
        }

        return AmbientOcclusion.evaluate(level)
    }


}