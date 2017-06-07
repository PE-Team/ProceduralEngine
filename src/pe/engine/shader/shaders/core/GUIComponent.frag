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

vec4 borderRadiusCalc(vec3 borderR, vec2 pos){
	borderR *= 2*pixWidth;
	
	vec4 result = vec4(pos, 1.0, 1.0);
	
	if(pos.x > borderWidthMod.x - borderR.x){
		float x = borderR.x + pos.x - borderWidthMod.x;
		float y = borderR.x - pos.y;
		float r = sqrt(x*x + y*y);
		result.x = max(r - borderR.x + borderWidthMod.x, result.x);
		result.z = min(x / r, result.z);
		result.w = min(y / r, result.w);
	}
	
	if(pos.y < borderR.y && pos.x < borderR.y){
		float x = borderR.y - pos.x;
		float y = borderR.y - pos.y;
		float r = sqrt(x*x + y*y);
		float res = borderR.y - r;
		result.x = min(res, result.x);
		result.y = min(res, result.y);
		result.z = min(x / r, result.z);
		result.w = min(y / r, result.w);
	}
	
	if(pos.y > borderWidthMod.y - borderR.z){
		float x = borderR.z - pos.x;
		float y = borderR.z + pos.y - borderWidthMod.y;
		float r = sqrt(x*x + y*y);
		result.y = max(r - borderR.z + borderWidthMod.y, result.y);
		result.z = min(x / r, result.z);
		result.w = min(y / r, result.w);
	}
	
	return result;
}

vec2 draw(vec2 mask){
	vec4 borderW = pixWidth * borderWidth;
	float width = 1.2 * pixWidth;
	
	vec2 varBorder = border;
	if(isTop > 0.5){
		vec4 borderValue = borderRadiusCalc(borderRadius.yxw, varBorder);
		varBorder.x = borderValue.x;
		varBorder.y = borderValue.y;
		
		float halfWidth = borderWidthMod.x / 2;
		float halfHeight = borderWidthMod.y / 2;
		
		if(varBorder.x < halfWidth && varBorder.y < halfHeight){
			
			if(borderValue.z < 1.0){
				float radius = mix(borderW.x, borderW.w, borderValue.z);
				borderW.x = radius;
				borderW.w = radius;
			}
			float left = smoothstep(borderW.w - width, borderW.w + width, varBorder.x);
			float right = smoothstep(borderW.x - width, borderW.x + width, varBorder.y);
			mask.x = min(left, right);
		}else if(varBorder.x < halfWidth && varBorder.y >= halfHeight){

			float left = smoothstep(borderW.y - width, borderW.y + width, borderWidthMod.y - varBorder.y);
			float right = smoothstep(borderW.w - width, borderW.w + width, varBorder.x);
			mask.x = min(left, right);
		}else if(varBorder.x >= halfWidth && varBorder.y < halfHeight){
			mask.x = 0.7;
		}
		
	}else{
		vec4 borderValue = borderRadiusCalc(borderRadius.yzw, varBorder);
		varBorder.x = borderValue.x;
		varBorder.y = borderValue.y;
		
		float leftRadius = 0.0;
		float topRadius = 0.0;
		
		float rightRadius = 0.0;//mix(borderW.z, borderW.w, borderValue.w);
		float bottomRadius = 0.0;//mix(borderW.y, borderW.z, borderValue.z);
		
		float ailizedLeft = smoothstep(leftRadius - width, leftRadius + width, borderWidthMod.y - varBorder.y);
		float ailizedTop = smoothstep(topRadius - width, topRadius + width, borderWidthMod.x - varBorder.x);
		
		float ailizedRight = smoothstep(rightRadius - width, rightRadius + width, varBorder.y);
		float ailizedBottom = smoothstep(bottomRadius - width, bottomRadius + width, varBorder.x);
		
		mask.x = min(min(min(ailizedLeft, ailizedTop), ailizedRight), ailizedBottom);
	}
	
	if(varBorder.x < 0.0 || varBorder.y < 0.0 || varBorder.x > borderWidthMod.x || varBorder.y > borderWidthMod.y){
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