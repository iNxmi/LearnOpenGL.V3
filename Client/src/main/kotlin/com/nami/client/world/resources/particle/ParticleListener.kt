package com.nami.client.world.resources.particle

import com.nami.client.world.World

interface ParticleListener {

    fun update(particle: Particle.Instance, world: World)

}