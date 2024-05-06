package com.nami.world.chunk

import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.biome.BiomeTemplate
import com.nami.world.block.Block
import com.nami.world.block.Block.Companion.MUSHROOM_BLOCK_RED
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random


class Chunk(val world: World, val position: Vector3i) {

    private val log = KotlinLogging.logger { }

    private val blockManager = world.blockManager
    private val chunkManager = world.chunkManager
    private val noiseGenerators = world.noiseGenerators

    companion object {
        @JvmStatic
        val SIZE = Vector3i(16, 16, 16)
    }

    private val biomes = mutableMapOf<Vector2i, Biome>()

    val meshTerrain = ChunkMesh(this, Block.Type.SOLID)
    val meshFluid = ChunkMesh(this, Block.Type.FLUID)
    val meshFoliage = ChunkMesh(this, Block.Type.FOLIAGE)

    init {
        //Generate blocks
        for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x))
            for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {
                val blockPosition = Vector2i(x, z)

                val elevation = noiseGenerators.elevationNoise.evaluateNoise(
                    blockPosition.x.toDouble(),
                    blockPosition.y.toDouble()
                ).toFloat()
                val moisture = noiseGenerators.moistureNoise.evaluateNoise(
                    blockPosition.x.toDouble(),
                    blockPosition.y.toDouble()
                ).toFloat()
                val temperature = noiseGenerators.temperatureNoise.evaluateNoise(
                    blockPosition.x.toDouble(),
                    blockPosition.y.toDouble()
                ).toFloat()

                val biome = Biome.evaluate(Vector3f(elevation, moisture, temperature))
                biomes[Vector2i(x, z)] = biome

                for (y in (position.y * SIZE.y) until ((position.y + 1) * SIZE.y)) {
                    val blockPosition2 = Vector3i(x, y, z)

                    val value = noiseGenerators.caveNoise.evaluateNoise(
                        blockPosition2.x.toDouble(),
                        blockPosition2.y.toDouble(),
                        blockPosition2.z.toDouble()
                    )

                    if ((0.425f..0.575f).contains(value))
                        continue

                    val block = biome.generate(blockPosition2)

                    if (block != null)
                        blockManager.setBlock(blockPosition2, block, false)
                }
            }

        //Carve Caves
