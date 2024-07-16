package com.nami.world.particle.handlers

import com.nami.random
import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.particle.Particle
import com.nami.world.particle.ParticleListener
import org.joml.Vector3f

class ParticleHandlerExplosion : ParticleListener {

    private val direction = Vector3f(
        Float.random(-1f..1f),
        Float.random(-1f..1f),
        Float.random(-1f..1f)
    ).normalize()

    private val speed = Float.random(1.5f..3.0f)

    override fun update(particle: Particle.Instance, world: World, time: SceneTime) {
        particle.transform.position.add(
            Vector3f(direction).mul(speed * time.delta)
        )
    }

}