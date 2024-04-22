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

        @JvmStatic
        val models: Path = resources.resolve("models")

        @JvmStatic
        val meshes: Path = resources.resolve("meshes")

        @JvmStatic
        val sounds: Path = resources.resolve("sounds")

        @JvmStatic
        val textures: Path = resources.resolve("textures")

        @JvmStatic
        val blocks: Path = resources.resolve("blocks")

        //Data
        private val data: Path = root.resolve("data")

        @JvmStatic
        val screenshots: Path = data.resolve("screenshots")

        @JvmStatic
        val logs: Path = data.resolve("logs")

        @JvmStatic
        val saves: Path = data.resolve("saves")

    }

}