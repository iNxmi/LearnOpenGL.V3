package com.nami.world.block

import com.nami.Time
import com.nami.world.Drop
import com.nami.world.block.blocks.*
import com.nami.world.chunk.ChunkMesh

abstract class Block(val id: String) {

    companion object {
        val set by lazy {
            setOf(
                BlockBedrock,
                BlockBirchLeaves,
                BlockBirchLog,
                BlockBirchPlanks,
                BlockBirchWorkbench,
                BlockCactus,
                BlockCobblestone,
                BlockDirt,
                BlockError,
                BlockFurnace,
                BlockGrass,
                BlockGravel,
                BlockIce,
                BlockJungleLeaves,
                BlockJungleLog,
                BlockJunglePlanks,
                BlockJungleWorkbench,
                BlockMushroomRed,
                BlockMushroomStem,
                BlockMushroomYellow,
                BlockMycelium,
                BlockOakLeaves,
                BlockOakLog,
                BlockOakPlanks,
                BlockOakWorkbench,
                BlockPodzol,
                BlockSand,
                BlockSnow,
                BlockStone,
                BlockTNT,
                BlockWater
            )
        }
        val map by lazy { set.associateBy { it.id } }

        fun get(id: String) = map[id]
    }

    abstract val textures: Map<Face, String>

    // abstract val model: Model
    open val layer: Layer = Layer.SOLID
    open val resistance: Map<String, Float> = mapOf()
    open val drops: Set<Drop> = setOf()
    open val tags: Set<String> = setOf()

    open fun onDamage(damage: Float) {}
    open fun onIgnition() {}
    open fun onDestroy() {}
    open fun onPlace() {}
    open fun onUse() {}
    open fun onUpdate(time: Time) {}

}

