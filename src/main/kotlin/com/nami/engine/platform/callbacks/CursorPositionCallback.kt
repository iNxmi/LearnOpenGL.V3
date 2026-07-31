package com.nami.engine.platform.callbacks

import com.nami.engine.platform.window.Window

interface CursorPositionCallback {

    fun onCursorPositionCallback(window: Window, x: Int, y: Int)

}