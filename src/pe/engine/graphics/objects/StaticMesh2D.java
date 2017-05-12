package pe.engine.graphics.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import pe.engine.main.PE;
import pe.util.math.Vec2f;

public class StaticMesh2D extends Mesh{

	public StaticMesh2D(Vec2f[] vertices, int[] indices){
		super(2, PE.STATIC_MESH_2D, indices.length);
		
		try (MemoryStack stack = MemoryStack.stackPush()){
			FloatBuffer vertecesBuffer = stack.mallocFloat(2 * vertices.length);
			for(Vec2f vertex:vertices){
				vertex.putInBuffer(vertecesBuffer);
			}
			vertecesBuffer.flip();
			
			vbo.use();
			vbo.setData(vertecesBuffer);
			vbo.unbind();
			
			IntBuffer indecesBuffer = stack.mallocInt(indices.length);
			indecesBuffer.put(indices);
			indecesBuffer.flip();
			
			ebo.use();
			ebo.setData(indecesBuffer);
			ebo.unbind();
		}
	}
}
