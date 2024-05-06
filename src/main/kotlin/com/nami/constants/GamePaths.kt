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
        val shaders: Path = resources.resolve("shaders")

        @JvmStatic
        val html: Path = resources.resolve("html")

        @JvmStatic
        val css: Path = resources.resolve("css")

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