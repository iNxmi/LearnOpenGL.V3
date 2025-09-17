package com.nami.world.block

import com.nami.resources.Resources

object BlockOakPlanks : Block(id = "oak_planks") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.oak_planks"),
        Face.BOTTOM to Resources.TEXTURE.get("block.oak_planks"),
        Face.NORTH to Resources.TEXTURE.get("block.oak_planks"),
        Face.EAST to Resources.TEXTURE.get("block.oak_planks"),
        Face.WEST to Resources.TEXTURE.get("block.oak_planks"),
        Face.SOUTH to Resources.TEXTURE.get("block.oak_planks")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.oak_planks"))
    )

}