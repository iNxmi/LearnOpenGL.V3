package com.nami.world.chunk

import com.nami.shader.Shader
import com.nami.toIntArray
import com.nami.world.*
import com.nami.world.biome.Biome
import com.nami.world.biome.BiomeGenerator
import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.*
import java.text.NumberFormat
import kotlin.math.roundToInt


class Chunk(val world: World, val position: Vector2i, val generator: ChunkGenerator) {

    private val log = KotlinLogging.logger { }

    companion object {
        @JvmStatic
        val width = 16

        @JvmStatic
        val height = 256

        @JvmStatic
        val depth = 16
    }

    private val biomes = mutableMapOf<Vector2i, Biome>()
    private val blocks = mutableMapOf<Vector3i, Block>()

    val vao = glGenVertexArrays()
    val vbo = glGenBuffers()
    val ebo = glGenBuffers()
    var indices = 0

    init {
        //Generate blocks
        run {
            for (x in 0 until width)
                for (z in 0 until depth) {
                    val worldPosition = Vector2i(
                        x + position.x * width,
                        z + position.y * depth
                    )

                    val elevation = generator.elevationNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()
                    val moisture = generator.moistureNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()
                    val temperature = generator.temperatureNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()

                    val factors = Vector3f(
                        elevation * (Biome.elevationRange.endInclusive - Biome.elevationRange.start) + Biome.elevationRange.start,
                        moisture * (Biome.moistureRange.endInclusive - Biome.moistureRange.start) + Biome.moistureRange.start,
                        temperature * (Biome.temperatureRange.endInclusive - Biome.temperatureRange.start) + Biome.temperatureRange.start
                    )

                    val biome = Biome.evaluate(factors)
                    biomes[Vector2i(x, z)] = biome

                    val column = biome.generateColumn(worldPosition)
                    column.forEach { (y, template) -> blocks[Vector3i(x, y, z)] = template.create() }
                }

            //Carve Caves
//            run {
//                val iterator = blocks.keys.iterator()
//                while (iterator.hasNext()) {
//                    val chunkRelativeBlockPosition = iterator.next()
//
//                    if (chunkRelativeBlockPosition.y == 1)
//                        continue
//
//                    val value = generator.caveNoise.evaluateNoise(
//                        (chunkRelativeBlockPosition.x + position.x * width).toDouble(),
//                        (chunkRelativeBlockPosition.y).toDouble(),
//                        (chunkRelativeBlockPosition.z + position.y * depth).toDouble()
//                    )
//
//                    if ((0.425f..0.575f).contains(value))
//                        iterator.remove()
//                }
//            }

            //Place Trees
            run {
                for (x in 0 until width)
                    for (z in 0 until depth) {
                        val height = getHeight(Vector2i(x, z), Chunk.height)
                        val block = getBlock(Vector3i(x, height, z))?.template
                        val biome = getBiome(Vector2i(x, z))?.generator

                        if (biome == Biome.normal && block == Block.grass) {
                            val noise = generator.treeNoise.evaluateNoise(
                                (x + position.x * width).toDouble(),
                                (z + position.y * depth).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise)) {
                                for (i in 0 until 5) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.log.create()

                                    if (i >= 2) {
                                        blocks[Vector3i(x - 1, y, z)] = Block.leaves.create()
                                        blocks[Vector3i(x + 1, y, z)] = Block.leaves.create()
                                        blocks[Vector3i(x, y, z - 1)] = Block.leaves.create()
                                        blocks[Vector3i(x, y, z + 1)] = Block.leaves.create()
                                    }
                                }

                                blocks[Vector3i(x, height + 6, z)] = Block.leaves.create()
                            }
                        } else if (biome == Biome.desert && block == Block.sand) {
                            val noise = generator.treeNoise.evaluateNoise(
                                (x + position.x * width).toDouble(),
                                (z + position.y * depth).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise))
                                for (i in 0 until (Math.random() * 2).roundToInt() + 2) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.cactus.create()
                                }

                        } else if (biome == Biome.snow && block == Block.snow) {
                            val noise = generator.treeNoise.evaluateNoise(
                                (x + position.x * width).toDouble(),
                                (z + position.y * depth).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise)) {
                                for (i in 0 until 8) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.log.create()

                                    if (i >= 3) {
                                        blocks[Vector3i(x - 1, y, z)] = Block.snow.create()
                                        blocks[Vector3i(x + 1, y, z)] = Block.snow.create()
                                        blocks[Vector3i(x, y, z - 1)] = Block.snow.create()
                                        blocks[Vector3i(x, y, z + 1)] = Block.snow.create()

                                        if (i == 4 || i == 5) {
                                            blocks[Vector3i(x - 2, y, z)] = Block.snow.create()
                                            blocks[Vector3i(x + 2, y, z)] = Block.snow.create()
                                            blocks[Vector3i(x, y, z - 2)] = Block.snow.create()
                                            blocks[Vector3i(x, y, z + 2)] = Block.snow.create()

                                            blocks[Vector3i(x - 1, y, z - 1)] = Block.snow.create()
                                            blocks[Vector3i(x + 1, y, z - 1)] = Block.snow.create()
                                            blocks[Vector3i(x - 1, y, z + 1)] = Block.snow.create()
                                            blocks[Vector3i(x + 1, y, z + 1)] = Block.snow.create()
                                        }
                                    }
                                }

