package com.nami.engine.platform.window

import com.nami.engine.platform.callbacks.CursorPositionCallback
import com.nami.engine.platform.callbacks.KeyCallback
import com.nami.engine.platform.callbacks.MouseButtonCallback
import com.nami.engine.platform.callbacks.ScrollCallback

interface Window {

    var size: Size
    var title: String
    var shouldClose: Boolean
    var isVisible: Boolean
    val isRawMouseMotionSupported: Boolean

    fun initialize(width: Int, height: Int, title: String)

    fun poll()
    fun swap()

    fun destroy()

    fun makeContextCurrent()

    fun setKeyCallback(callback: KeyCallback)
    fun setMouseButtonCallback(callback: MouseButtonCallback)
    fun setCursorPositionCallback(callback: CursorPositionCallback)
    fun setScrollCallback(callback: ScrollCallback)

}