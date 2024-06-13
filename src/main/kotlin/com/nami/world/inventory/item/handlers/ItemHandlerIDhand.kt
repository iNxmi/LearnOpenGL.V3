package com.nami.world.inventory.item.handlers

import com.nami.world.inventory.item.ItemListener
import com.nami.world.player.Player

class ItemHandlerIDhand : ItemListener {
    override fun onPrimaryUse(player: Player): Boolean {
        val block = player.getFacingBlock() ?: return false
        block.damage(0.2f)
        player.world.chunkManager.getByBlockPosition(block.position)?.updateMesh()

        if (block.health <= 0)
            block.template.drops?.forEach { player.inventory.add(it.item, it.drop()) }

        return false
    }

    override fun onSecondaryUse(player: Player): Boolean {
        return false
    }

}