package com.nami.engine.callbacks

import com.nami.engine.window.Window

interface WindowResizeCallback {

    fun onWindowResizeCallback(window: Window, width: Int, height: Int)

}