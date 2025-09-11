package com.nami.world.resources.item.handlers

import com.nami.world.entity.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class DefaultItemHandler : ItemListener {

    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

}