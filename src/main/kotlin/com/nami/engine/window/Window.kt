package com.nami.engine.window

import com.nami.engine.callbacks.CursorPositionCallback
import com.nami.engine.callbacks.KeyCallback
import com.nami.engine.callbacks.MouseButtonCallback
import com.nami.engine.callbacks.ScrollCallback
import com.nami.engine.callbacks.WindowResizeCallback
import com.nami.engine.input.CursorMode

interface Window {

    var size: Size
    var title: String

    var shouldClose: Boolean

    var isVisible: Boolean
    var isResizable: Boolean
    val isRawMouseMotionSupported: Boolean
    var isRawMouseMotionEnabled: Boolean
    var cursorMode : CursorMode

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