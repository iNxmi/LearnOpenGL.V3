package com.nami.world.resources.item.handlers

import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.entity.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener

class ItemBlockHandler : ItemListener {
    override fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        val position = player.getPositionBeforeFacingBlock(world) ?: return false
        val blockManager = world.blockManager

        //TODO blockId is very wrong here ... only temporary
        val blockId = item.template.id.split(".")[1]
        blockManager.setBlock(position, Resources.BLOCK.get(blockId).create(world, position))

        val chunk = world.chunkManager.getByBlockPosition(position)
        if (chunk != null)
            world.chunkManager.meshGenerator.addToQueue(chunk.position)

        return true
    }

    override fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

}