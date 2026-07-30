package com.nami.engine.callbacks

import com.nami.engine.input.Action
import com.nami.engine.input.MouseButton
import com.nami.engine.window.Window

interface MouseButtonCallback {

    fun onMouseButtonCallback(window: Window, button: MouseButton, action: Action)

}