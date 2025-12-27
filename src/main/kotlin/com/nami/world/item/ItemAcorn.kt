package com.nami.world.item

object ItemAcorn : Item("acorn") {

    override val weight = 0.1F

    override fun onPrimaryUse(): Boolean {
//        val position = player.getPositionBeforeFacingBlock(world) ?: return false
//        val factors = world.biomeManager.getBiomeFactors(position)
//        val blocks = FeatureBirchTree.generate(factors.x, factors.y, factors.z)
//
//        val instances = mutableMapOf<Vector3i, Block>()
//        blocks.forEach { (positionLocal, block) ->
//            val positionGlobal = Vector3i(position).add(positionLocal)
//            val instance = block
//            instances[positionGlobal] = instance
//        }
//
//        world.blockManager.setBlocks(instances)
        return false
    }

}