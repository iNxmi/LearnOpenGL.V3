package com.nami.world.tree

import com.nami.world.World
import org.joml.Vector3i

interface TreeGenerator {

    fun generate(position: Vector3i, world: World)

}