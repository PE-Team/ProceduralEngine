#version 330 core

in vec3 vertIn;
in vec3 vertOut;
flat in vec3 sidesConnect;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;

void main() {
	vec4 fill = vec4(0.5, 0.5, 0.5, 1.0);
	vec4 border = vec4(0.1, 0.1, 0.1, 1.0);
	vec2 colorMask = vec2(0.0, 1.0);
	float margin = 0.02;
	
	if	(	
		(vertOut.y < margin && sidesConnect.x == 1.0) || (vertOut.z < margin && sidesConnect.y == 1.0) ||
		(vertOut.x < margin && sidesConnect.z == 1.0) ||
		(vertIn.x < margin && vertOut.x < margin) || (vertIn.y < margin && vertOut.y < margin) || 
		(vertIn.z < margin && vertOut.z < margin)
		){
		
		colorMask.x = 0.5;
	}
	
	color = mix(fill, border, colorMask.x);
	color.w *= colorMask.y;
	
	//color = vec4(sidesConnect, 1.0);
	//color = vec4(vec3(colorMask.x), 1.0);
	//color = vec4(vec3(vertIn.x), 1.0);

//	if(drawBorder == 1.0){
//		color = borderColor;
//	}else{
//		color = backgroundColor;
//	}
}