#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 texCoord;

out vec4 color;

uniform float width;
uniform float height;
uniform vec4 backgroundColor;
uniform sampler2D backgroundTexture;
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

float draw(){
	float aWidth = 1.0;
	vec2 border = (vec2(1.0) - texCoord);
	border.x *= width;
	border.y *= height;
	
	vec2 topLeftBorder = vec2(width - border.x, border.y);
	vec2 topRightBorder = border;
	vec2 bottomRightBorder = vec2(border.x, height - border.y);
	vec2 bottomLeftBorder = vec2(width - border.x, height - border.y);
	
	float top = step(borderWidth.x + aWidth, border.y);
	float right = step(borderWidth.y + aWidth, border.x);
	float bottom = step(borderWidth.z + aWidth, height - border.y);
	float left = step(borderWidth.w + aWidth, width - border.x);
	
	float mask = min(min(min(top, right), bottom), left);
	
	float topLeftEdge = ailizeBorderRadius(borderRadius.x, aWidth, topLeftBorder);
	float topRightEdge = ailizeBorderRadius(borderRadius.y, aWidth, topRightBorder);
	float bottomRightEdge = ailizeBorderRadius(borderRadius.z, aWidth, bottomRightBorder);
	float bottomLeftEdge = ailizeBorderRadius(borderRadius.w, aWidth, bottomLeftBorder);
	
	mask = min(min(min(min(topLeftEdge, topRightEdge), bottomRightEdge), bottomLeftEdge), mask);

	return mask;
}

float calcDimTexCoord(float texCoord, float width, float borderL, float borderR){
	float texWidth = width - borderL - borderR;
	return texCoord * width / texWidth - borderL / texWidth;
}

vec2 calcBackgroundTexCoords(vec2 texCoord, float width, float height, vec4 border){
	return vec2(
		calcDimTexCoord(texCoord.x, width, border.w, border.y),
		calcDimTexCoord(texCoord.y, height, border.z, border.x)
	);
}

void main() {
	vec2 backgroundTexCoord = calcBackgroundTexCoords(texCoord, width, height, borderWidth);
	
	vec4 backgroundTextureColor = texture(backgroundTexture, backgroundTexCoord);
	
	color = mix(backgroundColor, backgroundTextureColor, backgroundTextureColor.w);
	
	color.w *= draw();
}