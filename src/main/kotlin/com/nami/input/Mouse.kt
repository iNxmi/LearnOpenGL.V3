package com.nami.input

import org.joml.Vector2f
import org.lwjgl.glfw.GLFW

class Mouse private constructor() {

    companion object {

        val buttons = mutableMapOf<Int, State>()
        val buttonsLast = mutableMapOf<Int, State>()

        val pos = Vector2f()
        val posLast = Vector2f()
        val posDelta = Vector2f()

        val scroll = Vector2f()
        val scrollLast = Vector2f()
        val scrollDelta = Vector2f()

        init {
            for (i in 0 until GLFW.GLFW_MOUSE_BUTTON_LAST) {
                buttons[i] = State.UP
                buttonsLast[i] = State.UP
            }
        }

        fun onCursorPosCallback(window: Long, x: Double, y: Double) {
            posLast.set(pos)
            pos.set(x, y)

            posDelta.set(pos.x - posLast.x, pos.y - posLast.y)
        }

        fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
            buttons[button] = if (action == GLFW.GLFW_PRESS) State.DOWN else State.UP
        }

        fun onScrollCallback(window: Long, x: Double, y: Double) {
            scrollLast.set(scroll)
            scroll.add(x.toFloat(), y.toFloat())

            scrollDelta.set(scroll.x - scrollLast.x, scroll.y - scrollLast.y)
        }

        fun update() {
            for (i in 0 until GLFW.GLFW_MOUSE_BUTTON_LAST) {
                if (buttons[i] == buttonsLast[i] && buttonsLast[i] == State.DOWN)
                    buttons[i] = State.HOLD

                buttonsLast[i] = buttons[i]!!
            }
        }

        fun endFrame() {
            posDelta.zero()
            scrollDelta.zero()
        }

    }

    enum class State {
        UP,
        DOWN,
        HOLD
    }

}