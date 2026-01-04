package com.nami.world.chunk

import com.nami.Time
import com.nami.extension.plus
import com.nami.extension.times
import com.nami.resources.Resources
import com.nami.resources.texture.TextureAtlas
import com.nami.world.Player
import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.block.Layer
import com.nami.world.block.blocks.BlockError
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Matrix4f
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

        fun indexToPosition(index: Int): Vector3i = Vector3i(
            index % SIZE.x,
            (index / SIZE.x) % SIZE.y,
            index / (SIZE.x * SIZE.y),
        )

        fun positionToIndex(position: Vector3i): Int = position.x + position.y * SIZE.x + position.z * SIZE.x * SIZE.y
    }

    private val log = KotlinLogging.logger {}

    val voxels: Array<Voxel>
    val layers: Map<Layer, MutableSet<Int>> = mapOf(
        Layer.SOLID to mutableSetOf(),
        Layer.TRANSPARENT to mutableSetOf(),
        Layer.FLUID to mutableSetOf(),
        Layer.FOLIAGE to mutableSetOf()
    )
    val meshes: Map<Layer, ChunkMesh>

    init {
        voxels = Array(SIZE.x * SIZE.y * SIZE.z) { index ->
            val localBlockPosition = indexToPosition(index)
            val globalBlockPosition = (this.position * SIZE) + localBlockPosition

            val elevation = world.elevation.evaluateNoise(
                globalBlockPosition.x.toDouble(),
                globalBlockPosition.z.toDouble()
            ).toFloat()

            val moisture = world.moisture.evaluateNoise(
                globalBlockPosition.x.toDouble(),
                globalBlockPosition.y.toDouble(),
                globalBlockPosition.z.toDouble()
            ).toFloat()

            val temperature = world.temperature.evaluateNoise(
                globalBlockPosition.x.toDouble(),
                globalBlockPosition.y.toDouble(),
                globalBlockPosition.z.toDouble()
            ).toFloat()

            val biome = Biome.create(globalBlockPosition, elevation, moisture, temperature)
            val block = biome.template.generate(globalBlockPosition, elevation, moisture, temperature)

            if (block != null)
                layers[block.layer]!!.add(index)

            Voxel(localBlockPosition, biome, block)
        }

        for ((index, voxel) in voxels.withIndex()) {
            val localBlockPosition = indexToPosition(index)
            val globalBlockPosition = (this.position * SIZE) + localBlockPosition
            val biome = voxel.biome

            if (voxel.block != null)
                continue

            for ((noise, feature) in biome.template.features) {
                if (!canSpawnFeature(globalBlockPosition, 4, noise))
                    continue

                val under = positionToIndex(localBlockPosition + Vector3i(0, -1, 0))
                if (under !in 0 until SIZE.x * SIZE.y * SIZE.z)
                    continue
                if (voxels[under].block == null)
                    continue

                val featureBlocks = feature.generate(biome.elevation, biome.moisture, biome.temperature)
//                val featureBlocks = mapOf(Vector3i() to BlockError)
                val byIndex = featureBlocks.mapKeys { positionToIndex(it.key + localBlockPosition) }

                for ((index, newBlock) in byIndex) {
                    if (index !in 0 until (SIZE.x * SIZE.y * SIZE.z))
                        continue

                    val currentVoxel = voxels[index]
                    val currentBlock = currentVoxel.block
                    if (currentBlock != null)
                        layers[currentBlock.layer]!!.remove(index)

                    voxels[index].block = newBlock

                    layers[newBlock.layer]!!.add(index)
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

    private fun canSpawnFeature(globalPosition: Vector3i, radius: Int, noise: JNoise): Boolean {
        var max = 0f
        for (z in globalPosition.z - radius..globalPosition.z + radius)
            for (y in globalPosition.y - radius..globalPosition.y + radius)
                for (x in globalPosition.x - radius..globalPosition.x + radius) {
                    val value = noise.evaluateNoise(
                        x.toDouble(),
                        y.toDouble(),
                        z.toDouble(),
                    ).toFloat()

                    max = max(max, value)
                }

        return noise.evaluateNoise(
            globalPosition.x.toDouble(),
            globalPosition.y.toDouble(),
            globalPosition.z.toDouble(),
        ).toFloat() == max
    }

    fun getVoxel(index: Int): Voxel = voxels[index]
    fun getVoxel(position: Vector3i): Voxel = getVoxel(positionToIndex(position))

    fun generateMesh() = meshes.forEach { (_, mesh) -> mesh.generate() }

    fun update() {}

    fun render(time: Time, player: Player, layer: Layer) {
        val shader = Resources.SHADER.get("chunk").bind()
        shader.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shader.uniform.set("u_specular_exponent", 8.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection())
        shader.uniform.set("u_view_matrix", player.camera.view())

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