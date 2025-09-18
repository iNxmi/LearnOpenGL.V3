package com.nami.world.material

import com.nami.resources.Resources

object MaterialMushroomStem : Material(id = "mushroom_stem") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.mushroom_stem"),
        Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_stem"),
        Face.NORTH to Resources.TEXTURE.get("block.mushroom_stem"),
        Face.EAST to Resources.TEXTURE.get("block.mushroom_stem"),
        Face.WEST to Resources.TEXTURE.get("block.mushroom_stem"),
        Face.SOUTH to Resources.TEXTURE.get("block.mushroom_stem")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("mushroom"), probability = 0.25f)
    )

}
