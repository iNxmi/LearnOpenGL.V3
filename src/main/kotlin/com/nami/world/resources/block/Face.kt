package com.nami.world.resources.block

import org.joml.Vector2f
import org.joml.Vector3f

enum class Face(
    val normal: Vector3f,

    val triangle0: Array<Vector3f>,
    val uvs0: Array<Vector2f>,

    val triangle1: Array<Vector3f>,
    val uvs1: Array<Vector2f>
) {
    TOP(
        Vector3f(0f, 1f, 0f),

        arrayOf(
            Vector3f(0f, 1f, 0f),
            Vector3f(1f, 1f, 1f),
            Vector3f(1f, 1f, 0f)
        ),
        arrayOf(
            Vector2f(0f, 0f),
            Vector2f(1f, 1f),
            Vector2f(1f, 0f)
        ),

        arrayOf(
            Vector3f(1f, 1f, 1f),
            Vector3f(0f, 1f, 0f),
            Vector3f(0f, 1f, 1f)
        ),
        arrayOf(
            Vector2f(1f, 1f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f)
        )
    ),
    BOTTOM(
        Vector3f(0f, -1f, 0f),

        arrayOf(
            Vector3f(0f, 0f, 1f),
            Vector3f(1f, 0f, 0f),
            Vector3f(1f, 0f, 1f)
        ),
        arrayOf(
            Vector2f(0f, 1f),
            Vector2f(1f, 0f),
            Vector2f(1f, 1f)
        ),

        arrayOf(
            Vector3f(1f, 0f, 0f),
            Vector3f(0f, 0f, 1f),
            Vector3f(0f, 0f, 0f)
        ),
        arrayOf(
            Vector2f(1f, 0f),
            Vector2f(0f, 1f),
            Vector2f(0f, 0f)
        )
    ),

    NORTH(
        Vector3f(0f, 0f, 1f),

        arrayOf(
            Vector3f(0f, 1f, 0f),
            Vector3f(1f, 0f, 0f),
            Vector3f(0f, 0f, 0f)
        ),
        arrayOf(
            Vector2f(0f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f)
        ),

        arrayOf(
            Vector3f(1f, 0f, 0f),
            Vector3f(0f, 1f, 0f),
            Vector3f(1f, 1f, 0f)
        ),
        arrayOf(
            Vector2f(1f, 0f),
            Vector2f(0f, 1f),
            Vector2f(1f, 1f)
        )
    ),

    EAST(
        Vector3f(1f, 0f, 0f),

        arrayOf(
            Vector3f(1f, 0f, 1f),
            Vector3f(1f, 1f, 0f),
            Vector3f(1f, 1f, 1f)
        ),
        arrayOf(
            Vector2f(1f, 0f),
            Vector2f(0f, 1f),
            Vector2f(1f, 1f),
        ),

        arrayOf(
            Vector3f(1f, 1f, 0f),
            Vector3f(1f, 0f, 1f),
            Vector3f(1f, 0f, 0f)
        ),
        arrayOf(
            Vector2f(0f, 1f),
            Vector2f(1f, 0f),

            Vector2f(0f, 0f),
        )
    ),

    WEST(
        Vector3f(-1f, 0f, 0f),

        arrayOf(
            Vector3f(0f, 1f, 1f),
            Vector3f(0f, 0f, 0f),
            Vector3f(0f, 0f, 1f),
        ),
        arrayOf(
            Vector2f(1f, 1f),
            Vector2f(0f, 0f),
            Vector2f(1f, 0f),
        ),

        arrayOf(
            Vector3f(0f, 0f, 0f),
            Vector3f(0f, 1f, 1f),
            Vector3f(0f, 1f, 0f),
        ),
        arrayOf(
            Vector2f(0f, 0f),
            Vector2f(1f, 1f),
            Vector2f(0f, 1f),
        )
    ),

    SOUTH(
        Vector3f(0f, 0f, -1f),

        arrayOf(
            Vector3f(1f, 1f, 1f),
            Vector3f(0f, 0f, 1f),
            Vector3f(1f, 0f, 1f)
        ),
        arrayOf(
            Vector2f(1f, 1f),
            Vector2f(0f, 0f),
            Vector2f(1f, 0f)
        ),

        arrayOf(
            Vector3f(0f, 0f, 1f),
            Vector3f(1f, 1f, 1f),
            Vector3f(0f, 1f, 1f)
        ),
        arrayOf(
            Vector2f(0f, 0f),
            Vector2f(1f, 1f),
            Vector2f(0f, 1f)
        )
    );
}