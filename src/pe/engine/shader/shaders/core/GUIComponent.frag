#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 border;

flat in vec2 borderWidthMod;
flat in float isTop;

out vec4 color;

uniform float pixWidth;
uniform vec4 backgroundColor;
uniform vec4 borderColor;
uniform vec4 borderWidth;
uniform vec4 borderRadius;

vec2 ailizeVertField(vec2 vertField){
	vec2 d = fwidth(vertField);
	d *= borderWidth;
    vec2 a2 = smoothstep(d * 0.99, d * 1.01, vertField);
    return a2;
}

float height(float r, float d){
	return sqrt(r*r - d*d);
}

vec2 borderRadiusCalc(vec3 borderR, vec2 pos){
	borderR *= 2*pixWidth;
	
	vec2 result = pos;
	
	if(pos.x > borderWidthMod.x - borderR.x){
		float x = borderR.x + pos.x - borderWidthMod.x;
		float y = borderR.x - pos.y;
		float r = sqrt(x*x + y*y);
		result.x = min(borderR.x - r, result.x);
	}
	
	if(pos.y < borderR.y && pos.x < borderR.y){
		float x = borderR.y - pos.x;
		float y = borderR.y - pos.y;
		float r = sqrt(x*x + y*y);
		float res = borderR.y - r;
		result.x = min(res, result.x);
		result.y = min(res, result.y);
	}
	
	if(pos.y > borderWidthMod.y - borderR.z){
		float x = borderR.z - pos.x;
		float y = borderR.z + pos.y - borderWidthMod.y;
		float r = sqrt(x*x + y*y);
		result.y = min(borderR.z - r, result.y);
	}
	
	return result;
}

vec2 draw(vec2 mask){
	float margin = pixWidth * borderWidth.x;
	vec4 borderW = pixWidth * borderWidth;
	float width = 0.003;
	
	vec2 varBorder = border;
	if(isTop > 0.5){
		vec2 borderValue = borderRadiusCalc(borderRadius.yxw, varBorder);
		varBorder.x = borderValue.x;
		varBorder.y = borderValue.y;
		
		float ailizedLeft = smoothstep(borderW.w - width, borderW.w + width, varBorder.x);
		float ailizedTop = smoothstep(borderW.x - width, borderW.x + width, varBorder.y);
		
		float ailizedRight = smoothstep(borderW.y - width, borderW.y + width, borderWidthMod.x - varBorder.x);
		float ailizedBottom = smoothstep(borderW.z - width, borderW.z + width, borderWidthMod.y - varBorder.y);
		
		mask.x = min(min(min(ailizedLeft, ailizedTop), ailizedRight), ailizedBottom);
	}else{
		vec2 borderValue = borderRadiusCalc(borderRadius.yzw, varBorder);
		varBorder.x = borderValue.x;
		varBorder.y = borderValue.y;
		
		
		float ailizedLeft = smoothstep(borderW.w - width, borderW.w + width, borderWidthMod.y - varBorder.y);
		float ailizedTop = smoothstep(borderW.x - width, borderW.x + width, borderWidthMod.x - varBorder.x);
		
		float ailizedRight = smoothstep(borderW.y - width, borderW.y + width, varBorder.y);
		float ailizedBottom = smoothstep(borderW.z - width, borderW.z + width, varBorder.x);
		
		mask.x = min(min(min(ailizedLeft, ailizedTop), ailizedRight), ailizedBottom);
	}
	
	if(varBorder.x < 0.0 || varBorder.y < 0.0){
		mask.y = 0.0;
	}

	return mask;
}

void main() {
	vec4 fillC = backgroundColor;
	vec4 borderC = borderColor;
	vec2 colorMask = vec2(1.0, 1.0);
	
	colorMask = draw(colorMask);
	
	color = mix(borderC, fillC, colorMask.x);
	color.w *= colorMask.y;
}