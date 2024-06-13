package com.nami.world.block

import com.nami.world.World
import org.joml.Vector3i
import kotlin.math.roundToInt

data class Block(
    val id: String,
    val textures: Array<String>,
    val layer: Layer,
    val resistance: Float,
    val handler: BlockListener?,
    val drops: Array<BlockDrop>?
) {

    fun create(world: World, position: Vector3i): Instance {
        return Instance(world, position, this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Block

        if (id != other.id) return false
        if (!textures.contentEquals(other.textures)) return false
        if (layer != other.layer) return false
        if (resistance != other.resistance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + textures.contentHashCode()
        result = 31 * result + layer.hashCode()
        result = 31 * result + resistance.hashCode()
        return result
    }

    enum class Layer {
        SOLID,
        FOLIAGE,
        FLUID
    }

    class Instance(
        val world: World,
        val position: Vector3i,
        val template: Block,
        var brightness: Float = 1f,
        var health: Float = 1f
    ) {

        fun update() {
            template.handler?.update(this)
        }

        fun destroy() {
            template.handler?.onDestroy(this)
            world.blockManager.setBlock(position, null)
        }

        fun damage(damage: Float): Float {
            health -= damage * (1f - template.resistance)

            template.handler?.onDamage(this, damage)

            val roundedHealth = ((health * 100f).roundToInt() / 100f)
            if (roundedHealth <= 0f) {
                health = 0f
                destroy()
            }

            return health
        }

    }

}

