//BEGIN_VS
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
//END_VS

//BEGIN_FS
#version 330 core

in vec3 color;
in vec3 normal;

in vec3 ws_position;

uniform float u_daylight_percentage;
uniform float u_ambient_strength;
uniform vec3 u_light_direction;
uniform vec3 u_camera_position;
uniform float u_specular_exponent;

out vec4 FragColor;

void main() {
    float ambient = u_ambient_strength;

    float diffuse = max(dot(normal, u_light_direction), 0.0) * u_daylight_percentage;

    vec3 camera_direction = normalize(u_camera_position - ws_position);
    vec3 light_direction_reflected = reflect(-u_light_direction, normal);
    float specular = pow(max(dot(light_direction_reflected, camera_direction), 0.0), u_specular_exponent) * diffuse;

    vec3 result = color * min(ambient + diffuse + specular, 1.0);
    FragColor = vec4(result, 1.0);
}
//END_FS