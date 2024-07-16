package com.nami.world.inventory.item.handlers

import com.nami.world.inventory.item.Item
import com.nami.world.inventory.item.ItemListener
import com.nami.world.player.Player

class DefaultItemHandler : ItemListener {

    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

}