package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemOakPlanks

object BlockOakPlanks : Block(id = "oak_planks") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.oak_planks")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakPlanks)
    )

}