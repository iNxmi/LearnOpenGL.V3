package com.nami.client.resources.item

import com.nami.client.resources.GamePath
import com.nami.client.resources.Resources
import com.nami.client.world.resources.item.Item
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderItem : Resources<Item>(GamePath.item, "item", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Item {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<Item.JSON>(jsonString)

        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

}