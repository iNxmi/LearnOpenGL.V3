package com.nami.world.block

import org.joml.Vector3i

enum class Face(
    val offset0: Vector3i,
    val offset1: Vector3i,
    val offset2: Vector3i,
    val offset3: Vector3i,
    val normal: Vector3i
) {

    // Y Positive
    TOP(
        offset0 = Vector3i(0, 1, 0),
        offset1 = Vector3i(0, 1, 1),
        offset2 = Vector3i(1, 1, 1),
        offset3 = Vector3i(1, 1, 0),
        normal = Vector3i(0, 1, 0)
    ),

    // Y Negative
    BOTTOM(
        offset0 = Vector3i(0, 0, 1),
        offset1 = Vector3i(0, 0, 0),
        offset2 = Vector3i(1, 0, 0),
        offset3 = Vector3i(1, 0, 1),
        normal = Vector3i(0, -1, 0)
    ),

    // Z Positive
    NORTH(
        offset0 = Vector3i(1, 0, 1),
        offset1 = Vector3i(1, 1, 1),
        offset2 = Vector3i(0, 1, 1),
        offset3 = Vector3i(0, 0, 1),
        normal = Vector3i(0, 0, 1)
    ),

    // X Positive
    EAST(
        offset0 = Vector3i(1, 0, 0),
        offset1 = Vector3i(1, 1, 0),
        offset2 = Vector3i(1, 1, 1),
        offset3 = Vector3i(1, 0, 1),
        normal = Vector3i(1, 0, 0)
    ),

    // Z Negative
    SOUTH(
        offset0 = Vector3i(0, 0, 0),
        offset1 = Vector3i(0, 1, 0),
        offset2 = Vector3i(1, 1, 0),
        offset3 = Vector3i(1, 0, 0),
        normal = Vector3i(0, 0, -1)
    ),

    // X Negative
    WEST(
        offset0 = Vector3i(0, 0, 1),
        offset1 = Vector3i(0, 1, 1),
        offset2 = Vector3i(0, 1, 0),
        offset3 = Vector3i(0, 0, 0),
        normal = Vector3i(-1, 0, 0)
    );

    companion object {
        val byNormal = entries.associateBy { it.normal }
    }

}