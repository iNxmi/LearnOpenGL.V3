package com.nami.world.chunk

import com.nami.world.World
import com.nami.world.block.Block
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.max
import kotlin.math.roundToInt


class Chunk(val world: World, val position: Vector3i) {

    private val log = KotlinLogging.logger { }

    private val biomeManager = world.biomeManager
    private val blockManager = world.blockManager

    private val noiseGenerators = world.noiseGenerators

    companion object {
        @JvmStatic
        val SIZE = Vector3i(16, 16, 16)
    }

    val meshes = mutableMapOf<Block.Layer, ChunkMesh>()

    init {
        Block.Layer.values().forEach { meshes[it] = ChunkMesh(this, it) }

        //Generate blocks
        for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x))
            for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {
                for (y in (position.y * SIZE.y) until ((position.y + 1) * SIZE.y)) {
                    val position = Vector3i(x, y, z)
                    biomeManager.generate(position)

                    val biome = biomeManager.getBiome(position) ?: continue
                    val block = biome.generate() ?: continue

                    blockManager.setBlock(position, block, false)
                }
            }

        //Place Trees
        for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x))
            for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {

                val height = blockManager.getHeight(Vector2i(x, z), 512, listOf(Block.Layer.SOLID))
                val r = when (biomeManager.getBiome(Vector3i(x, height, z))?.template?.id) {
                    "desert" -> 5
                    "snow_forest" -> 6
                    "forest" -> 5
                    "mushroom_forest" -> 5
                    "beach" -> 7
                    else -> 0
                }
                var max = 0f
                for (za in z - r until z + r)
                    for (xa in x - r until x + r) {
                        val noise = noiseGenerators.treeNoise.evaluateNoise(
                            xa.toDouble(),
                            za.toDouble(),
                        ).toFloat()

                        max = max(max, noise)
                    }

                if (noiseGenerators.treeNoise.evaluateNoise(
                        x.toDouble(),
                        z.toDouble(),
                    ).toFloat() == max
                )
                    placeTree(x, z)

            }

        updateMesh()
    }

    fun placeTree(x: Int, z: Int) {
        val height = blockManager.getHeight(Vector2i(x, z), 512, listOf(Block.Layer.SOLID))

        val block = blockManager.getBlock(Vector3i(x, height, z))
        val biome = biomeManager.getBiome(Vector3i(x, height, z))?.template

        if (biome?.id == "forest" && block?.template?.id == "grass") {
            val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 2 + 4).roundToInt()
            for (i in 0 until baseHeight)
                blockManager.setBlock(Vector3i(x, height + 1 + i, z), "log", false)

            for (i in 0 until 5) {
                val position = Vector3i(x, height + baseHeight, z)
                for (j in 5 until 15) {
                    val rand = noiseGenerators.treeRandom.nextFloat()

                    if ((0f..0.4f).contains(rand))
                        position.y += 1

                    if ((0.4f..0.55f).contains(rand))
                        position.x += 1
                    if ((0.55f..0.7f).contains(rand))
                        position.x -= 1

                    if ((0.7f..0.85f).contains(rand))
                        position.z += 1
                    if ((0.85f..1f).contains(rand))
                        position.z -= 1

                    blockManager.setBlock(Vector3i(position), "log", false)

                    if (blockManager.getBlock(Vector3i(position).add(1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(1, 0, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(-1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(-1, 0, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 1, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, -1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, -1, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, 1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, 1), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, -1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, -1), "leaves", false)
                }
            }
        } else if (biome?.id == "desert" && block?.template?.id == "sand") {
            for (i in 0 until (Math.random() * 2).roundToInt() + 2) {
                val y = height + i + 1
                blockManager.setBlock(Vector3i(x, y, z), "cactus", false)
            }
        } else if (biome?.id == "snow_forest" && block?.template?.id == "snow") {
            val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 4 + 4).roundToInt()
            for (i in 0 until baseHeight)
                blockManager.setBlock(Vector3i(x, height + 1 + i, z), "log", false)

            for (i in 0 until 5) {
                val position = Vector3i(x, height + baseHeight, z)
                for (j in 5 until 15) {
                    val rand = noiseGenerators.treeRandom.nextFloat()

                    if ((0f..0.6f).contains(rand))
                        position.y += 1

                    if ((0.6f..0.7f).contains(rand))
                        position.x += 1
                    if ((0.7f..0.8f).contains(rand))
                        position.x -= 1

                    if ((0.8f..0.9f).contains(rand))
                        position.z += 1
                    if ((0.9f..1f).contains(rand))
                        position.z -= 1

                    blockManager.setBlock(Vector3i(position), "log", false)

                    if (blockManager.getBlock(Vector3i(position).add(1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(1, 0, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(-1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(-1, 0, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 1, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, -1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, -1, 0), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, 1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, 1), "leaves", false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, -1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, -1), "leaves", false)
                }
            }
        } else if (biome?.id == "mushroom_forest" && block?.template?.id == "mycelium") {

            val stemHeight = (Math.random() * 4).roundToInt() + 7
            for (i in 0 until stemHeight) {
                val y = height + i + 1
                blockManager.setBlock(Vector3i(x, y, z), "mushroom_stem", false)

                val radius = (Math.random() * (4 - 2) + 2).roundToInt()
                if (i == stemHeight - 1) {
                    for (zo in -radius..radius)
                        for (xo in -radius..radius)
                            if (zo * zo + xo * xo <= radius * radius)
                                blockManager.setBlock(Vector3i(x + xo, y + 1, z + zo), "mushroom", false)
                }
            }
        }
    }

    fun update() {

    }

    fun updateMesh() {
        meshes.forEach { it.value.update() }
    }

}