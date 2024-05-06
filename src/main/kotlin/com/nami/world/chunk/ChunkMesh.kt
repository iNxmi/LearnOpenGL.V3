package com.nami.world.chunk

import com.nami.toIntArray
import com.nami.world.block.Block
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import kotlin.math.roundToInt


class ChunkMesh(private val chunk: Chunk, val blockType: Block.Type) {

    val world = chunk.world
    val blockManager = world.blockManager
    val chunkManager = world.chunkManager

    var vao = 0
        private set
    var vbo = 0
        private set
    var ebo = 0
        private set
    var indicesCount = 0
        private set

    private fun face(
        block: Block,
        position: Vector3i,
        offset: Vector3i
    ): Boolean {
        if (chunkManager.getByBlockPosition(Vector3i(position).add(offset)) == null)
            return false

        val adjacentBlock = blockManager.getBlock(Vector3i(position).add(offset)) ?: return true

        return block.template.type != adjacentBlock.template.type
    }

    fun update() {
        val blocks = mutableMapOf<Vector3i, Block>()
        for (z in (chunk.position.z * Chunk.SIZE.z) until ((chunk.position.z + 1) * Chunk.SIZE.z))
            for (y in (chunk.position.y * Chunk.SIZE.y) until ((chunk.position.y + 1) * Chunk.SIZE.y))
                for (x in (chunk.position.x * Chunk.SIZE.x) until ((chunk.position.x + 1) * Chunk.SIZE.x)) {
                    val position = Vector3i(x, y, z)

                    val block = blockManager.getBlock(position) ?: continue
                    if (block.template.type != blockType) continue

                    blocks[position] = block
                }

        val generate = mutableListOf<Pair<Vector3i, Face>>()
        blocks.forEach { (position, block) ->
            if (block == null)
                return@forEach

            if (face(block, position, Vector3i(-1, 0, 0)))
                generate.add(Pair(position, Face.LEFT))
            if (face(block, position, Vector3i(1, 0, 0)))
                generate.add(Pair(position, Face.RIGHT))

            if (face(block, position, Vector3i(0, 1, 0)))
                generate.add(Pair(position, Face.TOP))
            if (face(block, position, Vector3i(0, -1, 0)))
                generate.add(Pair(position, Face.BOTTOM))

            if (face(block, position, Vector3i(0, 0, -1)))
                generate.add(Pair(position, Face.FRONT))
            if (face(block, position, Vector3i(0, 0, 1)))
                generate.add(Pair(position, Face.BACK))
        }

        if (generate.size <= 0)
            return

        val vertices = MemoryUtil.memAllocFloat(generate.size * Face.NUM_TRIANGLES * 3)
        val indices = MemoryUtil.memAllocInt(generate.size * Face.NUM_TRIANGLES * 3)
        generate.withIndex().forEach { (i, pair) ->
            val position = pair.first
            val chunkBlockPosition = Vector3i(position).sub(Vector3i(chunk.position).mul(Chunk.SIZE))
            val face = pair.second

            val block = blocks[position]!!
            val color = when (face) {
                Face.TOP -> block.template.colorTop
                Face.BOTTOM -> block.template.colorBottom
                Face.LEFT -> block.template.colorLeft
                Face.RIGHT -> block.template.colorRight
                Face.FRONT -> block.template.colorFront
                Face.BACK -> block.template.colorBack
            }.id
            val mutation = block.mutation
            val normal = face.normal

            for (j in 0 until Face.NUM_TRIANGLES) {
                val faceIndices = face.indices[j].toIntArray()

                for (k in 0 until 3) {
                    val pos = Vector3i(Face.positions[faceIndices[k]]).add(chunkBlockPosition)

                    var data = 0
                    data += pos.x shl 0
                    data += pos.y shl 6
                    data += pos.z shl 12
                    data += color shl 18
                    data += (mutation * 15).roundToInt() shl 24
                    data += normal shl 29
                    vertices.put(java.lang.Float.intBitsToFloat(data))

                    indices.put(i * 6 + j * 3 + k)
                }
            }
        }

        vertices.flip()
        indices.flip()

        chunkManager.sendToGPU(chunk.position
        ) {
            if (vao == 0) vao = glGenVertexArrays()
            glBindVertexArray(vao)

            if (vbo == 0) vbo = glGenBuffers()
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW)

            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 1, GL_FLOAT, false, 1 * Float.SIZE_BYTES, 0L * Float.SIZE_BYTES)

            if (ebo == 0) ebo = glGenBuffers()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW)

            glBindVertexArray(0)

            indicesCount = indices.limit()
        }
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