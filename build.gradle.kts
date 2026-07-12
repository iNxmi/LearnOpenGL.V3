import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("jvm") version "2.1.0"
}

group = "com.nami"
version = "1.0-SNAPSHOT"
description = "LearnOpenGL.V3"
java.sourceCompatibility = JavaVersion.VERSION_21

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }     ->
            "natives-macos"
        arrayOf("Windows").any { name.startsWith(it) }                ->
            "natives-windows"
        else                                                                            ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:3.4.1"))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl::$lwjglNatives")

    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-glfw::$lwjglNatives")

    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-openal::$lwjglNatives")

    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-opengl::$lwjglNatives")

    implementation("org.lwjgl:lwjgl-yoga")
    implementation("org.lwjgl:lwjgl-yoga::$lwjglNatives")

    implementation("org.joml:joml:1.10.9")
    implementation("org.joml:joml-primitives:1.10.0")




    implementation("commons-io:commons-io:2.16.1")

    implementation("com.google.guava:guava:33.2.1-jre")

    implementation("com.google.guava:guava-collections:r03")

    implementation("net.objecthunter:exp4j:0.4.8")

    implementation("de.articdive:jnoise-pipeline:4.1.0")

    implementation("de.javagl:obj:0.4.0")

    implementation("io.github.spair:imgui-java-binding:1.86.11")
    implementation("io.github.spair:imgui-java-lwjgl3:1.86.11")
    implementation("io.github.spair:imgui-java-natives-windows:1.86.11")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
}