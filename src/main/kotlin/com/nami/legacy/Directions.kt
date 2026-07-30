package com.nami

import org.joml.Vector3f

enum class Directions(val vector: Vector3f) {

    UP(Vector3f(0f, 1f, 0f)),
    DOWN(Vector3f(0f, -1f, 0f)),
    LEFT(Vector3f(-1f, 0f, 0f)),
    RIGHT(Vector3f(1f, 0f, 0f)),
    FRONT(Vector3f(0f, 0f, -1f)),
    BACK(Vector3f(0f, 0f, 1f));

}