package com.nami.client.world.resources.item

import com.nami.client.world.player.Player

interface ItemListener {

    fun onPrimaryUse(item: Item.Instance, player: Player): Boolean

    fun onSecondaryUse(item: Item.Instance, player: Player): Boolean

}