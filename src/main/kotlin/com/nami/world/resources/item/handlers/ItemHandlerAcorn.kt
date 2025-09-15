package com.nami.world.resources.item.handlers

import com.nami.world.World
import com.nami.world.entity.player.Player
import com.nami.world.feature.*
import com.nami.world.resources.block.Block
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener
import mu.KotlinLogging
import org.joml.Vector3i

class ItemHandlerAcorn : ItemListener {

    private val log = KotlinLogging.logger {}

    override fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        val position = player.getPositionBeforeFacingBlock(world) ?: return false
        val factors = world.biomeManager.getBiomeFactors(position)
        val blocks = FeatureBirchTree.generate(factors.x, factors.y, factors.z)

        val instances = mutableMapOf<Vector3i, Block.Instance>()
        blocks.forEach { (positionLocal, block) ->
            val positionGlobal = Vector3i(position).add(positionLocal)
            val instance = block.create(world, positionGlobal, 1.0f, 1.0f)
            instances[positionGlobal] = instance
        }

        world.blockManager.setBlocks(instances)
        return true
    }

    override fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

}