package com.nami.world.material

import com.nami.resources.Resources

object MaterialJungleLog : Material(id = "jungle_log") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.jungle_log_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.jungle_log_top"),
        Face.NORTH to Resources.TEXTURE.get("block.jungle_log_side"),
        Face.EAST to Resources.TEXTURE.get("block.jungle_log_side"),
        Face.WEST to Resources.TEXTURE.get("block.jungle_log_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.jungle_log_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.jungle_log"))
    )

}