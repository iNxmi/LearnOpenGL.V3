//BEGIN_VS
#version 330 core

layout (location = 0) in int a_data;

vec3 a_position = vec3(
    (a_data >> 0) & 63,
    (a_data >> 6) & 63,
    (a_data >> 12) & 63
);

float a_uv = ((a_data >> 18) & 63) / 14.0;

float mutation = ((a_data >> 24) & 31) / 15.0;

int normal_index = (a_data >> 29) & 7;
vec3 a_normal = vec3[6](
    vec3(0.0, 1.0, 0.0),
    vec3(0.0, -1.0, 0.0),
    vec3(-1.0, 0.0, 0.0),
    vec3(1.0, 0.0, 0.0),
    vec3(0.0, 0.0, 1.0),
    vec3(0.0, 0.0, -1.0)
)[normal_index];

uniform mat4 u_projection_matrix, u_view_matrix, u_model_matrix;
uniform sampler1D u_color_atlas;

out vec4 color;
out vec3 normal;

out vec3 ws_position;

void main() {
    color = vec4(texture(u_color_atlas, a_uv).rgb * mutation, texture(u_color_atlas, a_uv).a);
    normal = a_normal;

    ws_position = vec3(u_model_matrix * vec4(a_position, 1.0));

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrix * vec4(a_position, 1.0);
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

uniform float u_time;

out vec4 FragColor;

void main() {
    float ambient = 0.4;

    float diffuse = max(dot(normal, u_light_direction), 0.0);

    vec3 camera_direction = normalize(u_camera_position - ws_position);
    vec3 light_direction_reflected = reflect(-u_light_direction, normal);
    float specular = pow(max(dot(light_direction_reflected, camera_direction), 0.0), u_specular_exponent) * diffuse;

    vec3 result = color.rgb * min(ambient + diffuse + specular, 1.0) * ((cos(u_time / 3.14 * 2 + (1.0 - color.r) * 13 + (1.0 - color.g) * 19 + (1.0 - color.b) * 31) * 0.5 + 0.5) * 0.5 + 0.5);
    FragColor = vec4(result, color.a);
}
//END_FS