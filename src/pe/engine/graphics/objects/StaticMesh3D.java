package pe.engine.graphics.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import pe.engine.main.PE;
import pe.util.math.Vec3f;

public class StaticMesh3D extends Mesh{
	
	public StaticMesh3D(Vec3f[] vertices, int[] indices){
		super(3, PE.STATIC_MESH_3D, indices.length);
		
		try (MemoryStack stack = MemoryStack.stackPush()){
			FloatBuffer vertecesBuffer = stack.mallocFloat(3 * vertices.length);
			for(Vec3f vertex:vertices){
				vertex.putInBuffer(vertecesBuffer);
			}
			vertecesBuffer.flip();
			
			vertexVBO.use();
			vertexVBO.setData(vertecesBuffer);
			vertexVBO.unbind();
			
			IntBuffer indecesBuffer = stack.mallocInt(indices.length);
			indecesBuffer.put(indices);
			indecesBuffer.flip();
			
			indicesEBO.use();
			indicesEBO.setData(indecesBuffer);
			indicesEBO.unbind();
		}
	}
}
