package com.nami.world.resources.item.handlers

import com.nami.resources.Resources
import com.nami.world.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class ItemHandlerAcorn : ItemListener {

    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        val position = player.getPositionBeforeFacingBlock() ?: return false
        Resources.FEATURE.get("tree").create().handler.generate(player.world, position)

        return true
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

}