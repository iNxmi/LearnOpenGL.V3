package com.nami.world.feature

import com.nami.world.World
import org.joml.Vector3i

interface FeatureListener {

    fun generate(world: World, position: Vector3i)

}