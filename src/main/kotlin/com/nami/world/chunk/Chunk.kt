package com.nami.world.chunk

import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.player.Player
import com.nami.world.resources.block.Block
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.max


class Chunk(val world: World, val position: Vector3i) {

    private val log = KotlinLogging.logger { }

    val path = world.path.resolve("chunks")

    private val biomeManager = world.biomeManager
    private val blockManager = world.blockManager
    private val chunkManager = world.chunkManager

    companion object {
        @JvmStatic
        val SIZE = Vector3i(16, 16, 16)
    }

    val meshes = mutableMapOf<Block.Layer, ChunkMesh>()

    init {
        Block.Layer.values().forEach { meshes[it] = ChunkMesh(this, it) }

        //Generate blocks
        for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z)) {
            for (y in (position.y * SIZE.y) until ((position.y + 1) * SIZE.y))
                for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x)) {
                    val position = Vector3i(x, y, z)
                    val biome = biomeManager.getBiome(position)
                    val block = biome.generate() ?: continue

                    blockManager.setBlock(position, block)
                }
        }

        //Place Features
        for (z in (position.z * SIZE.z) until ((position.z + 1) * SIZE.z))
            for (x in (position.x * SIZE.x) until ((position.x + 1) * SIZE.x)) {
                val y = blockManager.getHeight(Vector2i(x, z), 512, setOf(Block.Layer.SOLID))
                val biome = biomeManager.getBiome(Vector3i(x, y, z))
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

                    feature.feature.create().handler.generate(world, Vector3i(x, y, z))
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

    fun update() {

    }

    fun render(player: Player, time: SceneTime, layer: Block.Layer) {
        meshes[layer]?.render(player, time)
    }

    fun generateMesh() {
        meshes.forEach { it.value.generate() }
    }

    fun uploadMesh() {
        meshes.forEach { it.value.upload() }
    }

}