package com.nami.world.material

import com.nami.resources.Resources

object MaterialMushroomYellow : Material(id = "mushroom_yellow") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.mushroom_yellow"),
        Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_yellow"),
        Face.NORTH to Resources.TEXTURE.get("block.mushroom_yellow"),
        Face.EAST to Resources.TEXTURE.get("block.mushroom_yellow"),
        Face.WEST to Resources.TEXTURE.get("block.mushroom_yellow"),
        Face.SOUTH to Resources.TEXTURE.get("block.mushroom_yellow")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("mushroom"), probability = 0.25f)
    )

}
