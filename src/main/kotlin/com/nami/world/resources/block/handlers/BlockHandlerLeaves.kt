package com.nami.world.resources.block.handlers

import com.nami.Time
import com.nami.world.resources.block.Block
import com.nami.world.resources.block.BlockListener
import org.joml.Vector3f

class BlockHandlerLeaves : BlockListener {

    override fun onDamage(block: Block.Instance, damage: Float) {

    }

    override fun onIgnition(block: Block.Instance) {

    }

    override fun onDestroy(block: Block.Instance) {

    }

    override fun onPlace(block: Block.Instance) {

    }

    override fun onUse(block: Block.Instance) {

    }

    override fun update(time: Time, block: Block.Instance) {
        if (Math.random() >= 0.9)
            block.world.particleManager.spawn(
                "leaf",
                Vector3f(block.position).add(Math.random().toFloat(), 0.5f, Math.random().toFloat())
            )
    }
}