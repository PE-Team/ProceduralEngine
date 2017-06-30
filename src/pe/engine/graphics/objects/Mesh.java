package pe.engine.graphics.objects;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import pe.engine.data.VertexArrayObject;
import pe.util.math.Vec2f;

public abstract class Mesh implements RenderableI{
	
	protected VertexArrayObject vao;
	protected int meshDataLocation = -1;
	protected int meshType;
	protected int indicesCount;
	protected int verticesCount;
	protected int dimension;
	protected boolean wireframe;
	
	protected Mesh(int meshType, int indicesCount, int verticesCount, int dimension){
		this.vao = new VertexArrayObject();
		this.meshType = meshType;
		this.indicesCount = indicesCount;
		this.verticesCount = verticesCount;
		this.dimension = dimension;
		this.wireframe = false;
	}
	
	public void setVertexData(Vec2f[] vertices){
		try (MemoryStack stack = MemoryStack.stackPush()) {
			vao.bind();

			FloatBuffer vertecesBuffer = stack.mallocFloat(dimension * vertices.length);
			for (Vec2f vertex : vertices) {
				vertex.putInBuffer(vertecesBuffer);
			}
			vertecesBuffer.flip();
			
			if(meshDataLocation == -1){
				vao.addVBO(dimension, vertecesBuffer);
			}else{
				vao.setVBO(dimension, vertecesBuffer, meshDataLocation);
			}

			vao.unbind();
		}
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
		vao.bind();
		vao.addVBO(dimension, data);
		vao.unbind();
	}
	
	public void render() {
		vao.bind();
		if(wireframe)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		if(wireframe)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		vao.unbind();
	}
}
