package com.nami.world.resources.particle.handlers

import com.nami.world.World
import com.nami.world.resources.block.Block
import com.nami.world.resources.particle.Particle
import com.nami.world.resources.particle.ParticleListener
import org.joml.Vector2i
import org.joml.Vector3f

class ParticleHandlerSnow : ParticleListener {

    private val speed = 1.0f

    override fun update(particle: Particle.Instance, world: World) {
        val transform = particle.transform
        transform.position.add(Vector3f(0f, -1f, 0f).mul(speed * world.time.delta))

        val blockPositionXZ = Vector2i(transform.position.x.toInt(), transform.position.z.toInt())
        val terrainHeight = world.blockManager.getHeight(
            blockPositionXZ,
            512,
            setOf(Block.Layer.SOLID, Block.Layer.TRANSPARENT, Block.Layer.FOLIAGE)
        )

        if (transform.position.y < terrainHeight + 1)
            transform.position.y = (terrainHeight + 1).toFloat()
    }

}