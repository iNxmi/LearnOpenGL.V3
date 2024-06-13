package com.nami.resources

import java.nio.file.Path
import java.nio.file.Paths

class GamePath {

    companion object {

        //Root
        private val root: Path = Paths.get("")

        //Resources
        private val resources: Path = root.resolve("src/main/resources")

        @JvmStatic
        val shaders: Path = resources.resolve("shaders")

        @JvmStatic
        val textures: Path = resources.resolve("textures")

        @JvmStatic
        val blocks: Path = resources.resolve("blocks")

        @JvmStatic
        val biomes: Path = resources.resolve("biomes")

        @JvmStatic
        val items: Path = resources.resolve("items")

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