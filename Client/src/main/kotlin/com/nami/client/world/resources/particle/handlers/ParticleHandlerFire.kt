package com.nami.client.world.resources.particle.handlers

import com.nami.client.world.World
import com.nami.client.world.resources.particle.Particle
import com.nami.client.world.resources.particle.ParticleListener

class ParticleHandlerFire : ParticleListener {
    override fun update(particle: Particle.Instance, world: World) {
        particle.transform.position.add(0f, 1f * world.time.delta, 0f)
    }

}