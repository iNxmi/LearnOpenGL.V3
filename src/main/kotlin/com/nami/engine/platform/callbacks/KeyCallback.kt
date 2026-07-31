package com.nami.engine.platform.callbacks

import com.nami.engine.platform.input.Action
import com.nami.engine.platform.input.Key
import com.nami.engine.platform.window.Window

interface KeyCallback {

    fun onKeyCallback(window: Window, key: Key, action: Action)

}