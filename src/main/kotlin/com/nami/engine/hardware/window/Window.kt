package com.nami.engine.hardware.window

import com.nami.engine.hardware.callbacks.CursorPositionCallback
import com.nami.engine.hardware.callbacks.KeyCallback
import com.nami.engine.hardware.callbacks.MouseButtonCallback
import com.nami.engine.hardware.callbacks.ScrollCallback

interface Window {

    var width: Int
    var height: Int
    var title: String

    fun initialize()
    fun destroy()
    fun shouldClose(): Boolean

    fun makeContextCurrent()

    fun setKeyCallback(callback: KeyCallback)
    fun setMouseButtonCallback(callback: MouseButtonCallback)
    fun setCursorPositionCallback(callback: CursorPositionCallback)
    fun setScrollCallback(callback: ScrollCallback)

}