package com.nami.resources.item

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.serializer.GlobalJSON
import com.nami.world.resources.item.Item
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderItem : Resources<Item>(GamePath.item, "item", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Item {
        val jsonString = Files.readString(path)
        val json = GlobalJSON.instance.decodeFromString<Item.JSON>(jsonString)

        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

}