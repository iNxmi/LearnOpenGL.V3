package com.nami.world.block

import com.nami.Time
import com.nami.resources.Resources
import com.nami.resources.texture.Texture

abstract class Block(val id: String) {

    abstract val textures: Map<Face, Texture>

    open val layer: Layer = Layer.SOLID
    open val resistance: Map<String, Float> = mapOf()
    open val drops: Set<Drop> = setOf()
    open val tags: Set<String> = setOf()

    open fun onDamage(damage: Float) {}
    open fun onIgnition() {}
    open fun onDestroy() {}
    open fun onPlace() {}
    open fun onUse() {}

    open fun update(time: Time) {}

}