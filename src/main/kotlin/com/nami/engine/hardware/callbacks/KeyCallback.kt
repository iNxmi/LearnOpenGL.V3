package com.nami.engine.hardware.callbacks

import com.nami.engine.hardware.input.Action
import com.nami.engine.hardware.input.Key
import com.nami.engine.hardware.window.Window

interface KeyCallback {

    fun onKeyCallback(window: Window, key: Key, action: Action)

}