package com.nami.world

import com.nami.entity.Entity
import com.nami.entity.Transform
import com.nami.mesh.Mesh
import com.nami.model.Material
import com.nami.model.Model
import com.nami.register.Register
import com.nami.shader.Shader
import com.nami.texture.Texture
import mu.KotlinLogging
import org.joml.Vector3f
import org.joml.Vector3i

class Chunk {

    private val log = KotlinLogging.logger { }

    companion object {
        @JvmStatic
        val width = 16

        @JvmStatic
        val height = 16

        @JvmStatic
        val depth = 16
    }

    val blocks = mutableMapOf<Vector3i, Vector3f>()
    private val blocksLast = mutableMapOf<Vector3i, Vector3f>()

    var mesh: Mesh? = null

    fun update() {
        if (blocks != blocksLast) {
            generateMesh()

            blocksLast.clear()
            blocks.forEach { (key, value) ->
                blocksLast[Vector3i(key)] = value
            }
        }
    }


    fun render() {
        mesh?.render()
    }

    private fun generateMesh() {
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

        val positions = mutableListOf<Float>()
        val colors = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val indices = mutableListOf<Int>()

        var count = 0
        generate.forEach { (position, faces) ->
            faces.forEach { face ->
                val i = count
                positions.addAll(
                    listOf(
                        Face.positions[face.indices[0]][0] + position.x,
                        Face.positions[face.indices[0]][1] + position.y,
                        Face.positions[face.indices[0]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)
                positions.addAll(
                    listOf(
                        Face.positions[face.indices[1]][0] + position.x,
                        Face.positions[face.indices[1]][1] + position.y,
                        Face.positions[face.indices[1]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)
                positions.addAll(
                    listOf(
                        Face.positions[face.indices[2]][0] + position.x,
                        Face.positions[face.indices[2]][1] + position.y,
                        Face.positions[face.indices[2]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)

                indices.add(i * 6 + 0)
                indices.add(i * 6 + 1)
                indices.add(i * 6 + 2)

                normals.addAll(face.normal.toList())
                normals.addAll(face.normal.toList())
                normals.addAll(face.normal.toList())

                positions.addAll(
                    listOf(
                        Face.positions[face.indices[3]][0] + position.x,
                        Face.positions[face.indices[3]][1] + position.y,
                        Face.positions[face.indices[3]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)
                positions.addAll(
                    listOf(
                        Face.positions[face.indices[4]][0] + position.x,
                        Face.positions[face.indices[4]][1] + position.y,
                        Face.positions[face.indices[4]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)
                positions.addAll(
                    listOf(
                        Face.positions[face.indices[5]][0] + position.x,
                        Face.positions[face.indices[5]][1] + position.y,
                        Face.positions[face.indices[5]][2] + position.z
                    )
                )
                colors.add(blocks[position]!!.x)
                colors.add(blocks[position]!!.y)
                colors.add(blocks[position]!!.z)

                indices.add(i * 6 + 3)
                indices.add(i * 6 + 4)
                indices.add(i * 6 + 5)

                normals.addAll(face.normal.toList())
                normals.addAll(face.normal.toList())
                normals.addAll(face.normal.toList())

                count++
            }

        }

        mesh = Mesh(
            positions.toFloatArray(),
            colors.toFloatArray(),
            normals.toFloatArray(),
            null,
            indices.toIntArray()
        )
    }

    enum class Face(val normal: FloatArray, val indices: IntArray) {
        TOP(
            floatArrayOf(
                0.0f, 1.0f, 0.0f
            ),
            intArrayOf(
                4, 2, 0,
                2, 4, 6
            )
        ),
        BOTTOM(
            floatArrayOf(
                0.0f, -1.0f, 0.0f
            ),
            intArrayOf(
                7, 1, 3,
                1, 7, 5
            )
        ),

        LEFT(
            floatArrayOf(
                -1.0f, 0.0f, 0.0f
            ),
            intArrayOf(
                6, 5, 7,
                5, 6, 4
            )
        ),
        RIGHT(
            floatArrayOf(
                1.0f, 0.0f, 0.0f
            ),
            intArrayOf(
                3, 0, 2,
                0, 3, 1
            )
        ),

        FRONT(
            floatArrayOf(
                0.0f, 0.0f, 1.0f
            ),
            intArrayOf(
                4, 1, 5,
                1, 4, 0
            )
        ),
        BACK(
            floatArrayOf(
                0.0f, 0.0f, -1.0f,
            ),
            intArrayOf(
                2, 7, 3,
                7, 2, 6
            )
        );

        companion object {
            val positions = arrayOf(
                floatArrayOf(1f, 1f, 0f),   // Right Top Back
                floatArrayOf(1f, 0f, 0f),   // Right Bottom Back
                floatArrayOf(1f, 1f, 1f),   // Right Top Front
                floatArrayOf(1f, 0f, 1f),   // Right Bottom Front

                floatArrayOf(0f, 1f, 0f),   //Left Top Back
                floatArrayOf(0f, 0f, 0f),   //Left Bottom Back
                floatArrayOf(0f, 1f, 1f),   //Left Top Front
                floatArrayOf(0f, 0f, 1f),   //Left Bottom Front
            )
        }

    }

}