package com.nami.resources.block

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.snakeToUpperCamelCase
import com.nami.world.block.Block
import com.nami.world.block.BlockDrop
import com.nami.world.block.BlockListener
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderBlock : Resources<Block>(GamePath.block, "block", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Block {
        val jsonString = Files.readString(path)
        val json: BlockJSON = Json.decodeFromString<BlockJSON>(jsonString)

        val textures: List<String> = listOf(
            json.textures.top,
            json.textures.bottom,

            json.textures.north,
            json.textures.east,
            json.textures.west,
            json.textures.south
        )

        textures.forEach { t ->
            if (!TEXTURE.map.containsKey(t))
                throw IllegalStateException("Unknown texture '$t' in '$path'")
        }

        val layer = when (json.layer) {
            "solid" -> Block.Layer.SOLID
            "transparent" -> Block.Layer.TRANSPARENT
            "foliage" -> Block.Layer.FOLIAGE
            "fluid" -> Block.Layer.FLUID
            else -> throw Exception("Unknown layer '${json.layer}' in '$path'")
        }

        val handlerClass: Class<*> =
            try {
                Class.forName("com.nami.world.block.handlers.BlockHandler${id.snakeToUpperCamelCase()}")
            } catch (e: Exception) {
                Class.forName("com.nami.world.block.handlers.DefaultBlockHandler")
            }

        val drops = mutableListOf<BlockDrop>()
        json.drops?.forEach { drops.add(BlockDrop(ITEM.get(it.item), it.amount.min, it.amount.max, it.rate)) }

        val block = Block(
            id,
            handlerClass as Class<BlockListener>,

            textures,
            layer,
            json.tags,
            json.resistance,
            drops
        )

        return block
    }

    override fun onLoadCompleted() {

    }

}