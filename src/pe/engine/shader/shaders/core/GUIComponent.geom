#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
	vec2 border;
} gs_in[];

out vec2 border;

flat out vec2 borderWidthMod;

float distToLine(vec2 point, vec2 linePoint1, vec2 linePoint2){
	float top = abs( (linePoint2.y - linePoint1.y)*point.x - (linePoint2.x - linePoint1.x)*point.y + linePoint2.x*linePoint1.y - linePoint2.y*linePoint1.x);
	float bottom = sqrt( pow(linePoint2.y - linePoint1.y, 2) + pow(linePoint2.x - linePoint1.x, 2) );
	return top / bottom;
}

void main() {
	vec2 vertex0 = gl_in[0].gl_Position.xy;
	vec2 vertex1 = gl_in[1].gl_Position.xy;
	vec2 vertex2 = gl_in[2].gl_Position.xy;
	
	vec2 borderWMod = vec2(0.0, 0.0);
	
	border = gs_in[0].border;
	if(gs_in[0].border.x > 0.5){
		float dist = distToLine(vertex0, vertex1, vertex2);
		border *= dist;
		borderWMod.x = dist;
	}else if(gs_in[0].border.y > 0.5){
		float dist = distToLine(vertex0, vertex1, vertex2);
		border *= dist;
		borderWMod.y = dist;
	}
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	border = gs_in[1].border;
	if(gs_in[1].border.x > 0.5){
		float dist = distToLine(vertex1, vertex0, vertex2);
		border *= dist;
		borderWMod.x = dist;
	}else if(gs_in[1].border.y > 0.5){
		float dist = distToLine(vertex1, vertex0, vertex2);
		border *= dist;
		borderWMod.y = dist;
	}
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	border = gs_in[2].border;
	if(gs_in[2].border.x > 0.5){
		float dist = distToLine(vertex2, vertex1, vertex0);
		border *= dist;
		borderWMod.x = dist;
	}else if(gs_in[2].border.y > 0.5){
		float dist = distToLine(vertex2, vertex1, vertex0);
		border *= dist;
		borderWMod.y = dist;
	}
	borderWidthMod = borderWMod;
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	EndPrimitive();
}