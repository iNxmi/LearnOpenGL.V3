package com.nami.engine.platform.callbacks

import com.nami.engine.platform.window.Window

interface WindowResizeCallback {

    fun onWindowResizeCallback(window: Window, width: Int, height: Int)

}