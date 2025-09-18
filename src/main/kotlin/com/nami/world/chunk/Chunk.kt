package com.nami.world.chunk

import com.nami.storage.Storage
import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.material.Material
import com.nami.world.material.Layer
import com.nami.world.entity.Player
import de.articdive.jnoise.pipeline.JNoise
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joml.Vector2i
import org.joml.Vector3f
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

    val meshes = mutableMapOf<Layer, Mesh>()

    init {
        Layer.entries.forEach { meshes[it] = Mesh(this, it) }

        val biomes = mutableMapOf<Vector3i, Vector3f>()
        val blocks = mutableMapOf<Vector3i, Material>()

        val json = Storage.read<JSON>(root, fileName)

        if (json != null) {
            load(json, biomes, blocks)
        } else {
            generate(biomes, blocks)
        }

        biomeManager.setBiomeFactors(biomes)
        blockManager.setBlocks(blocks)
        world.chunkManager.saver.addToQueue(position)
    }

    private fun load(
        json: JSON,
        biomes: MutableMap<Vector3i, Vector3f>,
        blocks: MutableMap<Vector3i, Material>
    ) {
        json.biomes.forEach { (index, factors) ->
            val indexToChunkRelativePosition = Vector3i(
                index % SIZE.y,
                (index / SIZE.y) % SIZE.x,
                index / (SIZE.y * SIZE.x)
            )

            val globalBlockPosition = Vector3i(position).mul(SIZE).add(indexToChunkRelativePosition)

            biomes[Vector3i(globalBlockPosition)] = factors
        }

        json.blocks.forEach { (index, blockJson) ->
            val indexToChunkRelativePosition = Vector3i(
                index % SIZE.y,
                (index / SIZE.y) % SIZE.x,
                index / (SIZE.y * SIZE.x)
            )

            val globalBlockPosition = Vector3i(position).mul(SIZE).add(indexToChunkRelativePosition)

            blocks[Vector3i(globalBlockPosition)] = block
        }
    }

    fun save() {
        val biomes = mutableMapOf<Int, Vector3f>()
        val blocks = mutableMapOf<Int, Material>()
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val blockPosition = Vector3i(position).mul(SIZE).add(x, y, z)
                    val index = x + y * SIZE.x + z * SIZE.y * SIZE.y

                    val biome = world.biomeManager.getBiomeFactors(blockPosition)
                    biomes[index] = biome

                    val block = world.blockManager.getBlock(blockPosition)
                    if (block != null)
                        blocks[index] = block
                }

        val json = JSON(biomes, blocks)
        Storage.write(json, root, fileName)
    }

    private fun generate(biomes: MutableMap<Vector3i, Vector3f>, blocks: MutableMap<Vector3i, Material>) {
        //Generate blocks
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val blockPosition = Vector3i(position).mul(SIZE).add(x, y, z)

                    biomeManager.generate(blockPosition)

                    val factors = biomeManager.getBiomeFactors(blockPosition)
                    val biome = Biome.evaluate(factors.x, factors.y, factors.z)
                    val block = biome.generateBlock(blockPosition, factors.x, factors.y, factors.z) ?: continue

                    blocks[blockPosition] = block
                }

        //Place Features
        for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z))
            for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x)) {
                val y = blockManager.getHeight(Vector2i(x, z), 512, setOf(Layer.SOLID))
                val biome = biomeManager.getBiomeFactors(Vector3i(x, y, z))
                val block = world.blockManager.getBlock(Vector3i(x, y, z)) ?: continue

                //TODO implement features

//                for (feature in biome.template.features) {
//                    val fy = blockManager.getHeight(Vector2i(x, z), 512, setOf(Block.Layer.SOLID))
//                    if (y != fy)
//                        continue
//
//                    val fblock = world.blockManager.getBlock(Vector3i(x, fy, z)) ?: continue
//
//                    if (feature.base != null)
//                        if (!feature.base.contains(fblock.template))
//                            continue
//
//                    if (!canSpawnFeature(Vector2i(x, z), feature.radius, feature.noise(world.seed)))
//                        continue
//
//                    val structureBlocks = feature.feature.create().handler.generate(world, Vector3i(x, y, z))
//                    blocks.putAll(structureBlocks)
//
//                    break
//                }
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

    fun render(player: Player, layer: Layer) = meshes[layer]?.render(player, world.time)

    fun generateMesh() = meshes.forEach { it.value.generate() }

    fun uploadMesh() = meshes.forEach { it.value.upload() }

    @Serializable
    data class JSON(
        val biomes: Map<Int, @Contextual Vector3f>,
        val blocks: Map<Int, String>
    )

}