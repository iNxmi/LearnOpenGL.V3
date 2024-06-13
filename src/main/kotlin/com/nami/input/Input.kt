package com.nami.input

import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

class Input {

    companion object {

        private val keyStatesLast = mutableMapOf<Int, State>()
        private val mouseButtonStatesLast = mutableMapOf<Int, State>()
        private val posLast = Vector2f()
        private val scrollLast = Vector2f()

        @JvmStatic
        val keyStates = mutableMapOf<Int, State>()

        @JvmStatic
        val mouseButtonStates = mutableMapOf<Int, State>()

        @JvmStatic
        val pos = Vector2f()

        @JvmStatic
        val posDelta = Vector2f()

        @JvmStatic
        val scroll = Vector2f()

        @JvmStatic
        val scrollDelta = Vector2f()

        init {
            for (i in 0 until GLFW.GLFW_KEY_LAST) {
                keyStates[i] = State.UP
                keyStatesLast[i] = State.UP
            }
            for (i in 0 until GLFW.GLFW_MOUSE_BUTTON_LAST) {
                mouseButtonStates[i] = State.UP
                mouseButtonStatesLast[i] = State.UP
            }
        }

        @JvmStatic
        fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            keyStates[key] = if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) State.DOWN else State.UP
        }

        @JvmStatic
        fun onCursorPosCallback(window: Long, x: Double, y: Double) {
            posLast.set(pos)
            pos.set(x, y)

            posDelta.set(pos.x - posLast.x, pos.y - posLast.y)
        }

        @JvmStatic
        fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
            mouseButtonStates[button] = if (action == GLFW.GLFW_PRESS) State.DOWN else State.UP
        }

        @JvmStatic
        fun onScrollCallback(window: Long, x: Double, y: Double) {
            scrollLast.set(scroll)
            scroll.add(x.toFloat(), y.toFloat())

            scrollDelta.set(scroll.x - scrollLast.x, scroll.y - scrollLast.y)
        }

        @JvmStatic
        fun update() {
            //Update Key Input
            for (i in 0 until GLFW.GLFW_KEY_LAST) {
                if (keyStates[i] == keyStatesLast[i] && keyStatesLast[i] == State.DOWN)
                    keyStates[i] = State.HOLD

                keyStatesLast[i] = keyStates[i]!!
            }

            //Update Mouse Button Input
            for (i in 0 until GLFW.GLFW_MOUSE_BUTTON_LAST) {
                if (mouseButtonStates[i] == mouseButtonStatesLast[i] && mouseButtonStatesLast[i] == State.DOWN)
                    mouseButtonStates[i] = State.HOLD

                mouseButtonStatesLast[i] = mouseButtonStates[i]!!
            }
        }

        @JvmStatic
        fun endFrame() {
            posDelta.zero()
            scrollDelta.zero()
        }

        @JvmStatic
        fun isKeyInStates(key: Int, state: State, vararg states: State): Boolean {
            val keyState = keyStates[key]

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

