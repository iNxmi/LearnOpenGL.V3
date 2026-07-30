package com.nami.engine.callbacks

import com.nami.engine.input.Action
import com.nami.engine.input.Key
import com.nami.engine.window.Window

interface KeyCallback {

    fun onKeyCallback(window: Window, key: Key, action: Action)

}