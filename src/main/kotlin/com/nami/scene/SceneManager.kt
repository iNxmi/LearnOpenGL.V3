package com.nami.scene

import com.nami.imgui.ImGUIManager

class SceneManager {

    companion object {

        private var selected: Scene? = null

        fun set(scene: Scene) {
            selected?.onDisable()

            selected = scene
            selected?.onEnable()
        }

        fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) =
            selected?.onKeyCallback(window, key, scancode, action, mods)

        fun onCursorPosCallback(window: Long, x: Double, y: Double) =
            selected?.onCursorPosCallback(window, x, y)

        fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) =
            selected?.onMouseButtonCallback(window, button, action, mods)

        fun onScrollCallback(window: Long, x: Double, y: Double) =
            selected?.onScrollCallback(window, x, y)

        fun update() = selected?.onUpdate()
        fun render() = selected?.onRender()
        fun renderHUD() {
            ImGUIManager.newFrame()
            selected?.onRenderHUD()
            ImGUIManager.render()
        }

    }

}