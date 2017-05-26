#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec3 vertIn;
in vec3 vertOut;
flat in vec3 sidesConnect;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;


float mapToAsymtotic(float x, float power){
	return pow(-1/(x + 1) + 1, 1 / power);
}

vec3 ailizeVertField(vec3 vertField){
	vec3 d = fwidth(vertField);
	d *= 10;
    vec3 a3 = smoothstep(d*0.80, d, vertField);
    return a3;
}

vec2 drawAilized(vec2 mask){
	vec3 inField = ailizeVertField(vertIn);
	vec3 outField = ailizeVertField(vertOut);
	
	if( inField.x < 1.0 && outField.x < 1.0){
		mask.x = min(max(inField.x, outField.x), mask.x);
	}
	
	if( inField.y < 1.0 && outField.y < 1.0){
		mask.x = min(max(inField.y, outField.y), mask.x);
	}
	
	if( inField.z < 1.0 && outField.z < 1.0){
		mask.x = min(max(inField.z, outField.z), mask.x);
	}
	
	if( outField.x < 1.0 && sidesConnect.x == 1.0){
		mask.x = min(outField.x, mask.x);
	}
	
	if( outField.y < 1.0 && sidesConnect.y == 1.0){
		mask.x = min(outField.y, mask.x);
	}
	
	if( outField.z < 1.0 && sidesConnect.z == 1.0){
		mask.x = min(outField.z, mask.x);
	}
	
	return mask;
}

vec2 draw(vec2 mask){
	float margin = 0.02;
	
	if( vertIn.x < margin && vertOut.x < margin){
		mask.x = 0.5;
	}
	
	if( vertIn.y < margin && vertOut.y < margin){
		mask.x = 0.5;
	}
	
	if( vertIn.z < margin && vertOut.z < margin){
		mask.x = 0.5;
	}
	
	if( vertOut.x < margin && sidesConnect.x == 1.0){
		mask.x = 0.5;
	}
	
	if( vertOut.y < margin && sidesConnect.y == 1.0){
		mask.x = 0.5;
	}
	
	if( vertOut.z < margin && sidesConnect.z == 1.0){
		mask.x = 0.5;
	}

	return mask;
}

void main() {
	vec4 fill = vec4(0.5, 0.5, 0.5, 1.0);
	vec4 border = vec4(0.1, 0.1, 0.1, 1.0);
	vec2 colorMask = vec2(1.0, 1.0);
	
	colorMask = draw(colorMask);
	
	color = mix(border, fill, colorMask.x);
	color.w *= colorMask.y;
	
	//color = vec4(fwidth(vertIn)*100, 1.0);
	color = vec4(vec3((fwidth(vertIn)*500).z), 1.0);

//	if(drawBorder == 1.0){
//		color = borderColor;
//	}else{
//		color = backgroundColor;
//	}
}