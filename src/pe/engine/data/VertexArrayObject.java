package pe.engine.data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public class VertexArrayObject implements DisposableResource{
	
	private int id;
	private List<BufferObject> bufferObjects;
	
	public VertexArrayObject(){
		GLVersion.checkAfter(PE.GL_VERSION_30);
		this.id = GL30.glGenVertexArrays();
		this.bufferObjects = new ArrayList<BufferObject>();
		
		Resources.add(this);
	}
	
	public void add(BufferObject bo){
		bufferObjects.add(bo);
	}
	
	public void remove(BufferObject bo){
		bufferObjects.remove(bo);
	}
	
	public void use(){
		GL30.glBindVertexArray(id);
	}
	
	public void unlink(){
		GL30.glBindVertexArray(0);
	}
	
	public void useVBO(int id){
		bufferObjects.get(id).use();
	}

	public void dispose() {
		GL30.glDeleteVertexArrays(id);
	}
	
	public int getID(){
		return id;
	}
}
