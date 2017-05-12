package pe.engine.graphics.objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import pe.engine.data.ElementBufferObject;
import pe.engine.data.VertexBufferObject;

public abstract class Mesh {
	
	protected VertexBufferObject vbo;
	protected ElementBufferObject ebo;
	protected List<Integer> enabledVertexAttribs;
	protected int meshType;
	protected int vertexCount;
	
	protected Mesh(int dimension, int meshType, int vertexCount){
		this.vbo = new VertexBufferObject(dimension);
		this.ebo = new ElementBufferObject();
		this.enabledVertexAttribs = new ArrayList<Integer>();
		this.meshType = meshType;
		this.vertexCount = vertexCount;
		
		enabledVertexAttribs.add(0); // Enable position vectors
		enabledVertexAttribs.add(1); // Enable vertex indeces
	}
	
	public int getMeshType(){
		return meshType;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public void render() {
		vbo.use();
		enableVertexAttribs();
		GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
		disableVertexAttribs();
		vbo.unbind();
	}
	
	protected void enableVertexAttribs(){
		for(int attribID:enabledVertexAttribs){
			GL20.glEnableVertexAttribArray(attribID);
		}
	}
	
	protected void disableVertexAttribs(){
		for(int attribID:enabledVertexAttribs){
			GL20.glDisableVertexAttribArray(attribID);
		}
	}
}
