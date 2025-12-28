package com.nami.world.chunk

import com.nami.resources.Resources
import com.nami.world.Player
import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.block.Layer
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i

class Chunk(
    val world: World,
    val position: Vector3i
) {

    companion object {
        val SIZE = Vector3i(16, 16, 16)
    }

    private val log = KotlinLogging.logger {}

    val voxels = mutableMapOf<Vector3i, Voxel>()
    val solid = ChunkMesh(this, Layer.SOLID)

    init {
        for (z in 0 until SIZE.z)
            for (x in 0 until SIZE.x) {

                val elevation = world.elevation.evaluateNoise(
                    position.x * SIZE.x + x.toDouble(),
                    position.z * SIZE.z + z.toDouble()
                ).toFloat()

                for (y in 0 until SIZE.y) {
                    val position = Vector3i(this@Chunk.position).mul(SIZE).add(x, y, z)

                    val moisture = world.moisture.evaluateNoise(
                        position.x.toDouble(),
                        position.y.toDouble(),
                        position.z.toDouble()
                    ).toFloat()

                    val temperature = world.temperature.evaluateNoise(
                        position.x.toDouble(),
                        position.y.toDouble(),
                        position.z.toDouble()
                    ).toFloat()

                    val biome = Biome.create(position, elevation, moisture, temperature)
                    val block = biome.template.generate(position, elevation, moisture, temperature)

                    voxels[position] = Voxel(position, biome, block)
                }
            }

        solid.generate()
    }

    fun update() {}

    fun render(player: Player) {
        val shader = Resources.SHADER.get("chunk.solid").bind()
        shader.uniform.set("u_light_direction", Vector3f(1f, 1f, 0f).normalize())
        shader.uniform.set("u_specular_exponent", 8.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection())
        shader.uniform.set("u_view_matrix", player.camera.view())

        shader.uniform.set("u_camera_position", player.transform.position)

//            glActiveTexture(GL_TEXTURE0)
//            glBindTexture(GL_TEXTURE_2D, TextureAtlas.texture!!.pointer)
//            shader.uniform.set("u_texture_diffuse", 0)

        val model = Matrix4f().translate(
            (position.x).toFloat(),
            (position.y).toFloat(),
            (position.z).toFloat()
        )

        shader.uniform.set("u_model_matrix", model)

        solid.render()
//        fluid.render()
//        foliage.render()
//        transparent.render()

        Resources.SHADER.unbind()

    }

}