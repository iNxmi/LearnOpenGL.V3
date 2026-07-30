package com.nami.engine.window

import com.nami.engine.callbacks.CursorPositionCallback
import com.nami.engine.callbacks.KeyCallback
import com.nami.engine.callbacks.MouseButtonCallback
import com.nami.engine.callbacks.ScrollCallback
import com.nami.engine.callbacks.WindowResizeCallback

interface Window {

    var size: Size
    var title: String
    var shouldClose: Boolean
    var isVisible: Boolean
    val isRawMouseMotionSupported: Boolean

    fun initialize()

    fun poll()
    fun swap()

    fun destroy()

    fun makeContextCurrent()

    fun setKeyCallback(callback: KeyCallback)
    fun setMouseButtonCallback(callback: MouseButtonCallback)
    fun setCursorPositionCallback(callback: CursorPositionCallback)
    fun setScrollCallback(callback: ScrollCallback)
    fun setWindowResizeCallback(callback: WindowResizeCallback)

}