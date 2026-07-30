package com.nami.world.item

import com.nami.world.item.items.*
import mu.KotlinLogging

abstract class Item(val id: String) {

    companion object {
        val set by lazy {
            setOf(
                ItemAcorn,
                ItemBirchLeaves,
                ItemBirchLog,
                ItemBirchPlanks,
                ItemBirchWorkbench,
                ItemCactus,
                ItemCobblestone,
                ItemDirt,
                ItemFlint,
                ItemFlintAxe,
                ItemFurnace,
                ItemGravel,
                ItemIce,
                ItemJungleLeaves,
                ItemJungleLog,
                ItemJunglePlanks,
                ItemJungleWorkbench,
                ItemLighter,
                ItemMushroom,
                ItemOakLeaves,
                ItemOakLog,
                ItemOakPlanks,
                ItemOakWorkbench,
                ItemSand,
                ItemSnow,
                ItemSnowball,
                ItemStick,
                ItemStone,
                ItemTnt
            )
        }
        val map by lazy { set.associateBy { it.id } }

        fun get(id: String) = map[id]
    }

    abstract val weight: Float
    open val tags: Set<String> = setOf()

    open fun onPrimaryUse(): Boolean = false
    open fun onSecondaryUse(): Boolean = false

}

