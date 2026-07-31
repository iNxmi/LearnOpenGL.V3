package com.nami.game.client.scene

import com.nami.engine.graphics.Graphics
import com.nami.scene.Scene

class SceneManager {

    var scene: Scene? = null
        set(value) {
            field?.destroy()

            field = value
            field?.initialize()
        }

    fun render(graphics: Graphics) = scene?.render(graphics)

}