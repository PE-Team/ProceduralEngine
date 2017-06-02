#version 330 core
#extension GL_OES_standard_derivatives : enable

in vec2 border;

flat in vec2 borderWidthMod;

out vec4 color;

uniform vec4 backgroundColor;
uniform vec4 borderColor;
uniform int borderWidth;
uniform int borderRadius;

vec2 ailizeVertField(vec2 vertField){
	vec2 d = fwidth(vertField);
	d *= borderWidth;
    vec2 a2 = smoothstep(d * 0.99, d * 1.01, vertField);
    return a2;
}

float height(float r, float d){
	return sqrt(r*r - d*d);
}

vec2 draw(vec2 mask){
	float pixWidth = 0.0025;
	float margin = pixWidth * borderWidth;
	float width = 0.003;
	float radius = 100 * pixWidth;
	
	vec2 varBorder = border;
	
	// for varBorder.x
	if(varBorder.y < radius){
		varBorder.x -= radius - height(radius, varBorder.y);
	}
	
	vec2 ailizedBorder0 = smoothstep(margin - width, margin + width, varBorder);
	vec2 ailizedBorder1 = smoothstep(margin - width, margin + width, borderWidthMod - varBorder);
	mask.x = min(min(min(ailizedBorder0.x, ailizedBorder0.y), ailizedBorder1.x), ailizedBorder1.y);

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