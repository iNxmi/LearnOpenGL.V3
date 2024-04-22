package com.nami.scene

abstract class Scene {

    protected val time = SceneTime()

    protected abstract fun onInit()

    fun init() {
        onInit()
    }

    fun update() {
        time.update()
        onUpdate()
    }

    protected abstract fun onUpdate()

    fun render() {
        onRender()
    }

    protected abstract fun onRender()
    fun renderHUD() {
        onRenderHUD()
    }

    protected abstract fun onRenderHUD()

}