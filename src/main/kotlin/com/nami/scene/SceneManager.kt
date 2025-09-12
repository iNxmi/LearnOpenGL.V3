package com.nami.scene

import com.nami.imgui.ImGUIManager
import com.nami.input.Keyboard
import com.nami.input.Keyboard.Companion.keys
import com.nami.input.Mouse.Companion.pos
import com.nami.input.Mouse.Companion.posDelta
import com.nami.input.Mouse.Companion.posLast
import com.nami.input.Mouse.Companion.scroll
import com.nami.input.Mouse.Companion.scrollDelta
import com.nami.input.Mouse.Companion.scrollLast
import org.lwjgl.glfw.GLFW
import kotlin.collections.set

class SceneManager {

    companion object {

        private var selected: Scene? = null

        fun set(scene: Scene) {
            selected?.disable()

            selected = scene
            selected?.enable()
        }

        fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) =
            selected?.onKeyCallback(window, key, scancode, action, mods)

        fun onCursorPosCallback(window: Long, x: Double, y: Double) =
            selected?.onCursorPosCallback(window, x, y)

        fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) =
            selected?.onMouseButtonCallback(window, button, action, mods)

        fun onScrollCallback(window: Long, x: Double, y: Double) =
            selected?.onScrollCallback(window, x, y)

        fun update() = selected?.update()
        fun render() = selected?.render()
        fun renderHUD() {
            ImGUIManager.newFrame()
            selected?.renderHUD()
            ImGUIManager.render()
        }

    }

}