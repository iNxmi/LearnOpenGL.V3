package com.nami.world.chunk

import com.nami.toIntArray
import com.nami.world.block.Block
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryStack
import java.awt.Color
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.roundToInt


class ChunkMesh(private val chunk: Chunk) {

    var vao = glGenVertexArrays()
    var vbo = glGenBuffers()
    var ebo = glGenBuffers()
    var indicesCount = 0

    private fun face(
        blocks: Map<Vector3i, Block>,
        position: Vector3i,
        offset: Vector3i
    ): Boolean {
        val adjacentPosition = Vector3i(position).add(offset)
        val adjacentBlock: Block? = if (
            (0 until Chunk.SIZE.x).contains(adjacentPosition.x) &&
            (0 until Chunk.SIZE.y).contains(adjacentPosition.y) &&
            (0 until Chunk.SIZE.z).contains(adjacentPosition.z)
        ) {
            blocks[adjacentPosition]
        } else {
            val worldAdjacentPosition = Vector3i(position).add(Vector3i(chunk.position).mul(Chunk.SIZE).add(offset))
            chunk.world.getBlock(worldAdjacentPosition)
        }

        if (adjacentBlock == null) return true

        val block = blocks[position]!!
        if (block.template.type != adjacentBlock.template.type) return true

        return false
    }

    private val generate = mutableMapOf<Vector3i, List<Face>>()
    private val vertices = mutableListOf<Float>()
    private val indices = mutableListOf<Int>()
    fun update(blocks: Map<Vector3i, Block>) {
        generate.clear()
        for (x in 0 until Chunk.SIZE.x)
            for (y in 0 until Chunk.SIZE.y)
                for (z in 0 until Chunk.SIZE.z) {
                    val position = Vector3i(x, y, z)
                    if (!blocks.containsKey(position)) continue

                    val faces = mutableListOf<Face>()

                    if (face(blocks, position, Vector3i(-1, 0, 0)))
                        faces.add(Face.LEFT)
                    if (face(blocks, position, Vector3i(1, 0, 0)))
                        faces.add(Face.RIGHT)

                    if (face(blocks, position, Vector3i(0, 1, 0)))
                        faces.add(Face.TOP)
                    if (face(blocks, position, Vector3i(0, -1, 0)))
                        faces.add(Face.BOTTOM)

                    if (face(blocks, position, Vector3i(0, 0, -1)))
                        faces.add(Face.FRONT)
                    if (face(blocks, position, Vector3i(0, 0, 1)))
                        faces.add(Face.BACK)

                    generate[position] = faces
                }

        vertices.clear()
        indices.clear()
        generate.forEach { (chunkRelativeBlockPosition, faces) ->
            faces.forEach { face ->
                val color = when (face) {
                    Face.TOP -> blocks[chunkRelativeBlockPosition]!!.color.cTop
                    Face.BOTTOM -> blocks[chunkRelativeBlockPosition]!!.color.cBottom
                    Face.LEFT -> blocks[chunkRelativeBlockPosition]!!.color.cLeft
                    Face.RIGHT -> blocks[chunkRelativeBlockPosition]!!.color.cRight
                    Face.FRONT -> blocks[chunkRelativeBlockPosition]!!.color.cFront
                    Face.BACK -> blocks[chunkRelativeBlockPosition]!!.color.cBack
                }
                val normal = face.normal

                for (i in 0 until Face.NUM_TRIANGLES) {
                    val faceIndices = face.indices[i].toIntArray()

                    for (j in 0 until 3) {
                        indices.add(vertices.size / 2)

                        val pos = Vector3i(Face.positions[faceIndices[j]]).add(chunkRelativeBlockPosition)

                        var data = 0
                        data += pos.x shl 0
                        data += pos.y shl 5
                        data += pos.z shl 10
                        data += normal shl 15
                        vertices.add(java.lang.Float.intBitsToFloat(data))

                        val c = Color(
                            (color.x * 255f).roundToInt(),
                            (color.y * 255f).roundToInt(),
                            (color.z * 255f).roundToInt(),
                            (color.w * 255f).roundToInt()
                        )
                        vertices.add(java.lang.Float.intBitsToFloat(c.rgb))
                    }
                }
            }
        }

        indicesCount = indices.size

        MemoryStack.stackPush().use { stack ->
            val verticesBuffer = stack.callocFloat(vertices.size)
            vertices.forEach { value -> verticesBuffer.put(value) }

            val indicesBuffer = stack.callocInt(indices.size)
            indices.forEach { value -> indicesBuffer.put(value) }

            updateBuffers(verticesBuffer, indicesBuffer)
        }
    }

    private fun updateBuffers(data: FloatBuffer, indices: IntBuffer) {
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 1, GL_FLOAT, false, 8, 0L * 4)

        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 1, GL_FLOAT, false, 8, 1L * 4)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW)

        glBindVertexArray(0)
    }

    enum class Face(val normal: Int, val indices: Array<Vector3i>) {
        TOP(
            0,
            arrayOf(
                Vector3i(4, 2, 0),
                Vector3i(2, 4, 6)
            )
        ),
        BOTTOM(
            1,
            arrayOf(
                Vector3i(7, 1, 3),
                Vector3i(1, 7, 5)
            )
        ),

        LEFT(
            2,
            arrayOf(
                Vector3i(6, 5, 7),
                Vector3i(5, 6, 4)
            )
        ),
        RIGHT(
            3,
            arrayOf(
                Vector3i(3, 0, 2),
                Vector3i(0, 3, 1)
            )
        ),

        FRONT(
            4,
            arrayOf(
                Vector3i(4, 1, 5),
                Vector3i(1, 4, 0)
            )
        ),
        BACK(
            5,
            arrayOf(
                Vector3i(2, 7, 3),
                Vector3i(7, 2, 6)
            )
        );

        companion object {
            val positions: Array<Vector3i> = arrayOf(
                Vector3i(1, 1, 0),   // Right Top Back
                Vector3i(1, 0, 0),   // Right Bottom Back
                Vector3i(1, 1, 1),   // Right Top Front
                Vector3i(1, 0, 1),   // Right Bottom Front

                Vector3i(0, 1, 0),   //Left Top Back
                Vector3i(0, 0, 0),   //Left Bottom Back
                Vector3i(0, 1, 1),   //Left Top Front
                Vector3i(0, 0, 1),   //Left Bottom Front
            )

            const val NUM_TRIANGLES = 2
        }
    }

}