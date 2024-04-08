package com.nami.scene.scenes

import com.nami.camera.Camera
import com.nami.Game
import com.nami.constants.Directions
import com.nami.constants.GamePaths
import com.nami.entity.Entity
import com.nami.entity.light.DirectionalLight
import com.nami.input.Input
import com.nami.model.ModelManager
import com.nami.nanovg.NVGDrawCall
import com.nami.nanovg.NVGManager
import com.nami.scene.Scene
import com.nami.shader.ShaderManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import mu.KotlinLogging
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.cos
import kotlin.math.sin


class TestScene : Scene {

    private val log = KotlinLogging.logger { }

    private val entities = mutableListOf<Entity>()

    private val fps = ImInt(Game.FPS.toInt())

    private val fov = floatArrayOf(90.0f)
    private val clip = floatArrayOf(1e-3f, 1000.0f)
    private val aspect = intArrayOf(16, 9)
    private val cam =
        Camera(Math.toRadians(fov[0].toDouble()).toFloat(), aspect[0].toFloat() / aspect[1].toFloat(), clip[0], clip[1])

    private val camSpeed = floatArrayOf(3f)
    private val camSensivity = floatArrayOf(0.11f)

    private var polygonMode = GL_FILL
    private var cullMode = GL_BACK
    private var cullfaceEnabled = false

    private var menu = false
    private var f3 = true

    init {
        glfwSetInputMode(Game.WINDOW_PTR, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(Game.WINDOW_PTR, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)

        val radius = 75f
        for (i in 0 until 25) {
            val entity = Entity(ModelManager.get("teapot"))
            entity.transform.position.set(
                Math.random(),
                Math.random(),
                Math.random()
            ).mul(2f).sub(1f, 1f, 1f).mul(Math.random().toFloat() * radius)
            entity.transform.rotation.set(Math.random()).mul(360f)
            entity.transform.scale.set(1f)

            entities.add(entity)
        }
    }

    private var yaw = 0f
    private var pitch = 0f

    override fun update() {
        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN) {
            menu = !menu
            glfwSetInputMode(Game.WINDOW_PTR, GLFW_CURSOR, if (menu) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED)
        }

        if (Input.keyStates[GLFW_KEY_F2] == Input.State.DOWN) {
            val widthArr = IntArray(1)
            val heightArr = IntArray(1)
            glfwGetWindowSize(Game.WINDOW_PTR, widthArr, heightArr)

            val width = widthArr[0]
            val height = heightArr[0]

            val buffer = BufferUtils.createByteBuffer(width * height * 3)
            glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, buffer)

            val pixels = IntArray(width * height)
            for (i in pixels.indices) {
                val red = buffer.get().toInt() and 0xFF shl 16
                val green = buffer.get().toInt() and 0xFF shl 8
                val blue = buffer.get().toInt() and 0xFF

                pixels[i] = red or green or blue
            }

            val flipped = IntArray(width * height)
            for (y in 0 until height)
                if (width >= 0)
                    System.arraycopy(pixels, ((height - 1) - y) * width, flipped, y * width, width)

            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            image.setRGB(0, 0, width, height, flipped, 0, width)

            val path = GamePaths.screenshots.resolve("${System.currentTimeMillis()}.png")
            ImageIO.write(image, "png", path.toFile())

            log.info { "Screenshot saved at '$path'" }
        }

        //Camera Rotation
        if (glfwGetInputMode(Game.WINDOW_PTR, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            yaw += Input.posDelta.x * camSensivity[0]
            pitch -= Input.posDelta.y * camSensivity[0]
            pitch = pitch.coerceIn(-90f, 90f)

            cam.direction.set(
                cos(Math.toRadians(yaw.toDouble())) * cos(Math.toRadians(pitch.toDouble())),
                sin(Math.toRadians(pitch.toDouble())),
                sin(Math.toRadians(yaw.toDouble())) * cos(Math.toRadians(pitch.toDouble()))
            ).normalize()
        }

        //Camera Movement
        run {
            var speed = camSpeed[0] * Game.DELTA_TIME
            if (Input.isKeyInStates(GLFW_KEY_LEFT_SHIFT, Input.State.DOWN, Input.State.HOLD))
                speed *= 5f

            if (Input.isKeyInStates(GLFW_KEY_W, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).mul(1f, 0f, 1f).mul(speed))
            if (Input.isKeyInStates(GLFW_KEY_S, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).mul(1f, 0f, 1f).mul(-speed))

            if (Input.isKeyInStates(GLFW_KEY_A, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).cross(Directions.UP).normalize().mul(-speed))
            if (Input.isKeyInStates(GLFW_KEY_D, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).cross(Directions.UP).normalize().mul(speed))

            if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(0f, speed, 0f)
            if (Input.isKeyInStates(GLFW_KEY_LEFT_CONTROL, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(0f, -speed, 0f)
        }
    }

    val dirLight0 = DirectionalLight(direction = Vector3f(1f, 1f, 0f).normalize(), color = Vector3f(1f, 1f, 1f))
    override fun render() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
        if (cullfaceEnabled) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE)
        glCullFace(cullMode)

        val shader = ShaderManager.bind("phong")

        shader.uniform.set("u_projection_matrix", cam.projection)
        shader.uniform.set("u_view_matrix", cam.view)


        shader.uniform.set("u_directional_lights[0].direction", dirLight0.direction)
        shader.uniform.set("u_directional_lights[0].color", dirLight0.color)

//        shader.uniform.set("u_point_lights[0].position", light.position)
//        shader.uniform.set("u_point_lights[0].color", light.color)
//        shader.uniform.set("u_point_lights[0].attenuation", light.attenuation)
        shader.uniform.set("u_camera_position", cam.position)

        entities.forEach { e -> e.render(shader) }

//        light.render(shader)

        ShaderManager.unbind()
    }

