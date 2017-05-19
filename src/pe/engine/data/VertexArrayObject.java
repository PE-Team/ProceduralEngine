package pe.engine.data;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public class VertexArrayObject implements DisposableResource{
	
	private int id;
	private ElementBufferObject ebo = null;
	private Set<VertexBufferObject> vbos;
	
	public VertexArrayObject(){
		GLVersion.checkAfter(PE.GL_VERSION_30);
		this.id = GL30.glGenVertexArrays();
		this.vbos = new HashSet<VertexBufferObject>();
		
		Resources.add(this);
	}
	
	public void use(){
		GL30.glBindVertexArray(id);
	}
	
	public void unbind(){
		GL30.glBindVertexArray(0);
	}
	
	public void enableVBOLocation(int index){
		GL20.glEnableVertexAttribArray(index);
	}
	
	public void addVBO(int dimension, FloatBuffer data){
		VertexBufferObject vbo = new VertexBufferObject(dimension);
		vbo.use();
		vbo.setData(data);
		vbo.setLocation(vbos.size());
		this.enableVBOLocation(vbos.size());
		vbos.add(vbo);
	}
	
	public void addEBO(IntBuffer indices){
		ebo = new ElementBufferObject();
		ebo.use();
		ebo.setData(indices);
	}

	public void dispose() {
		GL30.glDeleteVertexArrays(id);
	}
	
	public int getID(){
		return id;
	}
}
