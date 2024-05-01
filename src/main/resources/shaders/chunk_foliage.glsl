//BEGIN_VS
#version 330 core

layout (location = 0) in int a_data;
layout (location = 1) in int a_data_color;

vec3 a_position = vec3(
    (a_data >> 0) & 31,
    (a_data >> 5) & 31,
    (a_data >> 10) & 31
);

vec4 a_color = vec4(
    (a_data_color >> 16) & 0xFF,
    (a_data_color >> 8) & 0xFF,
    (a_data_color >> 0) & 0xFF,
    (a_data_color >> 24) & 0xFF
) / vec4(255.0);

vec3 NORMALS[6] = vec3[6](
    vec3(0.0, 1.0, 0.0),
    vec3(0.0, -1.0, 0.0),
    vec3(-1.0, 0.0, 0.0),
    vec3(1.0, 0.0, 0.0),
    vec3(0.0, 0.0, 1.0),
    vec3(0.0, 0.0, -1.0)
);

int normal_index = (a_data >> 15) & 7;
vec3 a_normal = NORMALS[normal_index];

uniform mat4 u_projection_matrix, u_view_matrix, u_model_matrix;
uniform float u_time;

out vec4 color;
out vec3 normal;

out vec3 ws_position;

void main() {
    color = a_color;
    normal = a_normal;

    vec3 pos = a_position + vec3(sin(u_time), cos(u_time), sin(u_time) * cos(-u_time)) * 0.05;

    ws_position = vec3(u_model_matrix * vec4(pos, 1.0));

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrix * vec4(pos, 1.0);
}
//END_VS

//BEGIN_FS
#version 330 core

in vec4 color;
in vec3 normal;

in vec3 ws_position;

uniform vec3 u_light_direction;
uniform vec3 u_camera_position;
uniform float u_specular_exponent;

uniform float u_gamma;

out vec4 FragColor;

void main() {
    float ambient = 0.4;

    float diffuse = max(dot(normal, u_light_direction), 0.0);

    vec3 camera_direction = normalize(u_camera_position - ws_position);
    vec3 light_direction_reflected = reflect(-u_light_direction, normal);
    float specular = pow(max(dot(light_direction_reflected, camera_direction), 0.0), u_specular_exponent) * diffuse;

    vec3 result = color.rgb * min(ambient + diffuse + specular, 1.0);
    FragColor = vec4(result, color.a);
}
//END_FS