package com.nami.legacy.scene

import com.nami.scene.Scene

object SceneManager {

    var scene: Scene? = null
        set(value) {
            field?.onDisable()

            field = value
            field?.onEnable()
        }

    fun update() = scene?.onUpdate()
    fun render() = scene?.onRender()

}