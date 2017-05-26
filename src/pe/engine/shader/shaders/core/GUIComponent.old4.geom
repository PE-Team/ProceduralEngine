#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
	vec2 vertexIn;
	vec2 vertexOut;
} gs_in[];

out vec3 vertIn;
out vec3 vertOut;

// Scale is opposite side / left side
float vertInValue(vec2 leftSide, vec2 rightSide, vec2 borderDir, float scale){
	if(	borderDir.x * rightSide.y - borderDir.y * rightSide.x == 0.0){
		return -1.0;
	}
	
	if(	borderDir.x * leftSide.y - borderDir.y * leftSide.x == 0.0){
		return -2.0;
	}
	
	mat2 matrix = mat2(borderDir, rightSide);
	vec2 answer = inverse(matrix) * leftSide;
	return abs(scale/answer.y);
}

// Scale is opposite side / left side
float vertOutValue(vec2 leftSide, vec2 rightSide, vec2 borderDir, float scale){
	if(borderDir.x * rightSide.y - borderDir.y * rightSide.x == 0.0){
		return -1.0;
	}
	
	mat2 matrix = mat2(borderDir, rightSide);
	vec2 answer = inverse(matrix) * leftSide;
	return 1 - abs(scale/answer.y);
}

bool clockwise(vec2 v1, vec2 v2, vec2 v3){
	return (v2.x - v1.x) * (v2.y + v1.y) + (v3.x - v2.x) * (v3.y + v2.y) + (v1.x - v3.x) * (v1.y + v3.y) > 0;
}

void main() {
	int index0 = 0;
	int index1 = 1;
	int index2 = 2;
	
	if(!clockwise(gl_in[0].gl_Position.xy, gl_in[1].gl_Position.xy, gl_in[2].gl_Position.xy)){
		index1 = 2;
		index2 = 1;
	}
	
	vec4 vertex0 = gl_in[index0].gl_Position;
	vec4 vertex1 = gl_in[index1].gl_Position;
	vec4 vertex2 = gl_in[index2].gl_Position;
	
	vec3 vertIn0 = vec3(1.0, 0.0, 0.0);
	vec3 vertIn1 = vec3(0.0, 1.0, 0.0);
	vec3 vertIn2 = vec3(0.0, 0.0, 1.0);
	
	vec3 vertOut0 = vec3(1.0, 0.0, 0.0);
	vec3 vertOut1 = vec3(0.0, 1.0, 0.0);
	vec3 vertOut2 = vec3(0.0, 0.0, 1.0);
	
	vec2 side01 = normalize(vertex1 - vertex0).xy;
	vec2 side12 = normalize(vertex2 - vertex1).xy;
	vec2 side20 = normalize(vertex0 - vertex2).xy;
	
	float dist01 = distance(vertex1, vertex0);
	float dist12 = distance(vertex2, vertex1);
	float dist20 = distance(vertex0, vertex2);
	
	vec2 inDir0 = normalize(gs_in[index0].vertexIn);
	vec2 inDir1 = normalize(gs_in[index1].vertexIn);
	vec2 inDir2 = normalize(gs_in[index2].vertexIn);
	
	vec2 outDir0 = normalize(gs_in[index0].vertexOut);
	vec2 outDir1 = normalize(gs_in[index1].vertexOut);
	vec2 outDir2 = normalize(gs_in[index2].vertexOut);

	
	
	// IN VERTEX 1 (y)
	vertIn0.y = 0.0;
	vertIn1.y = vertInValue(side12, side20, inDir1, dist20/dist12);
	vertIn2.y = 1.0;
	
	if(vertIn0.y == -1.0){
		vertIn0.y = 0.0;
		vertIn1.y = 0.0;
		vertIn2.y = 0.0;
	}
	
	if(vertIn0.y == -2.0){
		vertIn0.y = 0.0;
		vertIn1.y = 0.0;
		vertIn2.y = 0.0;
	}
	
	// IN VERTEX 2 (z)
	vertIn0.z = 1.0;
	vertIn1.z = vertInValue(side20, side01, inDir2, dist01/dist20);
	vertIn2.z = 0.0;
	
	if(vertIn1.z == -1.0){
		vertIn0.z = 0.0;
		vertIn1.z = 0.0;
		vertIn2.z = 0.0;
	}
	
	if(vertIn1.z == -2.0){
		vertIn0.z = 0.0;
		vertIn1.z = 0.0;
		vertIn2.z = 0.0;
	}
	
	// OUT VERTEX 0 (x)
	vertOut0.x = vertOutValue(side12, side20, outDir0, dist20/dist12);
	vertOut1.x = 0.0;
	vertOut2.x = 1.0;
	
	if(vertOut0.x == -1.0){
		vertOut0.x = 0.0;
		vertOut1.x = 1.0;
		vertOut2.x = 0.0;
	}
	
	// OUT VERTEX 1 (y)
	vertOut0.y = 1.0;
	vertOut1.y = vertOutValue(side20, side01, outDir1, dist01/dist20);
	vertOut2.y = 0.0;
	
	if(vertOut1.y == -1.0){
		vertOut0.y = 0.0;
		vertOut1.y = 0.0;
		vertOut2.y = 1.0;
	}
	
	// OUT VERTEX 2 (z)
	vertOut0.z = 0.0;
	vertOut1.z = 1.0;
	vertOut2.z = vertOutValue(side01, side12, outDir2, dist12/dist01);
	
	if(vertOut2.z == -1.0){
		vertOut0.z = 1.0;
		vertOut1.z = 0.0;
		vertOut2.z = 0.0;
	}
	
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
	gl_Position = vertex2;
	EmitVertex();
	
	EndPrimitive();
}