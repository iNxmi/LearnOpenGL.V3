package com.nami.engine.hardware.callbacks

import com.nami.engine.hardware.input.Action
import com.nami.engine.hardware.input.MouseButton
import com.nami.engine.hardware.window.Window

interface MouseButtonCallback {

    fun onMouseButtonCallback(window: Window, button: MouseButton, action: Action)

}