#version 330 core

in vec3 vertIn;
in vec3 vertOut;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;

void main() {
	vec4 fill = vec4(0.5, 0.5, 0.5, 1.0);
	vec4 border = vec4(0.1, 0.1, 0.1, 1.0);
	vec2 colorMask = vec2(0.0, 1.0);
	float margin = 0.1;
	
	if(	(vertIn.x < margin && vertOut.x < margin) || (vertIn.y < margin && vertOut.y < margin) || 
		(vertIn.z < margin && vertOut.z < margin) ||
		(vertIn.x < margin && vertOut.z < margin) || (vertIn.y < margin && vertOut.x < margin) || 
		(vertIn.z < margin && vertOut.y < margin)	){
		colorMask.x = 1.0;
	}
	
	color = mix(fill, border, colorMask.x);
	color.w *= colorMask.y;
	
	//color = vec4(vertIn, 1.0);
	//color = vec4(vec3(colorMask.x), 1.0);
	color = vec4(0.0, vertOut.x, 0.0, 1.0);

//	if(drawBorder == 1.0){
//		color = borderColor;
//	}else{
//		color = backgroundColor;
//	}
}