package com.nami.world.block

import org.joml.Vector3i

object AmbientOcclusion {

    data class AOOffsets(
        val side1: Vector3i,
        val side2: Vector3i,
        val corner: Vector3i
    )

    fun getTable(face: Face): Array<AOOffsets> = when (face) {
        Face.TOP -> AO_TOP
        Face.BOTTOM -> AO_BOTTOM
        Face.NORTH -> AO_NORTH
        Face.SOUTH -> AO_SOUTH
        Face.EAST -> AO_EAST
        Face.WEST -> AO_WEST
    }

    val AO_TOP = arrayOf(
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 0, -1), Vector3i(-1, 0, -1)),
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 0, 1), Vector3i(-1, 0, 1)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 0, 1), Vector3i(1, 0, 1)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 0, -1), Vector3i(1, 0, -1))
    )

    val AO_BOTTOM = arrayOf(
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 0, -1), Vector3i(-1, 0, -1)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 0, -1), Vector3i(1, 0, -1)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 0, 1), Vector3i(1, 0, 1)),
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 0, 1), Vector3i(-1, 0, 1))
    )

    val AO_NORTH = arrayOf(
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, -1, 0), Vector3i(1, -1, 0)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 1, 0), Vector3i(1, 1, 0)),
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 1, 0), Vector3i(-1, 1, 0)),
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, -1, 0), Vector3i(-1, -1, 0))
    )

    val AO_SOUTH = arrayOf(
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, -1, 0), Vector3i(-1, -1, 0)),
        AOOffsets(Vector3i(-1, 0, 0), Vector3i(0, 1, 0), Vector3i(-1, 1, 0)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, 1, 0), Vector3i(1, 1, 0)),
        AOOffsets(Vector3i(1, 0, 0), Vector3i(0, -1, 0), Vector3i(1, -1, 0))
    )

    val AO_EAST = arrayOf(
        AOOffsets(Vector3i(0, 0, -1), Vector3i(0, -1, 0), Vector3i(0, -1, -1)),
        AOOffsets(Vector3i(0, 0, -1), Vector3i(0, 1, 0), Vector3i(0, 1, -1)),
        AOOffsets(Vector3i(0, 0, 1), Vector3i(0, 1, 0), Vector3i(0, 1, 1)),
        AOOffsets(Vector3i(0, 0, 1), Vector3i(0, -1, 0), Vector3i(0, -1, 1))
    )

    val AO_WEST = arrayOf(
        AOOffsets(Vector3i(0, 0, 1), Vector3i(0, -1, 0), Vector3i(0, -1, 1)),
        AOOffsets(Vector3i(0, 0, 1), Vector3i(0, 1, 0), Vector3i(0, 1, 1)),
        AOOffsets(Vector3i(0, 0, -1), Vector3i(0, 1, 0), Vector3i(0, 1, -1)),
        AOOffsets(Vector3i(0, 0, -1), Vector3i(0, -1, 0), Vector3i(0, -1, -1))
    )

}