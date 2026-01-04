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
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D
import org.lwjgl.opengl.GL33.*
import java.text.NumberFormat

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

            if(block != null)
                layers[block.layer]!!.add(index)

            Voxel(localBlockPosition, biome, block)
        }

        meshes = mapOf(
            Layer.SOLID to ChunkMesh(this, Layer.SOLID),
            Layer.TRANSPARENT to ChunkMesh(this, Layer.TRANSPARENT),
            Layer.FLUID to ChunkMesh(this, Layer.FLUID),
            Layer.FOLIAGE to ChunkMesh(this, Layer.FOLIAGE)
        )
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