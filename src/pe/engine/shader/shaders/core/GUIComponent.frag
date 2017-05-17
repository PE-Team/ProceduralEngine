#version 150 core

out vec4 fragColor;

uniform vec4 backgroundColor;
uniform int borderRadius;
uniform int borderWidth;
uniform vec4 borderColor;

void main() {
    fragColor = backgroundColor;
}