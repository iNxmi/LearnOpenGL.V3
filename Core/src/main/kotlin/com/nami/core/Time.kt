package com.nami.core

class Time {

    var time = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update(delta: Float) {
        this.delta = delta * scale
        time += this.delta
    }

}