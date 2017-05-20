package pe.engine.graphics.objects;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import pe.engine.data.VertexArrayObject;

public abstract class Mesh {
	
	protected VertexArrayObject vao;
	protected int meshType;
	protected int indicesCount;
	protected int verticesCount;
	protected boolean wireframe;
	
	protected Mesh(int meshType, int indicesCount, int verticesCount){
		this.vao = new VertexArrayObject();
		this.meshType = meshType;
		this.indicesCount = indicesCount;
		this.verticesCount = verticesCount;
		this.wireframe = false;
	}
	
	public void setWireframe(boolean wireframe){
		this.wireframe = wireframe;
	}
	
	public int getMeshType(){
		return meshType;
	}
	
	public int getIndicesCout(){
		return indicesCount;
	}
	
	public int getVerticesCount(){
		return verticesCount;
	}
	
	public void addShaderAttrib(int dimension, FloatBuffer data){
		vao.use();
		vao.addVBO(dimension, data);
		vao.unbind();
	}
	
	public void render() {
		vao.use();
		if(wireframe)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		if(wireframe)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		vao.unbind();
	}
}
