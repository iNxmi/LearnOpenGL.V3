package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemBirchWorkbench

object BlockBirchWorkbench : Block(id = "birch_workbench") {

    override val textures = mapOf(
        Face.TOP to "block.birch_workbench_top",
        Face.BOTTOM to "block.birch_planks",
        Face.NORTH to "block.birch_workbench_front",
        Face.EAST to "block.birch_workbench_side",
        Face.WEST to "block.birch_workbench_side",
        Face.SOUTH to "block.birch_workbench_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "workbench"
    )

    override val drops = setOf(
        Drop(ItemBirchWorkbench)
    )

}