package com.nami.world.resources.item

import com.nami.world.World
import com.nami.world.entity.Player

interface ItemListener {

    fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean
    fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean

}