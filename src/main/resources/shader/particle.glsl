//BEGIN_VS
#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_uv;
layout (location = 2) in vec2 a_normal;

uniform mat4 u_projection_matrix;
uniform mat4 u_view_matrix;

uniform mat4 u_model_matrices[128];
uniform vec3 u_colors[128];

out vec3 color;

void main() {
    color = u_colors[gl_InstanceID];

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrices[gl_InstanceID] * vec4(a_position, 1.0);
}
//END_VS

//BEGIN_FS
#version 330 core

in vec3 color;

void main() {
    gl_FragColor = vec4(color, 1.0);
}
//END_FS