package com.nami.world.resources.item.handlers

import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.entity.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class ItemHandlerAcorn : ItemListener {

    override fun onPrimaryUse(world: World,item: Item.Instance, player: Player): Boolean {
        val position = player.getPositionBeforeFacingBlock(world) ?: return false
        Resources.FEATURE.get("tree").create().handler.generate(world, position)

        return true
    }

    override fun onSecondaryUse(world: World,item: Item.Instance, player: Player): Boolean {
        return false
    }

}