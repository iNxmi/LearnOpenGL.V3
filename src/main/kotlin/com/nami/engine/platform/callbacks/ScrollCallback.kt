package com.nami.engine.platform.callbacks

import com.nami.engine.platform.window.Window

interface ScrollCallback {

    fun onScrollCallback(window: Window, x: Int, y: Int)

}