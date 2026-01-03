plugins {
    kotlin("jvm") version "2.2.21"
    application
}

group = "com.nami"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val lwjglVersion = "3.3.6"
val lwjglNatives = "natives-windows"
val imguiVersion = "1.86.11"
val jomlVersion = "1.10.5"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("me.friwi:jcefmaven:141.0.10")

    implementation("commons-io:commons-io:2.16.1")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation("com.google.guava:guava-collections:r03")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("dev.romainguy:kotlin-math:1.6.0")
    implementation("org.danilopianini:khttp:1.3.1")
    implementation("de.articdive:jnoise-pipeline:4.1.0")
    implementation("de.javagl:obj:0.4.0")

    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-windows:$imguiVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("org.joml:joml:$jomlVersion")

    // LWJGL dependencies
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-cuda")
    implementation("org.lwjgl", "lwjgl-egl")
    implementation("org.lwjgl", "lwjgl-fmod")
    implementation("org.lwjgl", "lwjgl-freetype")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-harfbuzz")
    implementation("org.lwjgl", "lwjgl-hwloc")
    implementation("org.lwjgl", "lwjgl-jawt")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-ktx")
    implementation("org.lwjgl", "lwjgl-libdivide")
    implementation("org.lwjgl", "lwjgl-llvm")
    implementation("org.lwjgl", "lwjgl-lmdb")
    implementation("org.lwjgl", "lwjgl-lz4")
    implementation("org.lwjgl", "lwjgl-meow")
    implementation("org.lwjgl", "lwjgl-meshoptimizer")
    implementation("org.lwjgl", "lwjgl-msdfgen")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-odbc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opencl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-opengles")
    implementation("org.lwjgl", "lwjgl-openvr")
    implementation("org.lwjgl", "lwjgl-openxr")
    implementation("org.lwjgl", "lwjgl-opus")
    implementation("org.lwjgl", "lwjgl-ovr")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-remotery")
    implementation("org.lwjgl", "lwjgl-rpmalloc")
    implementation("org.lwjgl", "lwjgl-shaderc")
    implementation("org.lwjgl", "lwjgl-spvc")
    implementation("org.lwjgl", "lwjgl-sse")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyexr")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    implementation("org.lwjgl", "lwjgl-tootle")
    implementation("org.lwjgl", "lwjgl-vma")
    implementation("org.lwjgl", "lwjgl-vulkan")
    implementation("org.lwjgl", "lwjgl-xxhash")
    implementation("org.lwjgl", "lwjgl-yoga")
    implementation("org.lwjgl", "lwjgl-zstd")
    implementation("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-freetype", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-harfbuzz", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-hwloc", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-ktx", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-libdivide", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-llvm", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-lmdb", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-lz4", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-meow", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-msdfgen", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengles", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-openvr", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-openxr", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opus", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-ovr", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-remotery", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-rpmalloc", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-shaderc", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-spvc", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-sse", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-tinyexr", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-tinyfd", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-tootle", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-vma", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-xxhash", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-yoga", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-zstd", classifier = lwjglNatives)
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
