#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 border;

flat in vec2 borderWidthMod;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;
uniform int borderWidth;

vec2 ailizeVertField(vec2 vertField){
	vec2 d = fwidth(vertField);
	d *= borderWidth;
    vec2 a2 = smoothstep(d * 0.99, d * 1.01, vertField);
    return a2;
}

vec2 draw(vec2 mask){
	float margin = 0.1;
	float width = 0.0025;
	
	vec2 ailizedBorder0 = smoothstep(margin - width, margin + width, border);
	vec2 ailizedBorder1 = smoothstep(margin - width, margin + width, borderWidthMod - border);
	//mask.x = min(min(min(ailizedBorder0.x, ailizedBorder0.y), ailizedBorder1.x), ailizedBorder1.y);
	mask.x = min(ailizedBorder0.x, ailizedBorder0.y);

	return mask;
}

void main() {
	vec4 fillC = backgroundColor;
	vec4 borderC = borderColor;
	vec2 colorMask = vec2(1.0, 1.0);
	
	colorMask = draw(colorMask);
	
	color = mix(borderC, fillC, colorMask.x);
	color.w *= colorMask.y;
	
	//color = vec4(border, 0.0, 1.0);
}