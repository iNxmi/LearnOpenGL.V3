package com.nami.scene

import com.nami.Game

class SceneTime {

    var time = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update() {
        delta = Game.DELTA_TIME * scale
        time += delta
    }

}