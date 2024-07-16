package com.nami.world.inventory.item.handlers

import com.nami.world.inventory.item.Item
import com.nami.world.inventory.item.ItemListener
import com.nami.world.player.Player

class ItemBlockHandler : ItemListener {
    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        val position = player.getPositionBeforeFacingBlock() ?: return false
        val blockManager = player.world.blockManager

        //TODO item.id is very wrong here .... only temporary
        blockManager.setBlock(position, item.template.id.split(".")[1])

        val chunk = player.world.chunkManager.getByBlockPosition(position)
        if (chunk != null)
            player.world.chunkManager.meshGenerator.addToQueue(chunk.position)

        return true
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }
}