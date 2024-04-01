package com.nami.constants

import java.nio.file.Path
import java.nio.file.Paths

class GamePaths {

    companion object {

        @JvmStatic
        val resources: Path = Paths.get("src", "main", "resources")

        @JvmStatic
        val fonts: Path = resources.resolve("fonts")

        @JvmStatic
        val shaders: Path = resources.resolve("shaders")

        @JvmStatic
        val textures: Path = resources.resolve("textures")

    }

}