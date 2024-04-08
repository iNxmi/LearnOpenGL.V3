#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec3 a_normal;
layout (location = 2) in vec2 a_uv;

uniform mat4 u_projection_matrix, u_view_matrix, u_model_matrix;
uniform mat3 u_normal_matrix;

out vec2 ls_uv;

out vec3 ws_position;
out vec3 ws_normal;


void main() {
    ls_uv = a_uv;

    ws_position = vec3(u_model_matrix * vec4(a_position, 1.0));
    ws_normal = normalize(u_normal_matrix * a_normal);

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrix * vec4(a_position, 1.0);
}