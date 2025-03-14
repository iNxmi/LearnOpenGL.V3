package com.nami.client.world.resources.particle.handlers

import com.nami.client.random
import com.nami.client.world.World
import com.nami.client.world.resources.particle.Particle
import com.nami.client.world.resources.particle.ParticleListener
import org.joml.Vector3f

class ParticleHandlerExplosion : ParticleListener {

    private val direction = Vector3f(
        Float.random(-1f..1f),
        Float.random(-1f..1f),
        Float.random(-1f..1f)
    ).normalize()

    private val speed = Float.random(1.5f..3.0f)

    override fun update(particle: Particle.Instance, world: World) {
        particle.transform.position.add(
            Vector3f(direction).mul(speed * world.time.delta)
        )
    }

}