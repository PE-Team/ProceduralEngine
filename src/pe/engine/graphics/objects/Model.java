package pe.engine.graphics.objects;

import pe.engine.shader.main.ShaderProgram;

public class Model {

	protected Mesh mesh;
	protected ShaderProgram shaderProgram;

	public Model() {
	}
	
	public void setMesh(Mesh mesh){
		this.mesh = mesh;
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram){
		this.shaderProgram = shaderProgram;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public void render() {
		shaderProgram.use();
		mesh.render();
		shaderProgram.stop();
	}
}
