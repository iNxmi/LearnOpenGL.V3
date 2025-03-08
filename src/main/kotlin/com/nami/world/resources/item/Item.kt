package com.nami.world.resources.item

import com.nami.resources.Resources
import com.nami.resources.item.ResourceItem
import com.nami.snakeToUpperCamelCase
import kotlinx.serialization.Serializable

class Item(
    id: String,
    private val handlerClass: Class<ItemListener>,

    val weight: Float,
    val tags: List<String>?
) : ResourceItem(id) {

    fun create(data: Map<String, String> = mapOf(), count: Int = 0): Instance {
        val handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler, data, count)
    }

    class Instance(
        val template: Item,
        val handler: ItemListener,

        val data: Map<String, String>,
        var count: Int
    ) {

        @Serializable
        data class JSON(
            val id: String,
            val data: Map<String, String>
        ) {

            fun create(): Instance {
                val template = Resources.ITEM.get(id)
                return template.create(data)
            }

        }

        fun json(): JSON {
            return JSON(template.id, data)
        }

    }

    @Serializable
    data class JSON(
        val tags: List<String>? = null,
        val weight: Float,
        val handler: String? = null
    ) {

        fun create(id: String): Item {
            val itemPath = id.split(".")
            val handlerPath = when (itemPath[0]) {
                "block" -> "com.nami.world.resources.item.handlers.ItemBlockHandler"
                "tool" -> "com.nami.world.resources.item.handlers.ItemToolHandler"
                else -> "com.nami.world.resources.item.handlers.ItemHandler${id.snakeToUpperCamelCase()}"
            }

            var handlerClass: Class<ItemListener> = try {
                Class.forName(handlerPath)
            } catch (e: Exception) {
                Class.forName("com.nami.world.resources.item.handlers.DefaultItemHandler")
            } as Class<ItemListener>

            return Item(
                id,
                handlerClass,

                weight,
                tags,
            )
        }

    }

}