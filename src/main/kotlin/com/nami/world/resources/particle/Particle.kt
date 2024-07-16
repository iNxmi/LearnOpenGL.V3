package com.nami.world.resources.particle

import com.nami.entity.Transform
import com.nami.random
import com.nami.resources.particle.ResourceParticle
import com.nami.scene.SceneTime
import com.nami.world.World
import org.joml.Vector3f
import kotlin.math.roundToInt

class Particle(
    id: String,
    val handlerClass: Class<ParticleListener>,

    val timeInSeconds: ClosedFloatingPointRange<Float>,
    val scale: ClosedFloatingPointRange<Float>,
    val colors: List<ParticleColor>
) : ResourceParticle(id) {

    fun create(sceneTime: SceneTime, position: Vector3f): Instance {
        val durationInSeconds =
            Math.random().toFloat() * (timeInSeconds.endInclusive - timeInSeconds.start) + timeInSeconds.start

        val color = colors[Float.random(0f..colors.size - 1f).roundToInt()].generate()

        val transform = Transform()
        transform.position = position
        transform.scale = Vector3f(Math.random().toFloat() * (scale.endInclusive - scale.start) + scale.start)

        val acceleration = Transform()
        acceleration.scale.set(0f)

        val handler = handlerClass.getDeclaredConstructor().newInstance()

        return Instance(
            this,
            handler,

            transform,
            acceleration,
            sceneTime.time,
            durationInSeconds,
            color
        )
    }

    class Instance(
        val template: Particle,
        val handler: ParticleListener,

        val transform: Transform,
        val acceleration: Transform,
        val startTime: Float,
        val durationInSeconds: Float,
        val color: Vector3f
    ) {

        fun update(world: World, time: SceneTime) {
            handler.update(this, world, time)
        }

    }

}