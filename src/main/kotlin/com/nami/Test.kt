package com.nami

import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

fun main() {

    val elevation = JNoise.newBuilder()
        .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(System.currentTimeMillis()).build())
        .scale(0.01)
        .clamp(0.0, 1.0)
        .build()

    val moisture = JNoise.newBuilder()
        .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(System.currentTimeMillis() + 1).build())
        .scale(0.01)
        .clamp(0.0, 1.0)
        .build()

    val temperature = JNoise.newBuilder()
        .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(System.currentTimeMillis() + 2).build())
        .scale(0.01)
        .clamp(0.0, 1.0)
        .build()

    val image = BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB)
    for (y in 0 until 1024)
        for (x in 0 until 1024) {
            val e = (elevation.evaluateNoise(x.toDouble(), y.toDouble()) * 255).roundToInt()
            val m = (moisture.evaluateNoise(x.toDouble(), y.toDouble()) * 255).roundToInt()
            val t = (temperature.evaluateNoise(x.toDouble(), y.toDouble()) * 255).roundToInt()

            println("$e $m $t")

            image.setRGB(x, y, Color(e, m, t).rgb)
        }

    ImageIO.write(image, "png", File("E:/Windows/Desktop/emt.png"))
}