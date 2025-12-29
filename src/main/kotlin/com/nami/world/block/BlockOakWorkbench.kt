package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemOakWorkbench

object BlockOakWorkbench : Block(id = "oak_workbench") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.oak_workbench_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.oak_planks"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.oak_workbench_front"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.oak_workbench_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.oak_workbench_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.oak_workbench_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "workbench"
    )

    override val drops = setOf(
        Drop(ItemOakWorkbench)
    )

}