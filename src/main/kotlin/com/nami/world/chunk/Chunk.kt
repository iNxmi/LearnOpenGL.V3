package com.nami.world.chunk

import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt


class Chunk(val world: World, val position: Vector3i) {

    private val log = KotlinLogging.logger { }

    companion object {
        @JvmStatic
        val SIZE = Vector3i(16, 16, 16)
    }

    private val elevationNoise =
        JNoise.newBuilder()
            .fastSimplex(
                FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed).build()
            )
            .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
            .scale(1 / 2048.0)
            .addModifier { v -> (v + 1) / 2.0 }
            .clamp(0.0, 1.0)
            .build()

    private val moistureNoise = JNoise.newBuilder()
        .fastSimplex(
            FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 1).build()
        )
        .octavate(6, 0.5, 3.0, FractalFunction.FBM, false)
        .scale(1 / 3072.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    private val temperatureNoise = JNoise.newBuilder()
        .fastSimplex(
            FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 2).build()
        )
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / 4096.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    private val caveNoise = JNoise.newBuilder()
        .worley(
            WorleyNoiseGenerator.newBuilder().setSeed(world.seed + 3).build()
        )
        .octavate(8, 0.5, 2.0, FractalFunction.FBM, false)
        .scale(0.05)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    private val treeNoise = JNoise.newBuilder()
        .superSimplex(
            SuperSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 4).build()
        )
        .scale(100.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    private val biomes = mutableMapOf<Vector2i, Biome>()
    private val blocks = mutableMapOf<Vector3i, Block>()

    val meshTerrain = ChunkMesh(this)
    val meshFluid = ChunkMesh(this)
    val meshFoliage = ChunkMesh(this)

    init {
        //Generate blocks
        run {
            for (x in 0 until SIZE.x)
                for (z in 0 until SIZE.y) {
                    val worldPosition = Vector2i(
                        x + position.x * SIZE.x,
                        z + position.z * SIZE.z
                    )

                    val elevation = elevationNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()
                    val moisture = moistureNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()
                    val temperature = temperatureNoise.evaluateNoise(
                        worldPosition.x.toDouble(),
                        worldPosition.y.toDouble()
                    ).toFloat()

                    val biome = Biome.evaluate(Vector3f(elevation, moisture, temperature))
                    biomes[Vector2i(x, z)] = biome

                    for (y in 0 until SIZE.y) {
                        val worldBlockPosition = Vector3i(x, y + position.y * SIZE.y, z)
                        val block = biome.generate(worldBlockPosition)

                        if (block != null)
                            blocks[Vector3i(x, y, z)] = block.create()
                    }
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
//                        (chunkRelativeBlockPosition.x + position.x * size.x).toDouble(),
//                        (chunkRelativeBlockPosition.y).toDouble(),
//                        (chunkRelativeBlockPosition.z + position.y * size.z).toDouble()
//                    )
//
//                    if ((0.425f..0.575f).contains(value))
//                        iterator.remove()
//                }
//            }

            //Place Trees
            run {
                for (x in 0 until SIZE.x)
                    for (z in 0 until SIZE.z) {
                        val height = getHeight(Vector2i(x, z), 500)

                        val block = getBlock(Vector3i(x, height, z))?.template
                        val biome = getBiome(Vector2i(x, z))?.template

                        if (biome == Biome.NORMAL && block == Block.GRASS) {
                            val noise = treeNoise.evaluateNoise(
                                (x + position.x * SIZE.x).toDouble(),
                                (z + position.y * SIZE.z).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise)) {
                                for (i in 0 until 5) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.LOG.create()

                                    if (i >= 2) {
                                        blocks[Vector3i(x - 1, y, z)] = Block.LEAVES.create()
                                        blocks[Vector3i(x + 1, y, z)] = Block.LEAVES.create()
                                        blocks[Vector3i(x, y, z - 1)] = Block.LEAVES.create()
                                        blocks[Vector3i(x, y, z + 1)] = Block.LEAVES.create()
                                    }
                                }

                                blocks[Vector3i(x, height + 6, z)] = Block.LEAVES.create()
                            }
                        } else if (biome == Biome.DESERT && block == Block.SAND) {
                            val noise = treeNoise.evaluateNoise(
                                (x + position.x * SIZE.x).toDouble(),
                                (z + position.y * SIZE.z).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise))
                                for (i in 0 until (Math.random() * 2).roundToInt() + 2) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.CACTUS.create()
                                }

                        } else if (biome == Biome.SNOW && block == Block.SNOW) {
                            val noise = treeNoise.evaluateNoise(
                                (x + position.x * SIZE.x).toDouble(),
                                (z + position.y * SIZE.z).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise)) {
                                for (i in 0 until 8) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.LOG.create()

                                    if (i >= 3) {
                                        blocks[Vector3i(x - 1, y, z)] = Block.LEAVES_SNOW.create()
                                        blocks[Vector3i(x + 1, y, z)] = Block.LEAVES_SNOW.create()
                                        blocks[Vector3i(x, y, z - 1)] = Block.LEAVES_SNOW.create()
                                        blocks[Vector3i(x, y, z + 1)] = Block.LEAVES_SNOW.create()

                                        if (i == 4 || i == 5) {
                                            blocks[Vector3i(x - 2, y, z)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x + 2, y, z)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x, y, z - 2)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x, y, z + 2)] = Block.LEAVES_SNOW.create()

                                            blocks[Vector3i(x - 1, y, z - 1)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x + 1, y, z - 1)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x - 1, y, z + 1)] = Block.LEAVES_SNOW.create()
                                            blocks[Vector3i(x + 1, y, z + 1)] = Block.LEAVES_SNOW.create()
                                        }
                                    }
                                }

                                blocks[Vector3i(x, height + 9, z)] = Block.LEAVES_SNOW.create()
                            }
                        } else if (biome == Biome.MUSHROOM && block == Block.MYCELIUM) {
                            val noise = treeNoise.evaluateNoise(
                                (x + position.x * SIZE.x).toDouble(),
                                (z + position.y * SIZE.z).toDouble(),
                            )

                            if ((0.9f..1.0f).contains(noise)) {
                                val stemHeight = (Math.random() * 4).roundToInt() + 7
                                for (i in 0 until stemHeight) {
                                    val y = height + i + 1
                                    blocks[Vector3i(x, y, z)] = Block.MUSHROOM_STEM.create()

                                    if (i == stemHeight - 1) {
                                        blocks[Vector3i(x - 1, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x - 1, y + 1, z + 1)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x - 1, y + 1, z + 2)] = Block.MUSHROOM_BLOCK_RED.create()

                                        blocks[Vector3i(x - 2, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()

                                        blocks[Vector3i(x - 3, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x - 3, y + 1, z + 1)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x - 3, y + 1, z + 2)] = Block.MUSHROOM_BLOCK_RED.create()

                                        blocks[Vector3i(x + 1, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x + 2, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x + 3, y + 1, z)] = Block.MUSHROOM_BLOCK_RED.create()

                                        blocks[Vector3i(x, y + 1, z - 1)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x, y + 1, z - 2)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x, y + 1, z - 3)] = Block.MUSHROOM_BLOCK_RED.create()

                                        blocks[Vector3i(x, y + 1, z + 1)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x, y + 1, z + 2)] = Block.MUSHROOM_BLOCK_RED.create()
                                        blocks[Vector3i(x, y + 1, z + 3)] = Block.MUSHROOM_BLOCK_RED.create()
                                    }
                                }
                            }
                        }
                    }
            }

            updateMesh()
        }
    }

    fun getHeight(position: Vector2i, start: Int): Int {
        for (y in start downTo 0)
            if (getBlock(Vector3i(position.x, y, position.y)) != null)
                return y

        return 0
    }

    fun getBlock(position: Vector3i): Block? {
        return blocks[position]
    }

    fun setBlock(position: Vector3i, block: BlockTemplate?): Boolean {
        if (!(0 until SIZE.x).contains(position.x)) return false
        if (!(0 until SIZE.y).contains(position.y)) return false
        if (!(0 until SIZE.z).contains(position.z)) return false

        if (block == null)
            blocks.remove(position)
        else
            blocks[position] = block.create()

        updateMesh()

        if (position.x <= 0)
            world.chunkManager.get(Vector3i(this.position).add(-1, 0, 0))?.updateMesh()
        if (position.x >= SIZE.x - 1)
            world.chunkManager.get(Vector3i(this.position).add(1, 0, 0))?.updateMesh()

        if (position.y <= 0)
            world.chunkManager.get(Vector3i(this.position).add(0, -1, 0))?.updateMesh()
        if (position.y >= SIZE.y - 1)
            world.chunkManager.get(Vector3i(this.position).add(0, 1, 0))?.updateMesh()

        if (position.z <= 0)
            world.chunkManager.get(Vector3i(this.position).add(0, 0, -1))?.updateMesh()
        if (position.z >= SIZE.z - 1)
            world.chunkManager.get(Vector3i(this.position).add(0, 0, 1))?.updateMesh()

        return true
    }

    fun getBiome(chunkRelativeBlockPosition: Vector2i): Biome? {
        return biomes[chunkRelativeBlockPosition]
    }

    fun update() {

    }

    fun updateMesh() {
        val blocksTerrain = mutableMapOf<Vector3i, Block>()
        val blocksFoliage = mutableMapOf<Vector3i, Block>()
        val blocksFluid = mutableMapOf<Vector3i, Block>()

        blocks.forEach { (position, block) ->
            when (block.template.type) {
                Block.Type.SOLID -> blocksTerrain
                Block.Type.FOLIAGE -> blocksFoliage
                Block.Type.FLUID -> blocksFluid
            }[position] = block
        }

        meshTerrain.update(blocksTerrain)
        meshFoliage.update(blocksFoliage)
        meshFluid.update(blocksFluid)
    }

}