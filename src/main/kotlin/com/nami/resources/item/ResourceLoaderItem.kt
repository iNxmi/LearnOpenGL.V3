package com.nami.resources.item

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.snakeToUpperCamelCase
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderItem : Resources<Item>(GamePath.item, "item", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Item {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<ItemJSON>(jsonString)

        val itemPath = id.split(".")
        val handlerPath = when (itemPath[0]) {
            "block" -> "com.nami.world.resources.item.handlers.ItemBlockHandler"
            "tool" -> "com.nami.world.resources.item.handlers.ItemToolHandler"
            else -> "com.nami.world.resources.item.handlers.ItemHandler${id.snakeToUpperCamelCase()}"
        }

        var handlerClass: Class<*> = try {
            Class.forName(handlerPath)
        } catch (e: Exception) {
            Class.forName("com.nami.world.resources.item.handlers.DefaultItemHandler")
        }

        return Item(
            id,
            handlerClass as Class<ItemListener>,

            json.weight,
            json.tags,

        )
    }

    override fun onLoadCompleted() {

    }

}