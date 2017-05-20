#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 23) out;

in VS_OUT {
	vec2 vertexIn;
	vec2 vertexOut;
} gs_in[];

out float drawBorder;

uniform vec2 windowDimensions;
uniform int borderRadius;
uniform int borderWidth;

vec2 bisector(vec2 v1, vec2 v2){
	return normalize(v1 + v2); 
}

vec2 clamp(vec2 max, vec2 min, vec2 vec){
	vec2 bisec = bisector(max, min);
	float minDot = dot(max, bisec);
	if(dot(vec, bisec) < minDot){
		if(dot(vec, max) > dot(vec, min)){
			return max;
		}else{
			return min;
		}
	}else{
		return vec;
	}
}

vec2 pixelLength(vec2 vec, int pixels){
	return 2 * pixels * vec2(vec.x / windowDimensions.x, vec.y / windowDimensions.y);
}

void main() {

	vec2 bisec0 = bisector(-gs_in[0].vertexIn, gs_in[0].vertexOut);
	vec2 bisec1 = bisector(-gs_in[1].vertexIn, gs_in[1].vertexOut);
	vec2 bisec2 = bisector(-gs_in[2].vertexIn, gs_in[2].vertexOut);
	
	vec2 edge1 = gl_in[1].gl_Position.xy - gl_in[0].gl_Position.xy;
	vec2 edge2 = gl_in[2].gl_Position.xy - gl_in[1].gl_Position.xy;
	vec2 edge3 = gl_in[0].gl_Position.xy - gl_in[2].gl_Position.xy;
	
	//bisec0 = clamp(normalize(-edge3), normalize(edge1), bisec0);
	//bisec1 = clamp(normalize(-edge1), normalize(edge2), bisec1);
	//bisec2 = clamp(normalize(-edge2), normalize(edge3), bisec2);
	
	vec2 edgePart00 = -edge3 / 3;
	vec2 edgePart02 = edge1 / 3;
	vec2 edgePart01 = pixelLength(bisec0, borderWidth);
	
	vec2 edgePart10 = -edge1 / 3;
	vec2 edgePart12 = edge2 / 3;
	vec2 edgePart11 = length((edgePart10 + edgePart12) / 2) * bisec1;
	
	vec2 edgePart20 = -edge2 / 3;
	vec2 edgePart22 = edge3 / 3;
	vec2 edgePart21 = length((edgePart20 + edgePart22) / 2) * bisec2;
	
	drawBorder = 0.0;
	
	// TEST
	drawBorder = 1.0;
	gl_Position = vec4(0.0, 0.0, 1.0, 1.0);
	EmitVertex();
	
	gl_Position = vec4(1.0, 0.0, 1.0, 1.0);
	EmitVertex();
	
	gl_Position = vec4(1.0, 1.0, 1.0, 1.0);
	EmitVertex();
	
	EndPrimitive();
	
	// MIDDLE TRIANGLE
	gl_Position = gl_in[0].gl_Position + vec4(edgePart01, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(edgePart11, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + vec4(edgePart21, 0.0, 0.0);
	EmitVertex();
	
	EndPrimitive();
	
	// BORDER START
	
	drawBorder = 1.0;
	
	// BOTTOM BORDER
	
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(edgePart11, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(edgePart12, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + vec4(edgePart21, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + vec4(edgePart20, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	EndPrimitive();
	
	// RIGHT BORDER
	
	gl_Position = gl_in[2].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + vec4(edgePart21, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[2].gl_Position + vec4(edgePart22, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position + vec4(edgePart01, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position + vec4(edgePart00, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	EndPrimitive();
	
	// LEFT BORDER
	
	gl_Position = gl_in[0].gl_Position;
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position + vec4(edgePart01, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position + vec4(edgePart02, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(edgePart11, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position + vec4(edgePart10, 0.0, 0.0);
	EmitVertex();
	
	gl_Position = gl_in[1].gl_Position;
	EmitVertex();
	
	EndPrimitive();
}