package com.nami

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import java.util.*

class Input {

    companion object {

        private val keys = BooleanArray(GLFW.GLFW_KEY_LAST)
        private val keysPressed = BooleanArray(GLFW.GLFW_KEY_LAST)
        private val keysReleased = BooleanArray(GLFW.GLFW_KEY_LAST)

        fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (key < 0)
                return

            if (action == GLFW.GLFW_PRESS) {
                keys[key] = true
                keysPressed[key] = true
            } else if (action == GLFW.GLFW_RELEASE) {
                keys[key] = false
                keysReleased[key] = true
                keysReleased[key] = true
            }
        }

        private val buttons = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
        private val buttonsPressed = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
        private val buttonsReleased = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)

        fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
            if (button < 0)
                return

            if (action == GLFW_PRESS) {
                buttons[button] = true
                buttonsPressed[button] = true
            } else if (action == GLFW_RELEASE) {
                buttons[button] = false
                buttonsReleased[button] = true
            }
        }

        private val position = Vector2i()
        fun onCursorPosCallback(window: Long, x: Double, y: Double) {
            position.x = x.toInt()
            position.y = y.toInt()
        }

        private val scroll = Vector2i()
        fun onScrollCallback(window: Long, x: Double, y: Double) {
            scroll.x = x.toInt()
            scroll.y = y.toInt()
        }

        fun endFrame() {
            Arrays.fill(keysPressed, false)
            Arrays.fill(keysReleased, false)
            Arrays.fill(buttonsPressed, false)
            Arrays.fill(buttonsReleased, false)
            scroll.set(0)
        }

        fun isKeyDown(key: Int) = keys[key]
        fun isKeyPressed(key: Int) = keysPressed[key]
        fun isKeyReleased(key: Int) = keysReleased[key]

        fun isMouseDown(button: Int) = buttons[button]
        fun isMousePressed(button: Int) = buttonsPressed[button]
        fun isMouseReleased(button: Int) = buttonsReleased[button]

        fun position() = Vector2i(position)
        fun scroll() = Vector2i(scroll)
    }
}