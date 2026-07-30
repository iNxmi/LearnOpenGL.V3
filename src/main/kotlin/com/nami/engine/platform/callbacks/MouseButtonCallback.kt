package com.nami.engine.platform.callbacks

import com.nami.engine.platform.input.Action
import com.nami.engine.platform.input.MouseButton
import com.nami.engine.platform.window.Window

interface MouseButtonCallback {

    fun onMouseButtonCallback(window: Window, button: MouseButton, action: Action)

}