package com.nami.engine.callbacks

import com.nami.engine.window.Window

interface ScrollCallback {

    fun onScrollCallback(window: Window, x: Int, y: Int)

}