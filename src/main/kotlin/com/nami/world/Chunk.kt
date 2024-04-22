package com.nami.world

import com.nami.shader.Shader
import com.nami.toIntArray
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.opengl.GL33.*
import kotlin.math.roundToInt

class Chunk(val world: World, val x: Int, val z: Int, noise1: JNoise, noise2: JNoise) {

    private val log = KotlinLogging.logger { }

    companion object {
        @JvmStatic
        val width = 16

        @JvmStatic
        val height = 64

        @JvmStatic
        val depth = 16
    }

    val blocks = mutableMapOf<Vector3i, Block>()
    private val blocksLast = mutableMapOf<Vector3i, Block>()

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()
    var indices = 0

    init {
        //Generate blocks
        run {
            for (x in 0 until width)
                for (z in 0 until depth) {
                    var height =
                        ((noise1.evaluateNoise(
                            (x + this.x * width).toDouble(),
                            (z + this.z * depth).toDouble()
                        ) + (noise2.evaluateNoise(
                            (x + this.x * width).toDouble(),
                            (z + this.z * depth).toDouble()
                        ) + 1) / 3) * height).roundToInt()
                    height = height.coerceIn(1, Chunk.height)

                    for (y in 0 until height) {
                        val block: Block = if (y <= 0) {
                            BlockOneColor(Vector3f(0f, 0f, .75f))
                        } else if (y == 1) {
                            BlockOneColor(Vector3f(0.75f, 0.75f, 0f))
                        } else if (y > Chunk.height - Chunk.height / 3f) {
                            BlockOneColor(Vector3f(1f, 1f, 1f).mul(0.75f))
                        } else {
                            if (y == height - 1) {
                                BlockTopAndSideColor(
                                    colorTop = Vector3f(
                                        0f,
                                        0.6f,
                                        0f
                                    ),
                                    colorSide = Vector3f(
                                        196f / 255f,
                                        164f / 255f,
                                        132f / 255f
                                    ).mul(0.75f)
                                )
                            } else if (y >= height - 4) {
                                BlockOneColor(Vector3f(196f / 255f, 164f / 255f, 132f / 255f).mul(0.75f))
                            } else {
                                BlockOneColor(Vector3f(1f, 1f, 1f).mul(0.5f))
                            }

                        }

                        val vec = Vector3f(
                            (Math.random() * (1.0 - 0.65) + 0.91).toFloat()
                        )

                        block.colorTop.mul(vec)
                        block.colorBottom.mul(vec)
                        block.colorLeft.mul(vec)
                        block.colorRight.mul(vec)
                        block.colorFront.mul(vec)
                        block.colorBack.mul(vec)

                        blocks[Vector3i(x, if (y == 0) 1 else y, z)] = block
                    }
                }
        }
    }

    fun update() {
        if (blocks != blocksLast) {
            updateMesh()

            blocksLast.clear()
            blocks.forEach { (key, value) -> blocksLast[Vector3i(key)] = value }
        }
    }

