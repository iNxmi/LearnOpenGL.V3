package com.nami.resources.item

import com.nami.resources.GamePath
import com.nami.resources.Resource
import com.nami.world.inventory.item.Item
import com.nami.world.inventory.item.ItemListener
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceItem : Resource<Item>(GamePath.items, arrayOf("json")) {

    @Serializable
    private data class ItemJSON(
        val id: String,
        val type: String,
        val weight: Float,
        val handler: String? = null
    )

    override fun onLoad(path: Path): Item {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<ItemJSON>(jsonString)

        var handler: ItemListener? = null
        try {
            handler = Class.forName(json.handler ?: "com.nami.world.inventory.item.handlers.ItemHandlerID${json.id}")
                .getDeclaredConstructor().newInstance() as ItemListener
        } catch (e: Exception) {

        }

        return Item(
            json.id,
            json.weight,
            handler
        )
    }

    override fun onLoadCompleted() {

    }

}