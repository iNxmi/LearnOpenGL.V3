package com.nami.world.resources.particle.handlers

import com.nami.next
import com.nami.world.World
import com.nami.world.resources.particle.Particle
import com.nami.world.resources.particle.ParticleListener
import org.joml.Vector3f
import kotlin.random.Random

class ParticleHandlerExplosion : ParticleListener {

    val random = Random(System.currentTimeMillis())

    private val direction = Vector3f(
        random.next(-1f..1f),
        random.next(-1f..1f),
        random.next(-1f..1f)
    ).normalize()

    private val speed = random.next(1.5f..3.0f)

    override fun update(particle: Particle.Instance, world: World) {
        particle.transform.position.add(
            Vector3f(direction).mul(speed * world.time.delta)
        )
    }

}