package com.nami.world.resources.item.handlers

import com.nami.world.World
import com.nami.world.entity.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class DefaultItemHandler : ItemListener {

    override fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

    override fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

}