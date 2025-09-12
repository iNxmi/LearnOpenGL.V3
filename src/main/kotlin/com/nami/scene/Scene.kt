package com.nami.scene

abstract class Scene {

    open fun onEnable() {}
    open fun onDisable() {}

    open fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {}
    open fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {}
    open fun onCursorPosCallback(window: Long, x: Double, y: Double) {}
    open fun onScrollCallback(window: Long, x: Double, y: Double) {}

    open fun onUpdate() {}
    open fun onRender() {}
    open fun onRenderHUD() {}

}