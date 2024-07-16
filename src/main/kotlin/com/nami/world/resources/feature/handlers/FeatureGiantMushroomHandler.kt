package com.nami.world.resources.feature.handlers

import com.nami.random
import com.nami.world.World
import com.nami.world.resources.block.Block
import com.nami.world.resources.feature.FeatureListener
import org.joml.Vector2i
import org.joml.Vector3i

open class FeatureGiantMushroomHandler(val block: Block, val heightRange: IntRange, val radiusRange: IntRange) :
    FeatureListener {
    override fun generate(world: World, position: Vector3i) {
        val blockManager = world.blockManager
        val height = blockManager.getHeight(
            Vector2i(position.x, position.z),
            512,
            setOf(Block.Layer.SOLID, Block.Layer.TRANSPARENT)
        )

        val stemHeight = Int.random(heightRange)
        for (i in 0 until stemHeight) {
            val y = height + i + 1
            blockManager.setBlock(Vector3i(position.x, y, position.z), "mushroom_stem")

            val radius = Int.random(radiusRange)
            if (i == stemHeight - 1) {
                for (zo in -radius..radius)
                    for (xo in -radius..radius)
                        if (zo * zo + xo * xo <= radius * radius)
                            blockManager.setBlock(
                                Vector3i(position.x + xo, y + 1, position.z + zo),
                                block.id
                            )

                for (zo in -(radius + 1)..(radius + 1))
                    for (xo in -(radius + 1)..(radius + 1))
                        if (zo * zo + xo * xo <= (radius + 1) * (radius + 1))
                            blockManager.setBlock(
                                Vector3i(position.x + xo, y, position.z + zo),
                                block.id
                            )

                for (zo in -(radius - 2)..(radius - 2))
                    for (xo in -(radius - 2)..(radius - 2))
                        if (zo * zo + xo * xo <= (radius + 2) * (radius - 2))
                            blockManager.setBlock(
                                Vector3i(position.x + xo, y, position.z + zo),
                                null
                            )

                blockManager.setBlock(
                    Vector3i(position.x, y, position.z),
                    "mushroom_stem"
                )
            }
        }
    }

}