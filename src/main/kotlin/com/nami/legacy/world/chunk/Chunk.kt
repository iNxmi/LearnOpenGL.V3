package com.nami.world.chunk

import com.nami.Time
import com.nami.extension.plus
import com.nami.extension.times
import com.nami.resources.Resources
import com.nami.resources.texture.TextureAtlas
import com.nami.world.Player
import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.biome.biomes.BiomeBirchForest
import com.nami.world.biome.biomes.BiomeMushroomForest
import com.nami.world.biome.biomes.BiomeOakForest
import com.nami.world.biome.biomes.BiomeSpruceForest
import com.nami.world.block.Block
import com.nami.world.block.Layer
import com.nami.world.block.blocks.BlockError
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D
import org.lwjgl.opengl.GL33.*
import kotlin.math.max

class Chunk(
    val world: World,
    val position: Vector3i
) {

    companion object {
        val SIZE = Vector3i(16, 16, 16)
        val LENGTH = SIZE.x * SIZE.y * SIZE.z

        fun indexToPosition(index: Int): Vector3i = Vector3i(
            index % SIZE.x,
            (index / SIZE.x) % SIZE.y,
            index / (SIZE.x * SIZE.y),
        )

        fun positionToIndex(position: Vector3i): Int = position.x + position.y * SIZE.x + position.z * SIZE.x * SIZE.y
    }

    private val log = KotlinLogging.logger {}

    val elevationMap = mutableMapOf<Vector3i, Float>()
    val densityMap = mutableMapOf<Vector3i, Float>()
    val moistureMap = mutableMapOf<Vector3i, Float>()
    val temperatureMap = mutableMapOf<Vector3i, Float>()
    val biomes = mutableMapOf<Vector3i, Biome>()
    val blocks = mutableMapOf<Vector3i, Block>()
    val meshes: Map<Layer, ChunkMesh>

    init {
        for (z in 0 until SIZE.z)
            for (y in 0 until SIZE.y)
                for (x in 0 until SIZE.x) {
                    val localPosition = Vector3i(x, y, z)
                    val globalPosition = (this.position * SIZE) + localPosition

                    val elevation = world.biomeGenerator.getElevation(this, Vector2i(localPosition.x, localPosition.z))
                    elevationMap[localPosition] = elevation

                    val density = world.biomeGenerator.getDensity(this, localPosition)
                    val densityAbove = world.biomeGenerator.getDensity(this, localPosition + Vector3i(0, 1, 0))
                    densityMap[localPosition] = density

                    val moisture = world.biomeGenerator.getMoisture(this, localPosition)
                    moistureMap[localPosition] = moisture

                    val temperature = world.biomeGenerator.getTemperature(this, localPosition)
                    temperatureMap[localPosition] = temperature

                    val biome = Biome.evaluate(elevation, moisture, temperature)
                    biomes[localPosition] = biome

                    val block = biome.generate(globalPosition, density, densityAbove, moisture, temperature)
                    if (block != null)
                        blocks[localPosition] = block
                }

        for (z in 0 until SIZE.z)
            for (x in 0 until SIZE.x) {
                val elevation = world.biomeGenerator.getElevation(this, Vector2i(x, z))
                if ((this.position.y * SIZE.y) < elevation || ((this.position.y + 1) * SIZE.y) >= elevation)
                    continue

                val y = elevation.toInt()
                val localBlockPosition = Vector3i(x, y, z)
                val globalBlockPosition = (this.position * SIZE) + localBlockPosition

                val biome = biomes[globalBlockPosition]
                val block = blocks[globalBlockPosition]

                if (block != null)
                    continue

                for ((noise, feature) in biome!!.features) {
                    if (!canSpawnFeature(Vector2i(x, z), 4, noise))
                        continue

                    val localPositionUnder = localBlockPosition + Vector3i(0, -1, 0)
                    val globalPositionUnder = (this.position * SIZE) + localPositionUnder
                    if (world.getGlobalBlock(globalPositionUnder) == null)
                        continue

                    val featureBlocks = feature.generate(
                        elevationMap[localBlockPosition]!!,
                        moistureMap[localBlockPosition]!!,
                        temperatureMap[localBlockPosition]!!
                    )
                    for ((featureLocalBlockPosition, block) in featureBlocks) {

                        val localFeatureBlockPosition = localBlockPosition + featureLocalBlockPosition

                        if (localFeatureBlockPosition.x !in 0 until SIZE.x)
                            continue
                        if (localFeatureBlockPosition.y !in 0 until SIZE.y)
                            continue
                        if (localFeatureBlockPosition.z !in 0 until SIZE.z)
                            continue

                        val currentBlock = blocks[localFeatureBlockPosition]
                        if (currentBlock != null)
                            blocks[localFeatureBlockPosition] = block
                    }

                    break
                }
            }

        meshes = mapOf(
            Layer.SOLID to ChunkMesh(this, Layer.SOLID),
            Layer.TRANSPARENT to ChunkMesh(this, Layer.TRANSPARENT),
            Layer.FLUID to ChunkMesh(this, Layer.FLUID),
            Layer.FOLIAGE to ChunkMesh(this, Layer.FOLIAGE)
        )
    }

    private fun canSpawnFeature(globalPosition: Vector2i, radius: Int, noise: JNoise): Boolean {
        var max = 0f
        for (z in globalPosition.y - radius..globalPosition.y + radius)
            for (x in globalPosition.x - radius..globalPosition.x + radius) {
                val value = noise.evaluateNoise(
                    x.toDouble(),
                    z.toDouble(),
                ).toFloat()

                max = max(max, value)
            }

        return noise.evaluateNoise(
            globalPosition.x.toDouble(),
            globalPosition.y.toDouble(),
        ).toFloat() == max
    }

    fun generateMesh() = meshes.forEach { (_, mesh) -> mesh.generate() }

    fun render(time: Time, player: Player, layer: Layer) {
        val shader = Resources.SHADER.get("chunk").bind()
        shader.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shader.uniform.set("u_specular_exponent", 8.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection)
        shader.uniform.set("u_view_matrix", player.camera.view)

        shader.uniform.set("u_camera_position", player.transform.position)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
        shader.uniform.set("u_texture_diffuse", 0)

        val model = Matrix4f().translate(
            (position.x * SIZE.x).toFloat(),
            (position.y * SIZE.y).toFloat(),
            (position.z * SIZE.z).toFloat()
        )

        shader.uniform.set("u_model_matrix", model)

        val mesh = meshes[layer]!!
        mesh.sortFaces(player)
        mesh.render()

        Resources.SHADER.unbind()
    }

}