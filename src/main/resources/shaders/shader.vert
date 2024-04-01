#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aUV;

uniform mat4 model, view, projection;

out vec2 uv;

void main() {
    uv = aUV;
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}