#version 330 core

in vec2 texCoord;

out vec4 color;

uniform int charX;
uniform int charWidth;
uniform int atlasWidth;
uniform vec4 textColor;
uniform sampler2D texture;

vec2 calcTexCoord(vec2 texCoord, float charX, float charWidth, float atlasWidth){
	float x = (texCoord.x * charWidth + charX) / atlasWidth;
	return vec2(x, texCoord.y);
}

void main() {
	color = texture(texture, calcTexCoord(texCoord, charX, charWidth, atlasWidth));
	color.xyz = textColor.xyz;
	
	//color = vec4(vec3(calcTexCoord(texCoord, charX, charWidth, atlasWidth).x), 1.0);
}