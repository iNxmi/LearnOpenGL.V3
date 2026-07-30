package com.nami.engine.callbacks

import com.nami.engine.window.Window

interface CursorPositionCallback {

    fun onCursorPositionCallback(window: Window, x: Int, y: Int)

}