#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 2) in vec2 a_uv;

uniform mat4 u_projection, u_view, u_model;

out vec2 uv;

void main() {
    uv = a_uv;

    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);
}