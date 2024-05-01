package com.nami.constants

import java.nio.file.Path
import java.nio.file.Paths

class GamePaths {

    companion object {

        //Root
        private val root: Path = Paths.get("")

        //Resources
        private val resources: Path = root.resolve("src/main/resources")

        @JvmStatic
        val fonts: Path = resources.resolve("fonts")

        @JvmStatic
        val shaders: Path = resources.resolve("shaders")

        //Data
        private val data: Path = root.resolve("data")

        @JvmStatic
        val screenshots: Path = data.resolve("screenshots")

        @JvmStatic
        val maps: Path = data.resolve("maps")

        @JvmStatic
        val worlds: Path = data.resolve("worlds")

    }

}