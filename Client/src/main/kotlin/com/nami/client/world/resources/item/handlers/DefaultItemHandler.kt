package com.nami.client.world.resources.item.handlers

import com.nami.client.world.player.Player
import com.nami.client.world.resources.item.Item
import com.nami.client.world.resources.item.ItemListener

class DefaultItemHandler : ItemListener {

    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

}