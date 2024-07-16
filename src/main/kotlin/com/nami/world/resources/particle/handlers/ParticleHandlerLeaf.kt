package com.nami.world.resources.particle.handlers

import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.resources.block.Block
import com.nami.world.resources.particle.Particle
import com.nami.world.resources.particle.ParticleListener
import org.joml.Vector2i
import org.joml.Vector3f

class ParticleHandlerLeaf : ParticleListener {

    val windDirection = Vector3f(-1f, 0f, 1f).normalize()
    val windSpeed = 0.3f

    val particleSpeed = Vector3f(0f, -1f, 0f)

    override fun update(particle: Particle.Instance, world: World, time: SceneTime) {
        val position = particle.transform.position

        val height = world.blockManager.getHeight(
            Vector2i(position.x.toInt(), position.z.toInt()),
            512,
            setOf(Block.Layer.SOLID)
        )

        if (position.y == height.toFloat() + 1)
            return

        val move = Vector3f(particleSpeed).add(Vector3f(windDirection).mul(windSpeed)).mul(time.delta)
        position.add(move)

        if (position.y < height.toFloat() + 1)
            position.y = height.toFloat() + 1
    }

}