package com.nami.world.resources.item.handlers

import com.nami.world.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class ItemToolHandler : ItemListener {

    override fun onPrimaryUse(item: Item.Instance, player: Player): Boolean {
        val block = player.getFacingBlock() ?: return false
        block.damage(item.template, 0.2f)

        val chunk = player.world.chunkManager.getByBlockPosition(block.position)
        if (chunk != null)
            player.world.chunkManager.meshGenerator.addToQueue(chunk.position)

//        if (block.health <= 0)
//            block.template.drops?.forEach { player.inventory.add(it.item, it.drop()) }

        return false
    }

    override fun onSecondaryUse(item: Item.Instance, player: Player): Boolean {
        return false
    }

}