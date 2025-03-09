package com.nami.client.world.resources.item.handlers

import com.nami.client.resources.Resources
import com.nami.client.world.player.Player
import com.nami.client.world.resources.item.Item
import com.nami.client.world.resources.item.ItemListener

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