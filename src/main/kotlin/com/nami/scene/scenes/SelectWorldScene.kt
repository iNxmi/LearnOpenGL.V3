package com.nami.scene.scenes

import com.nami.Window
import com.nami.resources.GamePath
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import com.nami.storage.Storage
import com.nami.world.World
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import imgui.type.ImString
import org.apache.commons.io.FileUtils
import org.joml.Vector3i
import java.nio.file.Files
import kotlin.io.path.name

class SelectWorldScene : Scene() {

    val state = ImInt()
    val seed = ImString(System.currentTimeMillis().toString())
    val name = ImString()
    override fun onRenderHUD() {
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

            val worldPath = GamePath.worlds.resolve(name)
            val world = Storage.read<World>(worldPath, "world")

            if (world != null)
                SceneManager.set(PlayScene(world))
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

        if (ImGui.button("Create")) {
            val name = this.name.get().trim()
            if (name.isNotBlank() && !names.contains(name)) {
                val world = World(name, Vector3i(16), seed.get().hashCode().toLong(), 64)
                SceneManager.set(PlayScene(world))
            }
        }

        ImGui.end()
    }

}