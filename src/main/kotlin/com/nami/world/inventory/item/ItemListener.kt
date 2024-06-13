package com.nami.world.inventory.item

import com.nami.world.player.Player

interface ItemListener {

    fun onPrimaryUse(player: Player): Boolean

    fun onSecondaryUse(player: Player): Boolean

}