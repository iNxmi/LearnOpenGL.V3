package com.nami.resources.block

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.world.resources.block.Block
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