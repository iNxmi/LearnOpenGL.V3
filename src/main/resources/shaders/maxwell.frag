#version 330 core

in vec2 uv;

uniform sampler2D u_tex_diffuse;

out vec4 FragColor;

void main() {
    FragColor = texture(u_tex_diffuse, uv);
}