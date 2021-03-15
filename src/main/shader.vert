#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 uv;

uniform mat4 model;
uniform mat4 projection;

out vec4 colorOut;
out vec2 uvOut;

void main(void) {
    colorOut = color;
    uvOut = uv;
    vec4 v = vec4(position, 1.0);
    gl_Position = projection * model * v;
}
