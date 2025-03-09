package com.nami.client.resources.block

import com.nami.client.resources.GamePath
import com.nami.client.resources.Resources
import com.nami.client.world.resources.block.Block
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderBlock : Resources<Block>(GamePath.block, "block", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Block {
        val jsonString = Files.readString(path)
        val json: Block.JSON = Json.decodeFromString<Block.JSON>(jsonString)

        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

}