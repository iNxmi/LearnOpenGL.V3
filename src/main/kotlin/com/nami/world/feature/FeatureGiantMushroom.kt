package com.nami.world.feature

import com.nami.next
import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.random.Random

open class FeatureGiantMushroom(
    val block: Block,
    val heightRange: IntRange,
    val radiusRange: IntRange
) : Feature() {

    override fun shouldGenerate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float,
        seed: Long
    ): Map<Vector3i, Block> {
        val random = Random(seed)
        val blocks = mutableMapOf<Vector3i, Block>()

        val stemHeight = random.next(heightRange)
        for (y in 0 until stemHeight) {
            blocks[Vector3i(0, y, 0)] = Resources.BLOCK.get("mushroom_stem")

            val radius = random.next(radiusRange)
            if (y == stemHeight - 1) {
                for (z in -radius..radius)
                    for (x in -radius..radius)
                        if (z * z + x * x <= radius * radius)
                            blocks[Vector3i(x, y + 1, z)] = block

                for (z in -(radius + 1)..(radius + 1))
                    for (x in -(radius + 1)..(radius + 1))
                        if (z * z + x * x <= (radius + 1) * (radius + 1))
                            blocks[Vector3i(x, y, z)] = block

                for (z in -(radius - 2)..(radius - 2))
                    for (x in -(radius - 2)..(radius - 2))
                        if (z * z + x * x <= (radius + 2) * (radius - 2))
                            blocks.remove(Vector3i(x, y, z))

                blocks[Vector3i(0, y, 0)] = Resources.BLOCK.get("mushroom_stem")
            }
        }

        return blocks
    }

}