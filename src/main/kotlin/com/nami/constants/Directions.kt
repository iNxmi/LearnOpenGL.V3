package com.nami.constants

import org.joml.Vector3f

class Directions {

    companion object {
        val UP = Vector3f(0f, 1f, 0f)
            get() = Vector3f(field)
        val DOWN = Vector3f(0f, -1f, 0f)
            get() = Vector3f(field)

        val LEFT = Vector3f(-1f, 0f, 0f)
            get() = Vector3f(field)
        val RIGHT = Vector3f(1f, 0f, 0f)
            get() = Vector3f(field)

        val FRONT = Vector3f(0f, 0f, -1f)
            get() = Vector3f(field)
        val BACK = Vector3f(0f, 0f, 1f)
            get() = Vector3f(field)
    }

}