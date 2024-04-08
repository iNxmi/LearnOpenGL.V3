package com.nami.constants

import java.nio.file.Path
import java.nio.file.Paths

class GamePaths {

    companion object {

        //Root
        @JvmStatic
        val root: Path = Paths.get("")

        //Resources
        @JvmStatic
        val resources: Path = root.resolve("src/main/resources")

        @JvmStatic
        val fonts: Path = resources.resolve("fonts")

        @JvmStatic
        val shaders: Path = resources.resolve("shaders")

        @JvmStatic
        val textures: Path = resources.resolve("textures")

        @JvmStatic
        val models: Path = resources.resolve("models")

        @JvmStatic
        val meshes: Path = resources.resolve("meshes")


        //Data
        @JvmStatic
        val data: Path = root.resolve("data")

        @JvmStatic
        val screenshots: Path = data.resolve("screenshots")

        @JvmStatic
        val logs: Path = data.resolve("logs")

        @JvmStatic
        val saves: Path = data.resolve("saves")

    }

}