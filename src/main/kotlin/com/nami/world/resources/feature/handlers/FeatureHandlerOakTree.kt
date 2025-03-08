package com.nami.world.resources.feature.handlers

import com.nami.random
import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.resources.block.Block
import com.nami.world.resources.feature.FeatureListener
import org.joml.Vector3i
import kotlin.math.absoluteValue

class FeatureHandlerOakTree : FeatureListener {

    override fun generate(world: World, position: Vector3i): Map<Vector3i, Block.Instance> {
        val blocks = mutableMapOf<Vector3i, Block.Instance>()

        val baseHeight = Int.random(4..6)
        for (i in 0 until baseHeight)
            blocks[Vector3i(position).add(0, i, 0)] =
                Resources.BLOCK.get("oak_log").create(world, Vector3i(position).add(0, i, 0))

        for (i in 0 until 5) {
            val position = Vector3i(position).add(0, baseHeight, 0)
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

                blocks[Vector3i(position)] = Resources.BLOCK.get("oak_log").create(world, Vector3i(position))

                for (z in -1..1)
                    for (y in -1..1)
                        for (x in -1..1) {
                            val pos = Vector3i(position).add(x, y, z)

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 2)
                                continue

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 1)
                                if (Math.random() >= 0.25) continue

                            if (
                                (world.blockManager.getBlock(Vector3i(pos)) != null)
                                ||
                                (blocks[Vector3i(pos)] != null)
                            )
                                continue

                            blocks[Vector3i(pos)] =
                                Resources.BLOCK.get("oak_leaves").create(world, Vector3i(pos))
                        }
            }
        }

        return blocks
    }

}