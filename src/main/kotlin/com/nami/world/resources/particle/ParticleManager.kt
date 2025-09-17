package com.nami.world.resources.particle

import com.nami.Transform
import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.entity.Player
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL31.glDrawElementsInstanced
import org.lwjgl.opengl.GL33

class ParticleManager(val world: World) {

    val particles = mutableListOf<Particle.Instance>()

    fun spawn(id: String, position: Vector3f) {
        val particle = Resources.PARTICLE.get(id)
        val instance = particle.create(world.time, position)
        particles.add(instance)
    }

    fun update() {
        particles.removeAll { it.startTime + it.durationInSeconds < world.time.time }
        particles.forEach { it.update(world) }
    }

    fun render(player: Player) {
        particles.withIndex().groupBy { it.index / 128 }.values.forEach { list ->
            val shader = Resources.SHADER.get("particle").bind()

            shader.uniform.set("u_projection_matrix", player.camera.projection())
            shader.uniform.set("u_view_matrix", player.camera.view())

            list.withIndex().forEach { (i, it) ->
                val particle = it.value

                val t = (world.time.time - particle.startTime) / particle.durationInSeconds
                val x = particle.template.easing.toEasingFunction().evaluate(t)

                val transform = Transform(particle.transform)
                transform.scale.mul(x)

                shader.uniform.set("u_model_matrices[${i}]", transform.matrix())
                shader.uniform.set("u_colors[${i}]", particle.color)
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