package com.nami.world.resources.item.handlers

import com.nami.world.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

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