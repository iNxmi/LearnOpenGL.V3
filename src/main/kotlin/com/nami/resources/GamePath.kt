package com.nami.resources

import java.nio.file.Path
import java.nio.file.Paths

class GamePath {

    companion object {
        //Root
        private val root: Path = Paths.get("")

        //Resources
        private val resources: Path = root.resolve("src/main/resources")
        val shader: Path = resources.resolve("shader")
        val texture: Path = resources.resolve("texture")
        val block: Path = resources.resolve("block")
        val biome: Path = resources.resolve("biome")
        val item: Path = resources.resolve("item")
        val model: Path = resources.resolve("model")
        val particle: Path = resources.resolve("particle")
        val recipe: Path = resources.resolve("recipe")
        val feature: Path = resources.resolve("feature")
        val language: Path = resources.resolve("language")
        val creature: Path = resources.resolve("creature")

        //Data
        private val data: Path = root.resolve("data")
        val screenshots: Path = data.resolve("screenshots")
        val maps: Path = data.resolve("maps")
        val worlds: Path = data.resolve("worlds")
    }

}