package com.nami.world.inventory.item

import com.nami.world.player.Player

interface ItemListener {

    fun onPrimaryUse(item: Item.Instance, player: Player): Boolean

    fun onSecondaryUse(item: Item.Instance, player: Player): Boolean

}