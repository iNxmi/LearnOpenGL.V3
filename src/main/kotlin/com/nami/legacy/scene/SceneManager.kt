package com.nami.scene

import com.nami.imgui.ImGUIManager

object SceneManager {

    private var selected: Scene? = null

    fun set(scene: Scene) {
        selected?.onDisable()

        selected = scene
        selected?.onEnable()
    }

    fun update() = selected?.onUpdate()
    fun render() = selected?.onRender()
    fun renderHUD() {
        ImGUIManager.newFrame()
        selected?.onRenderHUD()
        ImGUIManager.render()
    }

}