    private fun updateMesh() {
        val generate = mutableMapOf<Vector3i, List<Face>>()
        blocks.keys.forEach { position ->
            val faces = mutableListOf<Face>()

            if (position.x <= 0) {
                faces.add(Face.LEFT)
            } else {
                val pos = Vector3i(position).add(-1, 0, 0)
                if (!blocks.containsKey(pos))
                    faces.add(Face.LEFT)
            }

            if (position.x >= width - 1) {
                faces.add(Face.RIGHT)
            } else {
                val pos = Vector3i(position).add(1, 0, 0)
                if (!blocks.containsKey(pos))
                    faces.add(Face.RIGHT)
            }

            if (position.y >= height - 1) {
                faces.add(Face.TOP)
            } else {
                val pos = Vector3i(position).add(0, 1, 0)
                if (!blocks.containsKey(pos))
                    faces.add(Face.TOP)
            }

            if (position.y <= 0) {
                faces.add(Face.BOTTOM)
            } else {
                val pos = Vector3i(position).add(0, -1, 0)
                if (!blocks.containsKey(pos))
                    faces.add(Face.BOTTOM)
            }

            if (position.z <= 0) {
                faces.add(Face.FRONT)
            } else {
                val pos = Vector3i(position).add(0, 0, -1)
                if (!blocks.containsKey(pos))
                    faces.add(Face.FRONT)
            }

            if (position.z >= depth - 1) {
                faces.add(Face.BACK)
            } else {
                val pos = Vector3i(position).add(0, 0, 1)
                if (!blocks.containsKey(pos))
                    faces.add(Face.BACK)
            }

            generate[position] = faces
        }

        val vertices = mutableListOf<Vertex>()
        val indices = mutableListOf<Int>()
        generate.forEach { (position, faces) ->
            faces.forEach { face ->
                val color = when (face) {
                    Face.TOP -> blocks[position]!!.colorTop
                    Face.BOTTOM -> blocks[position]!!.colorBottom
                    Face.LEFT -> blocks[position]!!.colorLeft
                    Face.RIGHT -> blocks[position]!!.colorRight
                    Face.FRONT -> blocks[position]!!.colorFront
                    Face.BACK -> blocks[position]!!.colorBack
                }
                val normal = Vector3f(face.normal)

                for (i in 0 until Face.NUM_TRIANGLES) {
                    val faceIndices = face.indices[i].toIntArray()

                    for (j in 0 until 3) {
                        val pos = Vector3f(Face.positions[faceIndices[j]]).add(Vector3f(position))
                        vertices.add(Vertex(pos, color, normal))
                        indices.add(vertices.size - 1)
                    }
                }
            }
        }

        this.indices = indices.size

        val data = mutableListOf<Float>()
        vertices.forEach { v -> data.addAll(v.toFloatArray().toList()) }

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data.toFloatArray(), GL_DYNAMIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE_BYTES, 0L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.SIZE_BYTES, 3L * Float.SIZE_BYTES)

        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE_BYTES, 6L * Float.SIZE_BYTES)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.toIntArray(), GL_DYNAMIC_DRAW)

        glBindVertexArray(0)
    }

    fun render(shader: Shader) {
        shader.bind()
        shader.uniform.set("u_model_matrix", Matrix4f().translate((x * width).toFloat(), 0f, (z * depth).toFloat()))

        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    enum class Face(val normal: Vector3f, val indices: Array<Vector3i>) {
        TOP(
            Vector3f(0.0f, 1.0f, 0.0f),
            arrayOf(
                Vector3i(4, 2, 0),
                Vector3i(2, 4, 6)
            )
        ),
        BOTTOM(
            Vector3f(0.0f, -1.0f, 0.0f),
            arrayOf(
                Vector3i(7, 1, 3),
                Vector3i(1, 7, 5)
            )
        ),

        LEFT(
            Vector3f(-1.0f, 0.0f, 0.0f),
            arrayOf(
                Vector3i(6, 5, 7),
                Vector3i(5, 6, 4)
            )
        ),
        RIGHT(
            Vector3f(1.0f, 0.0f, 0.0f),
            arrayOf(
                Vector3i(3, 0, 2),
                Vector3i(0, 3, 1)
            )
        ),

        FRONT(
            Vector3f(0.0f, 0.0f, 1.0f),
            arrayOf(
                Vector3i(4, 1, 5),
                Vector3i(1, 4, 0)
            )
        ),
        BACK(
            Vector3f(0.0f, 0.0f, -1.0f),
            arrayOf(
                Vector3i(2, 7, 3),
                Vector3i(7, 2, 6)
            )
        );

        companion object {
            val positions: Array<Vector3f> = arrayOf(
                Vector3f(1f, 1f, 0f),   // Right Top Back
                Vector3f(1f, 0f, 0f),   // Right Bottom Back
                Vector3f(1f, 1f, 1f),   // Right Top Front
                Vector3f(1f, 0f, 1f),   // Right Bottom Front

                Vector3f(0f, 1f, 0f),   //Left Top Back
                Vector3f(0f, 0f, 0f),   //Left Bottom Back
                Vector3f(0f, 1f, 1f),   //Left Top Front
                Vector3f(0f, 0f, 1f),   //Left Bottom Front
            )

            const val NUM_TRIANGLES = 2
        }
    }

}