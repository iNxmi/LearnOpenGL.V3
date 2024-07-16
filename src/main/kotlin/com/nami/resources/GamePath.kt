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
        val shader: Path = resources.resolve("shader")

        @JvmStatic
        val texture: Path = resources.resolve("texture")

        @JvmStatic
        val block: Path = resources.resolve("block")

        @JvmStatic
        val biome: Path = resources.resolve("biome")

        @JvmStatic
        val item: Path = resources.resolve("item")

        @JvmStatic
        val model: Path = resources.resolve("model")

        @JvmStatic
        val particle: Path = resources.resolve("particle")

        @JvmStatic
        val recipe: Path = resources.resolve("recipe")

        @JvmStatic
        val feature: Path = resources.resolve("feature")

        @JvmStatic
        val language: Path = resources.resolve("language")

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