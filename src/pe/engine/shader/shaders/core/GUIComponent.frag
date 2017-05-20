#version 330 core

in float drawBorder;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;

void main() {
	color = vec4(vec3(drawBorder), 1.0);

//	if(drawBorder == 1.0){
//		color = borderColor;
//	}else{
//		color = backgroundColor;
//	}
}