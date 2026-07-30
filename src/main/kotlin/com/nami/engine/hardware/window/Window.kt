package com.nami.engine.hardware.window

import com.nami.engine.hardware.callbacks.CursorPositionCallback
import com.nami.engine.hardware.callbacks.KeyCallback
import com.nami.engine.hardware.callbacks.MouseButtonCallback
import com.nami.engine.hardware.callbacks.ScrollCallback
import org.joml.Vector2i

interface Window {

    var size: Size
    var title: String
    var shouldClose: Boolean
    var isVisible: Boolean
    val isRawMouseMotionSupported: Boolean

    fun initialize(width: Int, height: Int, title: String)

    fun update()

    fun destroy()

    fun makeContextCurrent()

    fun setKeyCallback(callback: KeyCallback)
    fun setMouseButtonCallback(callback: MouseButtonCallback)
    fun setCursorPositionCallback(callback: CursorPositionCallback)
    fun setScrollCallback(callback: ScrollCallback)

}