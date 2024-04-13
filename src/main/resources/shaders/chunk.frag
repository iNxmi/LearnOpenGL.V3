#version 330 core

in vec3 color;
in vec3 normal;

in vec3 ws_position;

uniform float u_sin_t;
uniform float u_ambient_strength;
uniform vec3 u_light_direction;
uniform vec3 u_camera_position;
uniform float u_specular_exponent;

out vec4 FragColor;

void main() {
    float ambient = u_ambient_strength;
    float diffuse = 0;
    float specular = 0;
    if (u_sin_t >= 0) {
        diffuse = max(dot(normal, max(u_light_direction, vec3(0, 0, 0))), 0.0) * u_sin_t;

        vec3 camera_direction = normalize(u_camera_position - ws_position);
        vec3 light_direction_reflected = reflect(-max(u_light_direction, vec3(0, 0, 0)), normal);
        specular = pow(max(dot(light_direction_reflected, camera_direction), 0.0), u_specular_exponent) * diffuse;
    }

    vec3 result = color * min(ambient + diffuse + specular, 1.0);

    FragColor = vec4(result, 1.0);
}
