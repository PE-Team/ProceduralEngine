#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 6) out;

in VS_OUT {
	vec4 vertexOffset;
} gs_in[];

out float drawBorder;

uniform vec2 windowDimensions;
uniform int borderRadius;
uniform int borderWidth;

void main() {
	// MIDDLE TRIANGLE
	drawBorder = 0.25;
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	EndPrimitive();
	
	// OFFSET TRIANGLE
	drawBorder = 0.5;
	gl_Position = gl_in[0].gl_Position + gs_in[0].vertexOffset;
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + gs_in[1].vertexOffset;
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + gs_in[2].vertexOffset;
	EmitVertex();
	
	EndPrimitive();
}