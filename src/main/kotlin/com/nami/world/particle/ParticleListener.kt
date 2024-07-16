package com.nami.world.particle

import com.nami.scene.SceneTime
import com.nami.world.World

interface ParticleListener {

    fun update(particle: Particle.Instance, world: World, time: SceneTime)

}