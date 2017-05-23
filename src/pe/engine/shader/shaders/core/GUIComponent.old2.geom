#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 15) out;

in VS_OUT {
	vec4 vertexOffset;
	vec2 vertexIDs;
} gs_in[];

out float drawBorder;

uniform vec2 windowDimensions;
uniform int borderRadius;
uniform int borderWidth;

bool isNotColinear(vec4 v1, vec4 v2, vec4 v3){
	return dot(v1 - v2, v1 - v3) != 0.0;
}

void drawSide(vec4 outer1, vec4 inner1, vec4 outer2, vec4 inner2){
	if(isNotColinear(outer1, inner1, inner2)){
	
		gl_Position = outer1;
		EmitVertex();
		
		gl_Position = inner1;
		EmitVertex();
		
		gl_Position = outer2;
		EmitVertex();
		
		//gl_Position = inner2;
		//EmitVertex();
		
		EndPrimitive();
	}
}

void main() {
	vec4 outer0 = gl_in[0].gl_Position;
	vec4 outer1 = gl_in[1].gl_Position;
	vec4 outer2 = gl_in[2].gl_Position;
	
	vec4 inner0 = gl_in[0].gl_Position + borderWidth * gs_in[0].vertexOffset;
	vec4 inner1 = gl_in[1].gl_Position + borderWidth * gs_in[1].vertexOffset;
	vec4 inner2 = gl_in[2].gl_Position + borderWidth * gs_in[2].vertexOffset;

	// MIDDLE TRIANGLE
	drawBorder = 0.25;
	gl_Position = inner0;
	EmitVertex();
	
	gl_Position = inner1;
	EmitVertex();
	
	gl_Position = inner2;
	EmitVertex();
	
	EndPrimitive();
	
	// SIDES TRIANGLE
	drawBorder = 0.5;
	drawSide(outer0, inner0, outer1, inner1);
	//drawSide(outer1, inner1, outer2, inner2);
	//drawSide(outer2, inner2, outer0, inner0);
}