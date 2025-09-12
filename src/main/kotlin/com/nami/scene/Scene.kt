package com.nami.scene

abstract class Scene {

    open fun onEnable() {}
    open fun onDisable() {}

    open fun onUpdate() {}
    open fun onRender() {}
    open fun onRenderHUD() {}

}