//        for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x))
//            for (y in (position.y * SIZE.y) until ((position.y + 1) * SIZE.y))
//                for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {
//
//                    val r = 2
//                    var max = 0f
//                    for (za in z - r until z + r)
//                        for (ya in y - r until y + r)
//                            for (xa in x - r until x + r) {
//                                val noise = noiseGenerators.caveNoise.evaluateNoise(
//                                    xa.toDouble(),
//                                    ya.toDouble(),
//                                    za.toDouble(),
//                                ).toFloat()
//
//                                max = max(max, noise)
//                            }
//
//
//                    if (noiseGenerators.caveNoise.evaluateNoise(
//                            x.toDouble(),
//                            y.toDouble(),
//                            z.toDouble(),
//                        ).toFloat() == max
//                    ) {
//                        val position = Vector3i(x, y, z)
//                        for (i in 0 until 10) {
//                            val rand = noiseGenerators.treeRandom.nextFloat()
//
//                            if ((0f..(1f / 6f)).contains(rand))
//                                position.y += 1
//
//                            if (((1f / 6f)..(2f / 6f)).contains(rand))
//                                position.x += 1
//                            if (((2f / 6f)..(3f / 6f)).contains(rand))
//                                position.x -= 1
//
//                            if (((3f / 6f)..(4f / 6f)).contains(rand))
//                                position.z += 1
//                            if (((4f / 6f)..(5f / 6f)).contains(rand))
//                                position.z -= 1
//
//                            if (((5f / 6f)..(6f / 6f)).contains(rand))
//                                position.y -= 1
//
//                            val radius = 2
//                            for (d in -radius..radius)
//                                for (h in -radius..radius)
//                                    for (w in -radius..radius)
//                                        blockManager.setBlock(Vector3i(position).add(w, h, d), null, false)
//                        }
//                    }
//
//                }

        //Place Trees
        for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x))
            for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {

                val r = when (getBiome(Vector2i(x, z))?.template) {
                    Biome.DESERT -> 4
                    Biome.SNOW -> 4
                    Biome.NORMAL -> 4
                    Biome.MUSHROOM -> 5
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
        val height = blockManager.getHeight(Vector2i(x, z), 512)

        val block = blockManager.getBlock(Vector3i(x, height, z))?.template
        val biome = getBiome(Vector2i(x, z))?.template

        if (biome == Biome.NORMAL && block == Block.GRASS) {
            val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 2 + 4).roundToInt()
            for (i in 0 until baseHeight)
                blockManager.setBlock(Vector3i(x, height + 1 + i, z), Block.LOG, false)

            for (i in 0 until 3) {
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

                    blockManager.setBlock(Vector3i(position), Block.LOG, false)

                    if (blockManager.getBlock(Vector3i(position).add(1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(1, 0, 0), Block.LEAVES, false)
                    if (blockManager.getBlock(Vector3i(position).add(-1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(-1, 0, 0), Block.LEAVES, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 1, 0), Block.LEAVES, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, -1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, -1, 0), Block.LEAVES, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, 1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, 1), Block.LEAVES, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, -1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, -1), Block.LEAVES, false)
                }
            }
        } else if (biome == Biome.DESERT && block == Block.SAND) {
            for (i in 0 until (Math.random() * 2).roundToInt() + 2) {
                val y = height + i + 1
                blockManager.setBlock(Vector3i(x, y, z), Block.CACTUS, false)
            }
        } else if (biome == Biome.SNOW && block == Block.SNOW) {
            val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 4 + 4).roundToInt()
            for (i in 0 until baseHeight)
                blockManager.setBlock(Vector3i(x, height + 1 + i, z), Block.LOG, false)

            for (i in 0 until 3) {
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

                    blockManager.setBlock(Vector3i(position), Block.LOG, false)

                    if (blockManager.getBlock(Vector3i(position).add(1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(1, 0, 0), Block.LEAVES_SNOW, false)
                    if (blockManager.getBlock(Vector3i(position).add(-1, 0, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(-1, 0, 0), Block.LEAVES_SNOW, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 1, 0), Block.LEAVES_SNOW, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, -1, 0)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, -1, 0), Block.LEAVES_SNOW, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, 1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, 1), Block.LEAVES_SNOW, false)
                    if (blockManager.getBlock(Vector3i(position).add(0, 0, -1)) == null)
                        blockManager.setBlock(Vector3i(position).add(0, 0, -1), Block.LEAVES_SNOW, false)
                }
            }
        } else if (biome == Biome.MUSHROOM && block == Block.MYCELIUM) {

            val stemHeight = (Math.random() * 4).roundToInt() + 7
            for (i in 0 until stemHeight) {
                val y = height + i + 1
                blockManager.setBlock(Vector3i(x, y, z), Block.MUSHROOM_STEM, false)

                if (i == stemHeight - 1) {
                    blockManager.setBlock(Vector3i(x - 1, y + 1, z), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x - 1, y + 1, z + 1), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x - 1, y + 1, z + 2), MUSHROOM_BLOCK_RED, false)

                    blockManager.setBlock(Vector3i(x - 2, y + 1, z), MUSHROOM_BLOCK_RED, false)

                    blockManager.setBlock(Vector3i(x - 3, y + 1, z), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x - 3, y + 1, z + 1), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x - 3, y + 1, z + 2), MUSHROOM_BLOCK_RED, false)

                    blockManager.setBlock(Vector3i(x + 1, y + 1, z), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x + 2, y + 1, z), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x + 3, y + 1, z), MUSHROOM_BLOCK_RED, false)

                    blockManager.setBlock(Vector3i(x, y + 1, z - 1), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x, y + 1, z - 2), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x, y + 1, z - 3), MUSHROOM_BLOCK_RED, false)

                    blockManager.setBlock(Vector3i(x, y + 1, z + 1), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x, y + 1, z + 2), MUSHROOM_BLOCK_RED, false)
                    blockManager.setBlock(Vector3i(x, y + 1, z + 3), MUSHROOM_BLOCK_RED, false)
                }
            }
        }
    }

    fun getBiome(chunkRelativeBlockPosition: Vector2i): Biome? {
        return biomes[chunkRelativeBlockPosition]
    }

    fun update() {

    }

    fun updateMesh() {
        meshTerrain.update()
        meshFoliage.update()
        meshFluid.update()
    }

}