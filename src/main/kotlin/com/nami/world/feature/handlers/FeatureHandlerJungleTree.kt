package com.nami.world.feature.handlers

import com.nami.random
import com.nami.world.World
import com.nami.world.feature.FeatureListener
import org.joml.Vector3i
import kotlin.math.absoluteValue

class FeatureHandlerJungleTree : FeatureListener {

    override fun generate(world: World, position: Vector3i) {
        val blockManager = world.blockManager

        val treeHeight = Int.random(12..6)

        for (i in 0 until treeHeight)
            blockManager.setBlock(Vector3i(position).add(0, i, 0), "jungle_log")

        for (i in 0 until 5) {
            val position = Vector3i(position).add(0, treeHeight, 0)
            for (j in 5 until 15) {
                val rand = Float.random(0f..1f)

                if ((0f..0.4f).contains(rand))
                    position.y += 1

                if ((0.4f..0.55f).contains(rand))
                    position.x += 1
                if ((0.55f..0.7f).contains(rand))
                    position.x -= 1

                if ((0.7f..0.85f).contains(rand))
                    position.z += 1
                if ((0.85f..1f).contains(rand))
                    position.z -= 1

                blockManager.setBlock(Vector3i(position), "jungle_log")

                for (z in -1..1)
                    for (y in -1..1)
                        for (x in -1..1) {
                            val position = Vector3i(position).add(x, y, z)

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 2)
                                continue

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 1)
                                if (Math.random() >= 0.25) continue

                            val block = blockManager.getBlock(Vector3i(position))
                            if (block != null)
                                continue

                            blockManager.setBlock(position, "jungle_leaves")
                        }
            }
        }
    }

}