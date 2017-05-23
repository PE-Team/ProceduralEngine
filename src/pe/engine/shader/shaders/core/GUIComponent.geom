#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
	vec2 vertexIDs;
} gs_in[];

out vec3 vertEdge;
out vec3 vertBary;

bool isEdge(int v1, int v2){
	return (gs_in[v1].vertexIDs.x == gs_in[v2].vertexIDs.y) || (gs_in[v2].vertexIDs.x == gs_in[v1].vertexIDs.y);
}

void main() {
	vec3 vert0 = vec3(1.0);
	vec3 vert1 = vec3(1.0);
	vec3 vert2 = vec3(1.0);
	
	if(isEdge(1, 2)){
		vert1.x = 0.0;
		vert2.x = 0.0;
	}
	
	if(isEdge(0, 2)){
		vert0.y = 0.0;
		vert2.y = 0.0;
	}
	
	if(isEdge(0, 1)){
		vert0.z = 0.0;
		vert1.z = 0.0;
	}
	
	vertEdge = vert0;
	vertBary = vec3(1.0, 0.0, 0.0);
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	vertEdge = vert1;
	vertBary = vec3(0.0, 1.0, 0.0);
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	vertEdge = vert2;
	vertBary = vec3(0.0, 0.0, 1.0);
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	EndPrimitive();
}