package com.nami.engine.platform.window

import com.nami.engine.platform.callbacks.CursorPositionCallback
import com.nami.engine.platform.callbacks.KeyCallback
import com.nami.engine.platform.callbacks.MouseButtonCallback
import com.nami.engine.platform.callbacks.ScrollCallback
import com.nami.engine.platform.callbacks.WindowResizeCallback
import com.nami.engine.platform.input.CursorMode

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