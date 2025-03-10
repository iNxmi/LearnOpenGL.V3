package com.nami.client.scene.scenes

import com.nami.client.Window
import com.nami.client.resources.GamePath
import com.nami.client.scene.Scene
import com.nami.client.scene.SceneManager
import com.nami.client.world.World
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import imgui.type.ImString
import org.apache.commons.io.FileUtils
import org.joml.Vector3i
import java.nio.file.Files
import kotlin.io.path.name

class SelectWorldScene : Scene {


    override fun enable() {

    }

    override fun update() {

    }

    override fun render() {

    }

    val state = ImInt()
    val seed = ImString(System.currentTimeMillis().toString())
    val name = ImString()
    override fun renderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())

        ImGui.begin(
            "Select World",
            ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove or ImGuiWindowFlags.NoBackground
        )

        val names = mutableListOf<String>()
        Files.list(GamePath.worlds).forEach { f -> names.add(f.name) }
        ImGui.listBox("Worlds", state, names.toTypedArray())

        if (ImGui.button("Load")) {
            val name = names[state.get()]
            if (name.isNotEmpty()) {
                val world = World.load(name)
                SceneManager.set(PlayScene(world))
            }
        }

        ImGui.sameLine()

        if (ImGui.button("Delete")) {
            val name = names[state.get()]
            if (name.isNotEmpty()) {
                val root = GamePath.worlds.resolve(name)
                FileUtils.deleteDirectory(root.toFile())
            }
        }

        ImGui.separator()

        if (ImGui.inputText("Name", name))
            if (!name.get().all { char -> char.isDigit() || char.isLetter() })
                name.set("")

        if (ImGui.inputText("Seed", seed))
            if (!seed.get().all { char -> char.isDigit() })
                seed.set("")

        if (ImGui.button("Create")) {
            if (!names.contains(name.get())) {
                if(name.get().isNotEmpty()) {
                    val world = World.create(name.get(), Vector3i(512, 512, 512), seed.get().toLong(), 64)
                    SceneManager.set(PlayScene(world))
                }
            }
        }

        ImGui.end()
    }

    override fun disable() {

    }

}