package com.nami.legacy

import com.nami.engine.callbacks.CursorPositionCallback
import com.nami.engine.callbacks.KeyCallback
import com.nami.engine.callbacks.MouseButtonCallback
import com.nami.engine.callbacks.ScrollCallback
import com.nami.engine.input.Action
import com.nami.engine.input.Key
import com.nami.engine.input.MouseButton
import com.nami.engine.window.Window
import org.joml.Vector2i
import java.util.*

object Input : KeyCallback, MouseButtonCallback, ScrollCallback, CursorPositionCallback {


    private val keys = BooleanArray(Key.KEY_LAST.glfwCode)
    private val keysPressed = BooleanArray(Key.KEY_LAST.glfwCode)
    private val keysReleased = BooleanArray(Key.KEY_LAST.glfwCode)

    override fun onKeyCallback(
        window: Window,
        key: Key,
        action: Action
    ) = when (action) {
        Action.PRESS -> {
            keys[key.ordinal] = true
            keysPressed[key.ordinal] = true
        }

        Action.RELEASE -> {
            keys[key.ordinal] = false
            keysReleased[key.ordinal] = true
            keysReleased[key.ordinal] = true
        }

        else -> {}
    }

    private val buttons = BooleanArray(MouseButton.BUTTON_LAST.glfwCode)
    private val buttonsPressed = BooleanArray(MouseButton.BUTTON_LAST.glfwCode)
    private val buttonsReleased = BooleanArray(MouseButton.BUTTON_LAST.glfwCode)

    override fun onMouseButtonCallback(
        window: Window,
        button: MouseButton,
        action: Action
    ) = when (action) {
        Action.PRESS -> {
            buttons[button.ordinal] = true
            buttonsPressed[button.ordinal] = true
        }

        Action.RELEASE -> {
            buttons[button.ordinal] = false
            buttonsReleased[button.ordinal] = true
        }

        else -> {}
    }

    private val position = Vector2i()
    override fun onCursorPositionCallback(window: Window, x: Int, y: Int) {
        position.x = x
        position.y = y
    }

    private val scroll = Vector2i()
    override fun onScrollCallback(window: Window, x: Int, y: Int) {
        scroll.x = x
        scroll.y = y
    }

    fun endFrame() {
        Arrays.fill(keysPressed, false)
        Arrays.fill(keysReleased, false)
        Arrays.fill(buttonsPressed, false)
        Arrays.fill(buttonsReleased, false)
        scroll.set(0)
    }

    fun isKeyDown(key: Key) = keys[key.ordinal]
    fun isKeyPressed(key: Key) = keysPressed[key.ordinal]
    fun isKeyReleased(key: Key) = keysReleased[key.ordinal]

    fun isMouseDown(button: MouseButton) = buttons[button.ordinal]
    fun isMousePressed(button: MouseButton) = buttonsPressed[button.ordinal]
    fun isMouseReleased(button: MouseButton) = buttonsReleased[button.ordinal]

    fun position() = Vector2i(position)
    fun scroll() = Vector2i(scroll)

}