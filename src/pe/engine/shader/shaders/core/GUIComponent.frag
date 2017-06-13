#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 texCoord;

flat in vec2 borderWidthMod;

out vec4 color;

uniform float pixWidth;
uniform float width;
uniform float height;
uniform vec4 backgroundColor;
uniform vec4 borderColor;
uniform vec4 borderWidth;
uniform vec4 borderRadius;

float ailizeBorderRadius(float borderR, float ailizedWidth, vec2 border){
	float x = borderR - border.x;
	float y = borderR - border.y;
	float r = sqrt(x*x + y*y);
	float radius = 1 - smoothstep(borderR - ailizedWidth, borderR + ailizedWidth, r);
	vec2 side = step(borderR, border);
	return max(max(side.x, side.y), radius);
}

float ailizeElliseRadius(float width, float height, float borderR, float ailizedWidth, vec2 border){
	float x = borderR - border.x;
	float y = borderR - border.y;
	float r = sqrt(x*x + y*y);
	float eRadius = (width * height * r) / sqrt(width*width * y*y + height*height * x*x);
	float radius = 1 - smoothstep(eRadius - ailizedWidth, eRadius + ailizedWidth, r);
	vec2 side = step(borderR, border);
	return max(max(side.x, side.y), radius);
}

vec2 draw(vec2 mask){
	float aWidth = 1.0;
	vec2 border = (vec2(1.0) - texCoord);
	border.x *= width;
	border.y *= height;
	
	vec2 topLeftBorder = vec2(width - border.x, border.y);
	vec2 topRightBorder = border;
	vec2 bottomRightBorder = vec2(border.x, height - border.y);
	vec2 bottomLeftBorder = vec2(width - border.x, height - border.y);
	
	float top = smoothstep(borderWidth.x - aWidth, borderWidth.x + aWidth, border.y);
	float right = smoothstep(borderWidth.y - aWidth, borderWidth.y + aWidth, border.x);
	float bottom = smoothstep(borderWidth.z - aWidth, borderWidth.z + aWidth, height - border.y);
	float left = smoothstep(borderWidth.w - aWidth, borderWidth.w + aWidth, width - border.x);
	
	float topLeftInside = ailizeElliseRadius(borderRadius.x - borderWidth.w, borderRadius.x - borderWidth.x, borderRadius.x, aWidth, topLeftBorder);
	float topRightInside = ailizeElliseRadius(borderRadius.y - borderWidth.y, borderRadius.y - borderWidth.x, borderRadius.y, aWidth, topRightBorder);
	float bottomRightInside = ailizeElliseRadius(borderRadius.z - borderWidth.y, borderRadius.z - borderWidth.z, borderRadius.z, aWidth, bottomRightBorder);
	float bottomLeftInside = ailizeElliseRadius(borderRadius.w - borderWidth.w, borderRadius.w - borderWidth.z, borderRadius.w, aWidth, bottomLeftBorder);
	
	mask.x = min(min(min(min(min(min(min(top, right), bottom), left), topLeftInside), topRightInside), bottomRightInside), bottomLeftInside);
	
	float topLeftEdge = ailizeBorderRadius(borderRadius.x, aWidth, topLeftBorder);
	float topRightEdge = ailizeBorderRadius(borderRadius.y, aWidth, topRightBorder);
	float bottomRightEdge = ailizeBorderRadius(borderRadius.z, aWidth, bottomRightBorder);
	float bottomLeftEdge = ailizeBorderRadius(borderRadius.w, aWidth, bottomLeftBorder);
	
	mask.y = min(min(min(topLeftEdge, topRightEdge), bottomRightEdge), bottomLeftEdge);

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