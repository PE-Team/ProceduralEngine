package pe.engine.data;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public class VertexArrayObject implements DisposableResource{
	
	private int id;
	private ElementBufferObject ebo = null;
	private int vbos = 0;
	
	public VertexArrayObject(){
		GLVersion.checkAfter(PE.GL_VERSION_30);
		this.id = GL30.glGenVertexArrays();
		
		Resources.add(this);
	}
	
	public void bind(){
		GL30.glBindVertexArray(id);
	}
	
	public void unbind(){
		GL30.glBindVertexArray(0);
	}
	
	public void enableVBOLocation(int index){
		GL20.glEnableVertexAttribArray(index);
	}
	
	public int addVBO(int dimension, FloatBuffer data){
		int location = vbos;
		VertexBufferObject vbo = new VertexBufferObject(dimension);
		vbo.bind();
		vbo.setData(data);
		vbo.setLocation(location);
		enableVBOLocation(vbos);
		vbos++;
		return location;
	}
	
	public void setVBO(int dimension, FloatBuffer data, int location){
		if(location > vbos)
			throw new IllegalArgumentException("Cannot put a VBO at a location farther than the number of VBOs in the VAO.");
		
		VertexBufferObject vbo = new VertexBufferObject(dimension);
		vbo.bind();
		vbo.setData(data);
		vbo.setLocation(location);
		enableVBOLocation(vbos);
	}
	
	public void addEBO(IntBuffer indices){
		ebo = new ElementBufferObject();
		ebo.bind();
		ebo.setData(indices);
	}

	public void dispose() {
		GL30.glDeleteVertexArrays(id);
	}
	
	public int getID(){
		return id;
	}
}
