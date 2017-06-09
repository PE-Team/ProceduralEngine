#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 texCoord;

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

float ailizeBorderRadius(float borderR, float ailizedWidth, vec2 border){
	float x = borderR - border.x;
	float y = borderR - border.y;
	float r = sqrt(x*x + y*y);
	float radius = 1 - smoothstep(borderR - ailizedWidth, borderR + ailizedWidth, r);
	vec2 side = step(borderR, border);
	return max(max(side.x, side.y), radius);
}

vec2 draw(vec2 mask){
	float aWidth = 1.5;
	vec2 border = (vec2(1.0) - texCoord);
	border.x *= width;
	border.y *= height;
	
	float top = smoothstep(borderWidth.x - aWidth, borderWidth.x + aWidth, border.y);
	float right = smoothstep(borderWidth.y - aWidth, borderWidth.y + aWidth, width - border.x);
	float bottom = smoothstep(borderWidth.z - aWidth, borderWidth.z + aWidth, height - border.y);
	float left = smoothstep(borderWidth.w - aWidth, borderWidth.w + aWidth, border.x);
	
	mask.x = min(min(min(top, right), bottom), left);
	
	float topLeft = ailizeBorderRadius(borderRadius.x, aWidth, border);
	float topRight = ailizeBorderRadius(borderRadius.y, aWidth, vec2(width - border.x, border.y));
	float bottomRight = ailizeBorderRadius(borderRadius.z, aWidth, vec2(width - border.x, height - border.y));
	float bottomLeft = ailizeBorderRadius(borderRadius.w, aWidth, vec2(border.x, height - border.y));
	
	mask.y = min(min(min(topLeft, topRight), bottomRight), bottomLeft);;

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