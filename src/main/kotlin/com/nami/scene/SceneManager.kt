package com.nami.scene

class SceneManager {

    companion object {
        @JvmStatic
        var selected: Scene? = null

        @JvmStatic
        fun update() {
            selected?.update()
        }

        @JvmStatic
        fun render() {
            selected?.render()
        }

        @JvmStatic
        fun renderNVG() {
            selected?.renderNVG()
        }

        @JvmStatic
        fun renderNK() {
            selected?.renderNK()
        }

    }

}