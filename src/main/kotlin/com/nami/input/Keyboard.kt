package com.nami.input

import org.lwjgl.glfw.GLFW

class Keyboard {

    companion object {

        val keys = mutableMapOf<Int, State>()
        val keysLast = mutableMapOf<Int, State>()

        init {
            for (i in 0 until GLFW.GLFW_KEY_LAST) {
                keys[i] = State.UP
                keysLast[i] = State.UP
            }
        }

        fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            keys[key] = if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) State.DOWN else State.UP
        }

        fun update() {
            for (i in 0 until GLFW.GLFW_KEY_LAST) {
                if (keys[i] == keysLast[i] && keysLast[i] == State.DOWN)
                    keys[i] = State.HOLD

                keysLast[i] = keys[i]!!
            }
        }

        fun isKeyInStates(key: Int, state: State, vararg states: State): Boolean {
            val keyState = keys[key]

            if (keyState == state)
                return true

            states.forEach { s ->
                if (s == keyState)
                    return true
            }

            return false
        }

    }

    enum class State {
        UP,
        DOWN,
        HOLD
    }

}

