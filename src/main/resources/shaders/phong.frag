#version 330 core

struct Material {
    vec3 color;

    sampler2D texture_diffuse;
    int use_texture_diffuse;

    sampler2D texture_specular;
    int use_texture_specular;

    float specular_exponent;
};

struct DirectionalLight {
    vec3 direction;
    vec3 color;
};

struct PointLight {
    vec3 position;
    vec3 color;
    vec3 attenuation;
};

struct SpotLight {
    vec3 position;
    vec3 direction;
    vec3 color;

    float angle_in;
    float angle_out;
};

in vec2 ls_uv;

in vec3 ws_position;
in vec3 ws_normal;

uniform Material u_material;

#define NUM_LIGHTS 4
uniform DirectionalLight u_directional_lights[NUM_LIGHTS];
uniform PointLight u_point_lights[NUM_LIGHTS];
uniform SpotLight u_spot_lights[NUM_LIGHTS];

uniform vec3 u_camera_position;

out vec4 FragColor;

#define INTENSITY_AMBIENT 0.1f

vec4 directional_light(DirectionalLight light, Material material, vec3 direction_camera) {
    float strength_diffuse = max(dot(ws_normal, light.direction), 0.0);
    vec4 intensity_diffuse;
    if (material.use_texture_diffuse == 1) {
        intensity_diffuse = texture(material.texture_diffuse, ls_uv) * (strength_diffuse + INTENSITY_AMBIENT);
    } else {
        intensity_diffuse = vec4(vec3(strength_diffuse + INTENSITY_AMBIENT), 1.0);
    }

    vec3 direction_light_reflected = reflect(-light.direction, ws_normal);
    float strength_specular = pow(max(dot(direction_camera, direction_light_reflected), 0.0), material.specular_exponent);
    vec4 intensity_specular;
    if (material.use_texture_specular == 1) {
        intensity_specular = texture(material.texture_specular, ls_uv) * strength_specular;
    } else {
        intensity_specular = vec4(strength_specular);
    }

    return vec4(light.color * material.color, 1.0) * (intensity_diffuse + intensity_specular * intensity_diffuse);
}

//vec3 point_light(PointLight light, Material material, vec3 direction_camera) {
//    vec3 direction_light = normalize(light.position - ws_position);
//    vec3 direction_light_reflected = reflect(-direction_light, ws_normal);
//
//    float diffuse_intensity = max(dot(ws_normal, direction_light), 0.0);
//
//    float specular_strength = pow(max(dot(direction_camera, direction_light_reflected), 0.0), material.specular_exponent);
//    vec3 specular_intensity = texture(material.texture_specular, ls_uv).xyz * specular_strength;
//
//    float distance = length(light.position - ws_position);
//    float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * distance * distance);
//
//    vec3 intensity = (diffuse_intensity + specular_intensity * diffuse_intensity) * attenuation;
//
//    vec3 result = light.color * intensity;
//    return result;
//}
//
//vec3 spot_light(SpotLight light, Material material, vec3 direction_camera) {
//    return vec3(0, 0, 0);
//}

void main() {
    vec3 direction_camera = normalize(u_camera_position - ws_position);

    vec4 color;
    for (int i = 0; i < 1; i++) {
        color += directional_light(u_directional_lights[i], u_material, direction_camera);
    }

    FragColor = color;
}