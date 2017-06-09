#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 border;

flat in vec2 borderWidthMod;

out vec4 color;

uniform float pixWidth;
uniform int width;
uniform int height;
uniform vec4 backgroundColor;
uniform vec4 borderColor;
uniform vec4 borderWidth;
uniform vec4 borderRadius;

float ellipseRadius(float width, float height, float theta){
	float sin = sin(theta);
	float cos = cos(theta);
	return (width*height)/sqrt(width*width*sin*sin + height*height*cos*cos);
}

float ailizeBorder(float borderW, float dimension, float ailizedWidth, float borderPart){
	float min = (borderW - ailizedWidth) / dimension;
	float max = (borderW + ailizedWidth) / dimension;
	return smoothstep(min, max, borderPart);
}

float ailizeBorderRadius(float borderR, float ailizedWidth, vec2 border){
	float x = border.x * width - borderR;
	float y = border.y * height - borderR;
	float r = sqrt(x*x + y*y);
	float margin = 0.02;
	return smoothstep(0, margin, r);
}

vec2 draw(vec2 mask){
	float aWidth = 1.5;
	vec2 varBorder = border;
	
	float top = ailizeBorder(borderWidth.x, height, aWidth, 1.0 - varBorder.y);
	float right = ailizeBorder(borderWidth.y, width, aWidth, varBorder.x);
	float bottom = ailizeBorder(borderWidth.z, height, aWidth, varBorder.y);
	float left = ailizeBorder(borderWidth.w, width, aWidth, 1.0 - varBorder.x);
	
	mask.x = min(min(min(top, right), bottom), left);
	
	float topLeft = ailizeBorderRadius(borderRadius.x, aWidth, vec2(1.0) - varBorder);
	
	mask.y = topLeft;

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