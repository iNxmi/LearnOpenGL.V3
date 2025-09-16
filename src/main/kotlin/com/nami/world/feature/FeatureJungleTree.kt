package com.nami.world.feature

import com.nami.next
import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.absoluteValue
import kotlin.random.Random

object FeatureJungleTree : Feature(id = "jungle_tree") {

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

        val treeHeight = random.next(12..6)
        for (i in 0 until treeHeight)
            blocks[Vector3i(0, i, 0)] = Resources.BLOCK.get("jungle_log")

        for (i in 0 until 5) {
            val position = Vector3i(0, treeHeight, 0)
            for (j in 5 until 15) {
                val rand = random.next(0f..1f)

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

                blocks[Vector3i(position)] = Resources.BLOCK.get("jungle_log")

                for (z in -1..1)
                    for (y in -1..1)
                        for (x in -1..1) {
                            val pos = Vector3i(position).add(x, y, z)

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 2)
                                continue

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 1)
                                if (random.nextFloat() >= 0.25) continue

                            if (blocks[Vector3i(pos)] != null)
                                continue

                            blocks[Vector3i(pos)] = Resources.BLOCK.get("jungle_leaves")
                        }
            }
        }

        return blocks
    }

}