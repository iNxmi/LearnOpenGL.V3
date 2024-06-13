package com.nami.world.inventory.item.handlers

import com.nami.world.inventory.item.ItemListener
import com.nami.world.player.Player

class ItemHandlerIDtnt : ItemListener {
    override fun onPrimaryUse(player: Player): Boolean {
        val block = player.getBlockBeforeFacingBlock() ?: return false
        val position = block.position
        val blockManager = player.world.blockManager

        blockManager.setBlock(position, "tnt")
        player.world.chunkManager.getByBlockPosition(position)?.updateMesh()

        return true
    }

    override fun onSecondaryUse(player: Player): Boolean {
        return false
    }

}