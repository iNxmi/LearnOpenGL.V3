package com.nami.world.block

import com.nami.resources.Resources

object BlockBirchWorkbench : Block(id = "birch_workbench") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.birch_workbench_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.birch_planks"),
        Face.NORTH to Resources.TEXTURE.get("block.birch_workbench_front"),
        Face.EAST to Resources.TEXTURE.get("block.birch_workbench_side"),
        Face.WEST to Resources.TEXTURE.get("block.birch_workbench_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.birch_workbench_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "workbench"
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.birch_workbench"))
    )

}