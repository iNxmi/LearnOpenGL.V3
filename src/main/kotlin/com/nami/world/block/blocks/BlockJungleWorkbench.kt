package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemJungleWorkbench

object BlockJungleWorkbench : Block(id = "jungle_workbench") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.jungle_workbench_top",
        ChunkMesh.Face.BOTTOM to "block.jungle_planks",
        ChunkMesh.Face.NORTH to "block.jungle_workbench_front",
        ChunkMesh.Face.EAST to "block.jungle_workbench_side",
        ChunkMesh.Face.WEST to "block.jungle_workbench_side",
        ChunkMesh.Face.SOUTH to "block.jungle_workbench_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "workbench"
    )

    override val drops = setOf(
        Drop(ItemJungleWorkbench)
    )

}