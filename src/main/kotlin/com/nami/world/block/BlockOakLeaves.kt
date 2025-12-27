package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.item.ItemAcorn
import com.nami.world.item.ItemOakLeaves
import com.nami.world.item.ItemStick

object BlockOakLeaves : Block(id = "oak_leaves") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.oak_leaves"),
        Face.BOTTOM to Resources.TEXTURE.get("block.oak_leaves"),
        Face.NORTH to Resources.TEXTURE.get("block.oak_leaves"),
        Face.EAST to Resources.TEXTURE.get("block.oak_leaves"),
        Face.WEST to Resources.TEXTURE.get("block.oak_leaves"),
        Face.SOUTH to Resources.TEXTURE.get("block.oak_leaves")
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}