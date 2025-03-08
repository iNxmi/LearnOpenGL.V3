package com.nami.world.resources.particle

import com.nami.world.World

interface ParticleListener {

    fun update(particle: Particle.Instance, world: World)

}