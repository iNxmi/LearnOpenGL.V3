package com.nami.legacy.scene

import com.nami.scene.Scene

object SceneManager {

    private var selected: Scene? = null

    fun set(scene: Scene) {
        selected?.onDisable()

        selected = scene
        selected?.onEnable()
    }

    fun update() = selected?.onUpdate()
    fun render() = selected?.onRender()

}