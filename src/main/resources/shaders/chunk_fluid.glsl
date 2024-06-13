//BEGIN_VS
#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec3 a_normal;
layout (location = 2) in vec2 a_uv;
layout (location = 3) in vec3 a_color;
layout (location = 4) in float a_brightness;
layout (location = 5) in float a_health;

uniform mat4 u_projection_matrix, u_view_matrix, u_model_matrix;

out vec2 ls_uv;

out vec3 ws_position;
out vec3 ws_normal;
out vec3 color_mod;

out float brightness;

void main() {
    ls_uv = a_uv;

    vec3 position = a_position - vec3(0, 2.0 / 16.0, 0);

    ws_position = vec3(u_model_matrix * vec4(position, 1.0));
    ws_normal = a_normal;
    color_mod = a_color;

    brightness = a_brightness * a_health;

    gl_Position = u_projection_matrix * u_view_matrix * u_model_matrix * vec4(position, 1.0);
}
//END_VS

//BEGIN_FS
#version 330 core

in vec2 ls_uv;

in vec3 ws_position;
in vec3 ws_normal;
in vec3 color_mod;

in float brightness;

uniform vec3 u_light_direction;
uniform vec3 u_camera_position;
uniform float u_specular_exponent;

uniform sampler2D u_texture_diffuse;

uniform float u_time;

out vec4 FragColor;

void main() {
    float ambient = 0.4;

    float diffuse = max(dot(ws_normal, u_light_direction), 0.0);

    vec3 camera_direction = normalize(u_camera_position - ws_position);
    vec3 light_direction_reflected = reflect(-u_light_direction, ws_normal);
    float specular = pow(max(dot(light_direction_reflected, camera_direction), 0.0), u_specular_exponent) * diffuse;

    vec4 color = texture(u_texture_diffuse, ls_uv);
    vec3 result = color.rgb * color_mod * min(ambient + diffuse + specular, 1.0) * ((cos(u_time / 3.14 * 2 + (1.0 - color.r) * 13 + (1.0 - color.g) * 19 + (1.0 - color.b) * 31) * 0.5 + 0.5) * 0.5 + 0.5) * brightness;

    FragColor = vec4(result, color.a);
}
//END_FS