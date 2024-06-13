package com.nami.resources.block

import com.nami.resources.GamePath
import com.nami.resources.Resource
import com.nami.resources.item.Items
import com.nami.world.block.Block
import com.nami.world.block.BlockDrop
import com.nami.world.block.BlockListener
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceBlock : Resource<Block>(GamePath.blocks, arrayOf("json")) {

    @Serializable
    private data class TextureNamesJSON(
        val top: String,
        val bottom: String,

        val north: String,
        val east: String,
        val west: String,
        val south: String,
    )

    @Serializable
    private data class DropJSON(
        val item: String,
        val min: Int = 1,
        val max: Int = 1,
        val rate: Float = 1.0f
    )

    @Serializable
    private data class BlockJSON(
        val id: String,
        val textures: TextureNamesJSON,
        val layer: String,
        val tags: Array<String>? = null,
        val resistance: Float,
        val handler: String? = null,
        val drops: Array<DropJSON>? = null
    )

    override fun onLoad(path: Path): Block {
        val jsonString = Files.readString(path)
        val json: BlockJSON = Json.decodeFromString<BlockJSON>(jsonString)

        val textures: Array<String> = arrayOf(
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
            "foliage" -> Block.Layer.FOLIAGE
            "fluid" -> Block.Layer.FLUID
            else -> throw Exception("Unknown layer '${json.layer}' in '$path'")
        }

        var handler: BlockListener? = null
        try {
            handler = Class.forName(json.handler ?: "com.nami.world.block.handlers.BlockHandlerID${json.id}")
                .getDeclaredConstructor().newInstance() as BlockListener
        } catch (e: Exception) {

        }

        val drops = mutableListOf<BlockDrop>()
        json.drops?.forEach { drops.add(BlockDrop(Items.get(it.item), it.min, it.max, it.rate)) }

        val block = Block(
            json.id,
            textures,
            layer,
            json.resistance,
            handler,
            if (drops.isEmpty()) null else drops.toTypedArray()
        )

        return block
    }

    override fun onLoadCompleted() {

    }

}