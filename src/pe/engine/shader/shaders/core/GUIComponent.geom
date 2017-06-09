#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
	vec2 border;
} gs_in[];

out vec2 border;

flat out vec2 borderWidthMod;

uniform float screenRatio;

vec4 ratioize(vec4 vec){
	return vec4(vec.x * screenRatio, vec.y, vec.z, vec.w);
}

float distToLine(vec4 point, vec4 linePoint1, vec4 linePoint2){
	return min( length( ratioize(linePoint1 - point) ), length( ratioize(linePoint2 - point) ) );
}

void main() {
	vec4 vertex0 = gl_in[0].gl_Position;
	vec4 vertex1 = gl_in[1].gl_Position;
	vec4 vertex2 = gl_in[2].gl_Position;
	
	vec2 borderWMod = vec2(0.0);
	float dist = 0.0;
	
	dist = distToLine(vertex0, vertex1, vertex2);
	border = gs_in[0].border;
	borderWMod += border;
	gl_Position = vertex0;
	EmitVertex();
	
	dist = distToLine(vertex1, vertex0, vertex2);
	border = gs_in[1].border;
	borderWMod += border;
	gl_Position = vertex1;
	EmitVertex();
	
	dist = distToLine(vertex2, vertex1, vertex0);
	border = gs_in[2].border;
	borderWMod += border;
	borderWidthMod = borderWMod;
	gl_Position = vertex2;
	EmitVertex();
	
	EndPrimitive();
}