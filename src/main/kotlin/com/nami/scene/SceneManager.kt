package com.nami.scene

import com.nami.imgui.ImGUIManager

class SceneManager {

    companion object {

        private var selected: Scene? = null

        fun set(scene: Scene) {
            selected?.disable()

            selected = scene
            selected?.enable()
        }

        fun update() {
            selected?.update()
        }

        fun render() {
            selected?.render()
        }

        fun renderHUD() {
            ImGUIManager.newFrame()
            selected?.renderHUD()
            ImGUIManager.render()
        }

    }

}