                                blocks[Vector3i(x, height + 9, z)] = Block.snow.create()
                            }
                        }
                    }
            }

            updateMesh()
        }
    }

    fun getHeight(chunkRelativeBlockPosition: Vector2i, start: Int): Int {
        for (y in start downTo 0)
            if (getBlock(Vector3i(chunkRelativeBlockPosition.x, y, chunkRelativeBlockPosition.y)) != null)
                return y

        return 0
    }

    fun getBlock(chunkRelativeBlockPosition: Vector3i): Block? {
        return blocks[chunkRelativeBlockPosition]
    }

    fun setBlocks(vararg blocks: Pair<Vector3i, Block?>) {
        var front = false
        var back = false
        var left = false
        var right = false

        for (pair in blocks) {
            val chunkRelativeBlockPosition = pair.first

            if (chunkRelativeBlockPosition.x == 0)
                left = true

            if (chunkRelativeBlockPosition.x == width - 1)
                right = true

            if (chunkRelativeBlockPosition.z == 0)
                back = true

            if (chunkRelativeBlockPosition.z == depth - 1)
                front = true

            val block = pair.second

            if (block == null) {
                this.blocks.remove(chunkRelativeBlockPosition)
                continue
            }

            this.blocks[chunkRelativeBlockPosition] = block
        }

        updateMesh()

        if (front) {
            val chunkNorth = world.chunkManager.get(Vector2i(position).add(0, 1))
            chunkNorth?.updateMesh()
        }

        if (left) {
            val chunkEast = world.chunkManager.get(Vector2i(position).add(-1, 0))
            chunkEast?.updateMesh()
        }

        if (back) {
            val chunkSouth = world.chunkManager.get(Vector2i(position).add(0, -1))
            chunkSouth?.updateMesh()
        }

        if (right) {
            val chunkWest = world.chunkManager.get(Vector2i(position).add(1, 0))
            chunkWest?.updateMesh()
        }
    }

    fun getBiome(chunkRelativeBlockPosition: Vector2i): Biome? {
        return biomes[chunkRelativeBlockPosition]
    }

    fun update() {

    }

    fun updateMesh() {
        val generate = mutableMapOf<Vector3i, List<Face>>()
        blocks.keys.forEach { chunkBlockPosition ->
            val worldBlockPos =
                Vector3i(chunkBlockPosition).add(Vector3i(position.x, 0, position.y).mul(width, 0, depth))

            val faces = mutableListOf<Face>()

            var pos = Vector3i(chunkBlockPosition).add(-1, 0, 0)
            if ((0 until width).contains(pos.x)) {
                if (getBlock(pos) == null)
                    faces.add(Face.LEFT)
            } else {
                if (world.getBlock(Vector3i(worldBlockPos).add(-1, 0, 0)) == null)
                    faces.add(Face.LEFT)
            }

            pos = Vector3i(chunkBlockPosition).add(1, 0, 0)
            if ((0 until width).contains(pos.x)) {
                if (getBlock(pos) == null)
                    faces.add(Face.RIGHT)
            } else {
                if (world.getBlock(Vector3i(worldBlockPos).add(1, 0, 0)) == null)
                    faces.add(Face.RIGHT)
            }

            if (getBlock(Vector3i(chunkBlockPosition).add(0, 1, 0)) == null)
                faces.add(Face.TOP)

            if (getBlock(Vector3i(chunkBlockPosition).add(0, -1, 0)) == null)
                faces.add(Face.BOTTOM)

            pos = Vector3i(chunkBlockPosition).add(0, 0, -1)
            if ((0 until depth).contains(pos.z)) {
                if (getBlock(pos) == null)
                    faces.add(Face.FRONT)
            } else {
                if (world.getBlock(Vector3i(worldBlockPos).add(0, 0, -1)) == null)
                    faces.add(Face.FRONT)
            }

            pos = Vector3i(chunkBlockPosition).add(0, 0, 1)
            if ((0 until depth).contains(pos.z)) {
                if (getBlock(pos) == null)
                    faces.add(Face.BACK)
            } else {
                if (world.getBlock(Vector3i(worldBlockPos).add(0, 0, 1)) == null)
                    faces.add(Face.BACK)
            }

            generate[chunkBlockPosition] = faces
        }

        val vertices = mutableListOf<Vertex>()
        val indices = mutableListOf<Int>()
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
                val normal = Vector3f(face.normal)

                for (i in 0 until Face.NUM_TRIANGLES) {
                    val faceIndices = face.indices[i].toIntArray()

                    for (j in 0 until 3) {
                        val pos = Vector3f(Face.positions[faceIndices[j]]).add(Vector3f(chunkRelativeBlockPosition))
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
        shader.uniform.set(
            "u_model_matrix",
            Matrix4f().translate(
                (position.x * width).toFloat(),
                0f,
                (position.y * depth).toFloat()
            )
        )

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