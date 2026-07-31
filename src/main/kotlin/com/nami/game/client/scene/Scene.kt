package com.nami.scene

import com.nami.engine.graphics.Graphics

interface Scene {

     fun initialize() {}
     fun render(graphics: Graphics) {}
     fun destroy() {}

}