package com.nami.world.resources.block

import com.nami.Time
import com.nami.resources.Resources
import com.nami.resources.Resources.Companion.ITEM
import com.nami.resources.Resources.Companion.TEXTURE
import com.nami.resources.block.BlockDropJSON
import com.nami.resources.block.BlockTextureNamesJSON
import com.nami.resources.block.ResourceBlock
import com.nami.snakeToUpperCamelCase
import com.nami.world.World
import com.nami.world.resources.item.Item
import kotlinx.serialization.Serializable
import org.joml.Vector3i
import kotlin.math.min
import kotlin.math.roundToInt

class Block(
    id: String,
    private val handlerClass: Class<BlockListener>,

    val textures: List<String>,
    val layer: Layer,
    val tags: Set<String>?,
    val resistance: Map<String, Float>,
    val drops: Set<BlockDrop>?
) : ResourceBlock(id) {

    fun create(
        world: World,
        position: Vector3i,
        brightness: Float = 1.0f,
        health: Float = 1.0f
    ): Instance {
        val handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler, world, position, brightness, health)
    }

    class Instance(
        val template: Block,
        val handler: BlockListener,

        val world: World,
        val position: Vector3i,
        var brightness: Float,
        var health: Float
    ) {

        fun update(time: Time) {
            handler.update(time, this)
        }

        fun destroy() {
            handler.onDestroy(this)
            world.blockManager.setBlock(position, null)
        }

        fun damage(item: Item.Instance, damage: Float): Float {
            val criteria = mutableListOf<String>()
            criteria.add("item.${item.template.id}")
            item.template.tags?.forEach { criteria.add("tag.$it") }

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

        @Serializable
        data class JSON(
            val id: String,
            val brightness: Float,
            val health: Float
        ) {

            fun create(world: World, position: Vector3i): Instance {
                val template = Resources.BLOCK.get(id)
                val instance = template.create(world, position, brightness = brightness, health = health)
                return instance
            }

        }

        fun json(): JSON {
            return JSON(template.id, brightness, health)
        }

    }

}

