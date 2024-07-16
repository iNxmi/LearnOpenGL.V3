package com.nami.world.resources.particle

import com.nami.entity.Transform
import com.nami.resources.Resources
import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.player.Player
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL31.glDrawElementsInstanced
import org.lwjgl.opengl.GL33
import kotlin.math.sin

class ParticleManager(val world: World) {

    val particles = mutableListOf<Particle.Instance>()

    fun spawn(id: String, sceneTime: SceneTime, position: Vector3f) {
        val particle = Resources.PARTICLE.get(id)
        val instance = particle.create(sceneTime, position)
        particles.add(instance)
    }

    fun update(time: SceneTime) {
        particles.removeAll {
            it.startTime + it.durationInSeconds < time.time
        }
        particles.forEach {
            it.update(world, time)
        }
    }

    fun render(player: Player, time: SceneTime) {
        particles.withIndex().groupBy { it.index / 128 }.values.forEach { list ->
            val shader = Resources.SHADER.get("particle").bind()

            shader.uniform.set("u_projection_matrix", player.camera.projection())
            shader.uniform.set("u_view_matrix", player.camera.view())

            list.withIndex().forEach { (i, it) ->
                val x = (time.time - it.value.startTime) / it.value.durationInSeconds
                val transform = Transform(it.value.transform)
                transform.scale.mul(sin(x * Math.PI).toFloat())

                shader.uniform.set("u_model_matrices[${i}]", transform.modelMatrix())
                shader.uniform.set("u_colors[${i}]", it.value.color)
            }

            val model = Resources.MODEL.get("particle.cube")

            GL33.glBindVertexArray(model.vao)
            glDrawElementsInstanced(
                GL33.GL_TRIANGLES,
                model.numberOfIndices,
                GL_UNSIGNED_INT,
                0,
                list.size
            )
            GL33.glBindVertexArray(0)

            Resources.SHADER.unbind()
        }
    }

}