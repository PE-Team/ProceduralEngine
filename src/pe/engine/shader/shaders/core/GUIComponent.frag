#version 150 core

in vec3 barycentricValue;

out vec4 fragColor;

uniform vec4 backgroundColor;
uniform int borderRadius;
uniform int borderWidth;
uniform vec4 borderColor;

void main() {
	if(any(lessThan(barycentricValue, vec3(1)))){
		fragColor = backgroundColor;
	}else{
		fragColor = borderColor;
	}
	fragColor = vec4(barycentricValue, 1.0);
}