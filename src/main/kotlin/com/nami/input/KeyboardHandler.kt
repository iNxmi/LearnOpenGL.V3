package com.nami.input

import com.nami.input.Keyboard.Companion.keys
import com.nami.world.entity.command.CommandJump
import org.lwjgl.glfw.GLFW
import kotlin.collections.set
import org.lwjgl.glfw.GLFW.*

class KeyboardHandler {

    fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        keys[key] = if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) Keyboard.State.DOWN else Keyboard.State.UP
        val command = when(key) {
             GLFW_KEY_SPACE -> CommandJump()
        }
    }

}