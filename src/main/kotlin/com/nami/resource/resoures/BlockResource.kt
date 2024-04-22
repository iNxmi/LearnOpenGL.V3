package com.nami.resource.resoures

import com.nami.constants.GamePaths
import com.nami.resource.Resource
import com.nami.world.Block
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class BlockResource : Resource<Block>(GamePaths.blocks, "json") {
    override fun onLoad(path: Path): Block {
        val jsonString = Files.readString(path)

        val blockTemplate: Block.BlockTemplate = Json.decodeFromString<Block.BlockTemplate>(jsonString)

       TODO()
    }

}