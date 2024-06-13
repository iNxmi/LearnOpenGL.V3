package com.nami.world.block

interface BlockListener {

    fun onDamage(block: Block.Instance, damage: Float)
    fun onDestroy(block: Block.Instance)
    fun onPlace(block: Block.Instance)
    fun onUse(block: Block.Instance)
    fun update(block: Block.Instance)

}