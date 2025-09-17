package com.nami.world.resources.item.handlers

import com.nami.world.World
import com.nami.world.entity.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class ItemToolHandler : ItemListener {

    override fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        val block = player.getFacingBlock(world) ?: return false
        block.damage(item, 0.2f)

        val chunk = world.chunkManager.getByBlockPosition(block.position)
        if (chunk != null) {
            world.chunkManager.meshGenerator.addToQueue(chunk.position)
            world.chunkManager.saver.addToQueue(chunk.position)
        }

        if (block.health <= 0)
            block.template.drops?.forEach {
                if (!player.items.containsKey(it.item))
                    player.items[it.item] = it.item.create()

                player.items[it.item]!!.count += it.count()
            }

        return false
    }

    override fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

}