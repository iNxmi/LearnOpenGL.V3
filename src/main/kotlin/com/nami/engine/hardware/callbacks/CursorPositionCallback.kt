package com.nami.engine.hardware.callbacks

import com.nami.engine.hardware.window.Window

interface CursorPositionCallback {

    fun onCursorPositionCallback(window: Window, x: Int, y: Int)

}