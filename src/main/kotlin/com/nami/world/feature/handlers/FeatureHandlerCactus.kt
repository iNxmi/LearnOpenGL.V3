package com.nami.world.feature.handlers

import com.nami.world.World
import com.nami.world.feature.FeatureListener
import org.joml.Vector3i
import kotlin.math.roundToInt

class FeatureHandlerCactus : FeatureListener {

    override fun generate(world: World, position: Vector3i) {
        val rand = (Math.random() * 2.0).roundToInt()
        val h = 2 + rand
        for (y in position.y + 1 until position.y + 1 + h) {
            world.blockManager.setBlock(Vector3i(position.x, y, position.z), "cactus")
        }
    }

}