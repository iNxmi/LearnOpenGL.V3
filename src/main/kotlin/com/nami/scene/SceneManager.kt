package com.nami.scene

import com.nami.imgui.ImGUIManager

class SceneManager {

    companion object {
        @JvmStatic
        var selected: Scene? = null
            set(value) {
                field = value
                value?.init()
            }

        @JvmStatic
        fun update() {
            selected?.update()
        }

        @JvmStatic
        fun render() {
            selected?.render()
        }

        @JvmStatic
        fun renderHUD() {
            ImGUIManager.newFrame()
            selected?.renderHUD()
            ImGUIManager.render()
        }

    }

}