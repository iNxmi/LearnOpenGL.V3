package com.nami.world.feature

import com.nami.next
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.absoluteValue
import kotlin.random.Random

open class FeatureTree(
    val blockStem: Block,
    val blockLeaves: Block,
    val stemHeight: IntRange,
    val stemThickness: IntRange,
    val leafRadius: Int,

    val probabilityUp: Float,
    val probabilityDown: Float,
    val probabilityLeft: Float,
    val probabilityRight: Float,
    val probabilityFront: Float,
    val probabilityBack: Float,
    id: String
) : Feature(id) {

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

        val baseHeight = random.next(stemHeight)
        for (i in 0 until baseHeight)
            blocks[Vector3i(0, i, 0)] = blockStem

        for (i in 0 until 5) {
            val position = Vector3i(0, baseHeight, 0)
            for (j in 5 until 15) {
                val rand =
                    random.next(0f..(probabilityUp + probabilityDown + probabilityLeft + probabilityRight + probabilityFront + probabilityBack))

                if ((0f..probabilityUp).contains(rand))
                    position.y += 1
                if ((probabilityUp..probabilityDown).contains(rand))
                    position.y -= 1
                if ((probabilityDown..probabilityLeft).contains(rand))
                    position.x -= 1
                if ((probabilityLeft..probabilityRight).contains(rand))
                    position.x += 1
                if ((probabilityRight..probabilityFront).contains(rand))
                    position.z -= 1
                if ((probabilityFront..probabilityBack).contains(rand))
                    position.z += 1

                blocks[Vector3i(position)] = blockStem

                for (z in -leafRadius..leafRadius)
                    for (y in -leafRadius..leafRadius)
                        for (x in -leafRadius..leafRadius) {
                            val pos = Vector3i(position).add(x, y, z)

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > leafRadius)
                                continue

                            if (x.absoluteValue + y.absoluteValue + z.absoluteValue > 1)
                                if (random.nextFloat() >= 0.25) continue

                            if (blocks[Vector3i(pos)] != null)
                                continue

                            blocks[Vector3i(pos)] = blockLeaves
                        }
            }
        }

        return blocks
    }

}