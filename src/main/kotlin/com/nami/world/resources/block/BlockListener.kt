package com.nami.world.resources.block

import com.nami.scene.SceneTime

interface BlockListener {

    fun onDamage(block: Block.Instance, damage: Float)
    fun onIgnition(block: Block.Instance)
    fun onDestroy(block: Block.Instance)
    fun onPlace(block: Block.Instance)
    fun onUse(block: Block.Instance)
    fun update(time: SceneTime, block: Block.Instance)

}