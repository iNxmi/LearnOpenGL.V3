#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec3 a_color;
layout (location = 2) in vec3 a_normal;


uniform mat4 u_projection_matrix, u_view_matrix, u_model_matrix;

out vec3 color;
out vec3 normal;

out vec3 ws_position;

void main() {
    color = a_color;
    normal = normalize(a_normal);

    ws_position = vec3(u_model_matrix * vec4(a_position, 1.0));

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrix * vec4(a_position, 1.0);
}