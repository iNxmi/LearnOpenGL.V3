package com.nami.world.resources.particle

import com.nami.Time
import com.nami.Transform
import com.nami.easing.EasingExpression
import com.nami.next
import com.nami.resources.particle.ResourceParticle
import com.nami.snakeToUpperCamelCase
import com.nami.world.World
import kotlinx.serialization.Serializable
import org.joml.Vector3f
import kotlin.math.roundToInt
import kotlin.random.Random

class Particle(
    id: String,
    val handlerClass: Class<ParticleListener>,

    val timeInSeconds: ClosedFloatingPointRange<Float>,
    val scale: ClosedFloatingPointRange<Float>,
    val colors: List<ParticleColor>,
    val easing: EasingExpression
) : ResourceParticle(id) {

    fun create(time: Time, position: Vector3f): Instance {
        val durationInSeconds =
            Math.random().toFloat() * (timeInSeconds.endInclusive - timeInSeconds.start) + timeInSeconds.start

        val random = Random(System.currentTimeMillis())
        val color = colors[random.next(0f..colors.size - 1f).roundToInt()].generate()

        val transform = Transform()
        transform.position.set(position)
        transform.scale.set(Vector3f(Math.random().toFloat() * (scale.endInclusive - scale.start) + scale.start))

        val handler = handlerClass.getDeclaredConstructor().newInstance()

        return Instance(
            this,
            handler,

            transform,
            time.time,
            durationInSeconds,
            color
        )
    }

    class Instance(
        val template: Particle,
        val handler: ParticleListener,

        val transform: Transform,
        val startTime: Float,
        val durationInSeconds: Float,
        val color: Vector3f
    ) {

        fun update(world: World) {
            handler.update(this, world)
        }

    }

    @Serializable
    data class JSON(
        val timeInSeconds: ClosedFloatingPointRange<Float>,
        val scale: ClosedFloatingPointRange<Float> = 1f..1f,
        val colors: List<ParticleColor>,
        val easing: EasingExpression
    ) {

        fun create(id: String): Particle {
            val handlerClass: Class<*> =
                try {
                    Class.forName("com.nami.world.resources.particle.handlers.ParticleHandler${id.snakeToUpperCamelCase()}")
                } catch (e: Exception) {
                    Class.forName("com.nami.world.resources.particle.handlers.DefaultParticleHandler")
                }

            val color = mutableListOf<ParticleColor>()
            colors.forEach {
                color.add(
                    ParticleColor(
                        it.color,
                        it.brightness
                    )
                )
            }

            return Particle(
                id,
                handlerClass as Class<ParticleListener>,

                timeInSeconds,
                scale,
                color,
                easing
            )
        }

    }

}