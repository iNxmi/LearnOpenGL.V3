package com.nami.world.block

import com.nami.Time
import com.nami.resources.texture.Texture
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh

abstract class Block(val id: String) {

    companion object {
        val set = mutableSetOf<Block>()
        val map = set.associateBy { it.id }

        fun get(id: String) = map[id]
    }

    init {
        set.add(this)
    }

    abstract val textures: Map<ChunkMesh.Face, Texture>

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

