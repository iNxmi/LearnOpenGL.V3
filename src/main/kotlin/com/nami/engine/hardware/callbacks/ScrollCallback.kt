package com.nami.engine.hardware.callbacks

import com.nami.engine.hardware.window.Window

interface ScrollCallback {

    fun onScrollCallback(window: Window, x: Int, y: Int)

}