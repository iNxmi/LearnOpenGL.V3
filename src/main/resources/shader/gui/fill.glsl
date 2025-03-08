//BEGIN_VS
#version 330 core

layout (location = 0) in vec3 a_position;

uniform mat4 u_model_matrix, u_projection_matrix;

void main() {
    gl_Position = u_projection_matrix * u_model_matrix * vec4(a_position, 1.0);
}
//END_VS

//BEGIN_FS
#version 330 core

uniform vec4 u_color;

out vec4 FragColor;

void main() {
    FragColor = u_color;
}
//END_FS