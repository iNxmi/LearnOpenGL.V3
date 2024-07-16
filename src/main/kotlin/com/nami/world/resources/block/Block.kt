package com.nami.world.resources.block

import com.nami.resources.block.ResourceBlock
import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.resources.item.Item
import org.joml.Vector3i
import kotlin.math.min
import kotlin.math.roundToInt

class Block(
    id: String,
    private val handlerClass: Class<BlockListener>,

    val textures: List<String>,
    val layer: Layer,
    val tags: List<String>?,
    val resistance: Map<String, Float>,
    val drops: List<BlockDrop>?
) : ResourceBlock(id) {

    fun create(world: World, position: Vector3i): Instance {
        val handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler, world, position)
    }

    enum class Layer {
        SOLID,
        TRANSPARENT,
        FOLIAGE,
        FLUID
    }

    class Instance(
        val template: Block,
        val handler: BlockListener,

        val world: World,
        val position: Vector3i,
        var brightness: Float = 1f,
        var health: Float = 1f
    ) {

        fun update(time: SceneTime) {
            handler.update(time, this)
        }

        fun destroy() {
            handler.onDestroy(this)
            world.blockManager.setBlock(position, null)
        }

        fun damage(item: Item, damage: Float): Float {
            val criteria = mutableListOf<String>()
            criteria.add("item.${item.id}")
            item.tags?.forEach { criteria.add("tag.$it") }

            var resistance = 1f
            template.resistance.forEach { (crit, value) ->
                if (!criteria.contains(crit))
                    return@forEach

                resistance = min(resistance, value)
            }

            health -= damage * (1f - resistance)
            handler.onDamage(this, damage)

            val roundedHealth = ((health * 100f).roundToInt() / 100f)
            if (roundedHealth <= 0f) {
                health = 0f
                destroy()
            }

            return health
        }

    }

}

