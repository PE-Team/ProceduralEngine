#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
	vec2 vertexIn;
	vec2 vertexOut;
	vec2 vertexIDs;
} gs_in[];

out vec3 vertIn;
out vec3 vertOut;
flat out vec3 sidesConnect;

vec2 project(vec2 direction, vec2 vector){
	return direction * (dot(direction, vector) / length(direction));
}

vec2 normal(vec2 direction){
	return normalize(vec2(-direction.y, direction.x));
}

vec3 vertValues(vec2 baseVert, vec2 vert1, vec2 vert2, vec2 borderDir){
	vec2 borderNorm = normal(borderDir);
	
	vec2 v1 = vert1 - baseVert;
	vec2 v2 = vert2 - baseVert;
	
	vec2 projV1 = project(borderNorm, v1);
	float projV1Sign = sign(dot(borderNorm, projV1));
	
	vec2 projV2 = project(borderNorm, v2);
	float projV2Sign = sign(dot(borderNorm, projV2));
	
	return vec3(0.0, length(projV1) * projV1Sign, length(projV2) * projV2Sign);
}

void main() {
	vec4 vertex0 = gl_in[0].gl_Position;
	vec4 vertex1 = gl_in[1].gl_Position;
	vec4 vertex2 = gl_in[2].gl_Position;

	vec3 vertIn0 = vec3(1.0, 0.0, 0.0);
	vec3 vertIn1 = vec3(0.0, 1.0, 0.0);
	vec3 vertIn2 = vec3(0.0, 0.0, 1.0);
	
	vec3 vertOut0 = vec3(1.0, 0.0, 0.0);
	vec3 vertOut1 = vec3(0.0, 1.0, 0.0);
	vec3 vertOut2 = vec3(0.0, 0.0, 1.0);
	
	vec3 connectedSides = vec3(0.0, 0.0, 0.0);
	
	vec2 inDir0 = normalize(gs_in[0].vertexIn);
	vec2 inDir1 = normalize(gs_in[1].vertexIn);
	vec2 inDir2 = normalize(gs_in[2].vertexIn);
	
	vec2 outDir0 = normalize(gs_in[0].vertexOut);
	vec2 outDir1 = normalize(gs_in[1].vertexOut);
	vec2 outDir2 = normalize(gs_in[2].vertexOut);
	
	// CALCULATING WHICH SIDES CONNECT {0 -> 1, 1 -> 2, 2 -> 0}
	if(gs_in[0].vertexIDs.x == gs_in[1].vertexIDs.y || gs_in[0].vertexIDs.y == gs_in[1].vertexIDs.x){
		connectedSides.x = 1.0;
	}
	if(gs_in[1].vertexIDs.x == gs_in[2].vertexIDs.y || gs_in[1].vertexIDs.y == gs_in[2].vertexIDs.x){
		connectedSides.y = 1.0;
	}
	if(gs_in[2].vertexIDs.x == gs_in[0].vertexIDs.y || gs_in[2].vertexIDs.y == gs_in[0].vertexIDs.x){
		connectedSides.z = 1.0;
	}
	
	// IN VERTEX 0 (x)
	vec3 inVert0 = vertValues(vertex0.xy, vertex1.xy, vertex2.xy, inDir0);
	vertIn0.x = inVert0.x;
	vertIn1.x = inVert0.y;
	vertIn2.x = inVert0.z;
	
	// IN VERTEX 1 (y)
	vec3 inVert1 = vertValues(vertex1.xy, vertex0.xy, vertex2.xy, inDir1);
	vertIn0.y = inVert1.y;
	vertIn1.y = inVert1.x;
	vertIn2.y = inVert1.z;
	
	// IN VERTEX 2 (z)
	vec3 inVert2 = vertValues(vertex2.xy, vertex1.xy, vertex0.xy, inDir2);
	vertIn0.z = inVert2.z;
	vertIn1.z = inVert2.y;
	vertIn2.z = inVert2.x;
	
	// OUT VERTEX 0 (x)
	vec3 outVert0 = vertValues(vertex0.xy, vertex1.xy, vertex2.xy, outDir0);
	vertOut0.x = outVert0.x;
	vertOut1.x = outVert0.y;
	vertOut2.x = outVert0.z;
	
	// OUT VERTEX 1 (y)
	vec3 outVert1 = vertValues(vertex1.xy, vertex0.xy, vertex2.xy, outDir1);
	vertOut0.y = outVert1.y;
	vertOut1.y = outVert1.x;
	vertOut2.y = outVert1.z;
	
	// OUT VERTEX 2 (z)
	vec3 outVert2 = vertValues(vertex2.xy, vertex1.xy, vertex0.xy, outDir2);
	vertOut0.z = outVert2.z;
	vertOut1.z = outVert2.y;
	vertOut2.z = outVert2.x;
	
	vertIn = vertIn0;
	vertOut = vertOut0;
	gl_Position = vertex0;
	EmitVertex();
	
	vertIn = vertIn1;
	vertOut = vertOut1;
	gl_Position = vertex1;
	EmitVertex();
	
	vertIn = vertIn2;
	vertOut = vertOut2;
	sidesConnect = connectedSides;
	gl_Position = vertex2;
	EmitVertex();
	
	EndPrimitive();
}