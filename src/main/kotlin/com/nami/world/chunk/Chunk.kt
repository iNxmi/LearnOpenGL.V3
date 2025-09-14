package com.nami.world.chunk

import com.nami.storage.Storage
import com.nami.world.World
import com.nami.world.entity.player.Player
import com.nami.world.resources.biome.Biome
import com.nami.world.resources.block.Block
import de.articdive.jnoise.pipeline.JNoise
import kotlinx.serialization.Serializable
import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.max

class Chunk(val world: World, val position: Vector3i) {

    private val root = world.root.resolve("chunks")
    private val fileName = "${position.x}_${position.y}_${position.z}"

    val biomeManager = world.biomeManager
    val blockManager = world.blockManager
    val chunkManager = world.chunkManager

    companion object {
        val SIZE = Vector3i(16, 16, 16)
    }

    val meshes = mutableMapOf<Block.Layer, ChunkMesh>()

    init {
        Block.Layer.entries.forEach { meshes[it] = ChunkMesh(this, it) }

        val biomes = mutableMapOf<Vector3i, Biome.Instance>()
        val blocks = mutableMapOf<Vector3i, Block.Instance>()

        val json = Storage.read<JSON>(root, fileName)

        if (json != null) {
            load(json, biomes, blocks)
        } else {
            generate(biomes, blocks)
        }

        biomeManager.setBiomes(biomes)
        blockManager.setBlocks(blocks)
        world.chunkManager.saver.addToQueue(position)
    }

    private fun load(
        json: JSON,
        biomes: MutableMap<Vector3i, Biome.Instance>,
        blocks: MutableMap<Vector3i, Block.Instance>
    ) {
        json.biomes.forEach { (index, biomeJson) ->
            val indexToChunkRelativePosition = Vector3i(
                index % SIZE.y,
                (index / SIZE.y) % SIZE.x,
                index / (SIZE.y * SIZE.x)
            )

            val globalBlockPosition = Vector3i(position).mul(SIZE).add(indexToChunkRelativePosition)

            biomes[Vector3i(globalBlockPosition)] = biomeJson.create(world, Vector3i(globalBlockPosition))
        }

        json.blocks.forEach { (index, blockJson) ->
            val indexToChunkRelativePosition = Vector3i(
                index % SIZE.y,
                (index / SIZE.y) % SIZE.x,
                index / (SIZE.y * SIZE.x)
            )

            val globalBlockPosition = Vector3i(position).mul(SIZE).add(indexToChunkRelativePosition)

            blocks[Vector3i(globalBlockPosition)] = blockJson.create(world, Vector3i(globalBlockPosition))
        }
    }

    fun save() {
        val biomesJson = mutableMapOf<Int, Biome.Instance.JSON>()
        val blocksJson = mutableMapOf<Int, Block.Instance.JSON>()
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val blockPosition = Vector3i(position).mul(SIZE).add(x, y, z)
                    val index = x + y * SIZE.x + z * SIZE.y * SIZE.y

                    val biome = world.biomeManager.getBiome(blockPosition)
                    if (biome != null)
                        biomesJson[index] = biome.json()

                    val block = world.blockManager.getBlock(blockPosition)
                    if (block != null)
                        blocksJson[index] = block.json()
                }

        val json = JSON(biomesJson, blocksJson)
        Storage.write(json, root, fileName)
    }

    private fun generate(biomes: MutableMap<Vector3i, Biome.Instance>, blocks: MutableMap<Vector3i, Block.Instance>) {
        //Generate blocks
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val blockPosition = Vector3i(position).mul(SIZE).add(x, y, z)
                    if (biomeManager.getBiome(blockPosition) == null)
                        biomeManager.generate(blockPosition)

                    val biome = biomeManager.getBiome(blockPosition) ?: continue
                    val block = biome.generate() ?: continue

                    blocks[blockPosition] = block.create(world, blockPosition)
                }

        //Place Features
        for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z))
            for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x)) {
                val y = blockManager.getHeight(Vector2i(x, z), 512, setOf(Block.Layer.SOLID))
                val biome = biomeManager.getBiome(Vector3i(x, y, z)) ?: continue
                val block = world.blockManager.getBlock(Vector3i(x, y, z)) ?: continue

                for (feature in biome.template.features) {
                    val fy = blockManager.getHeight(Vector2i(x, z), 512, setOf(Block.Layer.SOLID))
                    if (y != fy)
                        continue

                    val fblock = world.blockManager.getBlock(Vector3i(x, fy, z)) ?: continue

                    if (feature.base != null)
                        if (!feature.base.contains(fblock.template))
                            continue

                    if (!canSpawnFeature(Vector2i(x, z), feature.radius, feature.noise(world.seed)))
                        continue

                    val structureBlocks = feature.feature.create().handler.generate(world, Vector3i(x, y, z))
                    blocks.putAll(structureBlocks)

                    break
                }
            }

        chunkManager.meshGenerator.addToQueue(position)
    }

    private fun canSpawnFeature(position: Vector2i, radius: Int, noise: JNoise): Boolean {
        var max = 0f
        for (z in position.y - radius until position.y + radius)
            for (x in position.x - radius until position.x + radius) {
                val noise = noise.evaluateNoise(
                    x.toDouble(),
                    z.toDouble(),
                ).toFloat()

                max = max(max, noise)
            }

        return noise.evaluateNoise(
            position.x.toDouble(),
            position.y.toDouble(),
        ).toFloat() == max
    }

    fun update() {}

    fun render(player: Player, layer: Block.Layer) = meshes[layer]?.render(player, world.time)

    fun generateMesh() = meshes.forEach { it.value.generate() }

    fun uploadMesh() = meshes.forEach { it.value.upload() }

    @Serializable
    data class JSON(
        val biomes: Map<Int, Biome.Instance.JSON>,
        val blocks: Map<Int, Block.Instance.JSON>
    )

}