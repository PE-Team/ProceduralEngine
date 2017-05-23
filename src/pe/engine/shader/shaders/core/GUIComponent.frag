#version 330 core

in vec3 vertEdge;
in vec3 vertBary;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;

void main() {
	vec2 colorMask = vec2(0.0, 1.0);
	float margin = 0.1;
	
//	if(vertEdge.x < margin || vertEdge.y < margin || vertEdge.z < margin){
//		color = vec4(0.1, 0.1, 0.1, 1.0);
//	}
	
	if(vertBary.x - vertBary.y > 0 && vertBary.y - vertBary.z > 0){
		colorMask.x = 1.0;
	}
	
	if(colorMask.x == 0.0){
		color = vec4(0.5, 0.5, 0.5, colorMask.y);
	}else{
		color = vec4(0.1, 0.1, 0.1, colorMask.y);
	}
	
	//color = vec4(vertEdge, 1.0);

//	if(drawBorder == 1.0){
//		color = borderColor;
//	}else{
//		color = backgroundColor;
//	}
}