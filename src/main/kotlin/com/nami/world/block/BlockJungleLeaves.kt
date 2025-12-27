package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.item.ItemAcorn
import com.nami.world.item.ItemJungleLeaves
import com.nami.world.item.ItemStick

object BlockJungleLeaves : Block(id = "jungle_leaves") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.BOTTOM to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.NORTH to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.EAST to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.WEST to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.SOUTH to Resources.TEXTURE.get("block.jungle_leaves")
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemJungleLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}