    override fun renderNVG() {
        if (!f3)
            return

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        val hours = glfwGetTime().toInt() / 3600
        val minutes = (glfwGetTime().toInt() % 3600) / 60
        val seconds = glfwGetTime().toInt() % 60

        val fps = 1.0f / Game.DELTA_TIME
        val text = String.format(
            "FPS=%.1f\nDELTA_TIME=%f\nglfwGetTime=%.2f (%02d:%02d:%02d)\ncam.position=(%.2f; %.2f; %.2f))",
            fps,
            Game.DELTA_TIME,
            glfwGetTime(),
            hours,
            minutes,
            seconds,
            cam.position.x,
            cam.position.y,
            cam.position.z
        )
        NVGManager.draw().color(Color(1f, 1f, 1f, 1f)).fontFace("cascadia_code").fontSize(32.0f)
            .textAlign(NVGDrawCall.TextAlign.LEFT).textBox(10.0f, 40.0f, 1920f, text).draw(true)
    }

    private val comboPolyMode = ImInt()
    private val comboCullMode = ImInt(1)
    override fun renderImGUI() {
        if (!menu)
            return

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        val width = IntArray(1)
        val height = IntArray(1)
        glfwGetWindowSize(Game.WINDOW_PTR, width, height)

        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(width[0].toFloat(), height[0].toFloat())
        ImGui.setNextWindowBgAlpha(0.4f)

        ImGui.getFont().scale = 2f
        ImGui.begin("Settings", ImBoolean(), ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        if (ImGui.collapsingHeader("Window")) {
            if (ImGui.inputInt("FPS", fps))
                Game.FPS = fps.toFloat()
        }

        if (ImGui.collapsingHeader("Camera")) {
            if (ImGui.sliderFloat("FOV", fov, 1f, 179f))
                cam.fov = Math.toRadians(fov[0].toDouble()).toFloat()

            if (ImGui.sliderFloat2("zNear / zFar", clip, 1e-3f, 100.0f)) {
                cam.zNear = clip[0]
                cam.zFar = clip[1]
            }

            if (ImGui.inputInt2("Aspect Ratio", aspect)) {
                aspect[0] = aspect[0].coerceIn(1, 100)
                aspect[1] = aspect[1].coerceIn(1, 100)
                cam.aspect = aspect[0].toFloat() / aspect[1].toFloat()
            }

            ImGui.sliderFloat("Speed", camSpeed, 0.5f, 20.0f)
            ImGui.sliderFloat("Sensivity", camSensivity, 0.01f, 1f)
        }

        if (ImGui.collapsingHeader("OpenGL")) {
            if (ImGui.combo("glPolygonMode", comboPolyMode, arrayOf("GL_FILL", "GL_LINE", "GL_POINT")))
                polygonMode = when (comboPolyMode.get()) {
                    0 -> GL_FILL
                    1 -> GL_LINE
                    2 -> GL_POINT
                    else -> 0
                }

            if (ImGui.checkbox("glEnable(GL_CULL_FACE)", cullfaceEnabled))
                cullfaceEnabled = !cullfaceEnabled

            ImGui.sameLine()

            if (ImGui.combo("glCullFace", comboCullMode, arrayOf("GL_FRONT_AND_BACK", "GL_BACK", "GL_FRONT")))
                cullMode = when (comboCullMode.get()) {
                    0 -> GL_FRONT_AND_BACK
                    1 -> GL_BACK
                    2 -> GL_FRONT
                    else -> 0
                }
        }

        if (ImGui.radioButton("F3 Overlay", f3))
            f3 = !f3

        if (ImGui.button("Quit"))
            glfwSetWindowShouldClose(Game.WINDOW_PTR, true)

        ImGui.end()
    }

}