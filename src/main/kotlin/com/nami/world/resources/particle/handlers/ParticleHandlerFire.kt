package com.nami.world.resources.particle.handlers

import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.resources.particle.Particle
import com.nami.world.resources.particle.ParticleListener

class ParticleHandlerFire : ParticleListener {
    override fun update(particle: Particle.Instance, world: World, time: SceneTime) {
        particle.transform.position.add(0f, 1f * time.delta, 0f)
    }

}