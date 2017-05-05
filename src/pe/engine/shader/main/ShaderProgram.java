package pe.engine.shader.main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import pe.engine.main.PE;
import peu.util.math.Mat4f;

public class ShaderProgram implements DisposableResource{
	
	private int id;
	private boolean compiled = false;
	private byte attachedTypes = 0b0000;

	public ShaderProgram(){
		GLVersion.checkAfter(PE.GL_VERSION_20);
		this.id = GL20.glCreateProgram();
		
		Resources.add(this);
	}
	
	public void addShader(Shader shader){
		switch(shader.getShaderType()){
		case PE.SHADER_TYPE_VERTEX:
			if((attachedTypes & 0b0001) == 0b0001)
				throw new IllegalArgumentException("Cannot attach more than one Vertex Shader to the same Shader Program.");
			attachedTypes |= 0b0001;
			break;
		case PE.SHADER_TYPE_FRAGMENT:
			if((attachedTypes & 0b0010) == 0b0010)
				throw new IllegalArgumentException("Cannot attach more than one Fragment Shader to the same Shader Program.");
			attachedTypes |= 0b0010;
			break;
		case PE.SHADER_TYPE_GEOMETRY:
			if((attachedTypes & 0b0100) == 0b0100)
				throw new IllegalArgumentException("Cannot attach more than one Geometry Shader to the same Shader Program.");
			attachedTypes |= 0b0100;
			break;
		}
		GL20.glAttachShader(id, shader.getID());
	}
	
	public void compile(){
		GL20.glLinkProgram(id);
		compiled = true;
	}
	
	public void compileStatus(){
		int status =  GL20.glGetProgrami(id, GL20.GL_LINK_STATUS);
		if (status != GL11.GL_TRUE){
			StringBuilder sb = new StringBuilder();
			sb.append("An error occured while compiling a Shader Program");
			if(!compiled) sb.append("The Shader Program was not compiled.\nPlease call '.compile()' before checking the compile status.");
			sb.append(GL20.glGetShaderInfoLog(id)).append('\n');
			sb.append(Thread.currentThread().getStackTrace());
		    throw new RuntimeException(sb.toString());
		}
	}
	
	public void setDefaultFragOutValue(String name, int value){
		GLVersion.checkAfter(PE.GL_VERSION_30);
		if((attachedTypes & 0b0010) != 0b0010)
			throw new IllegalStateException("A Fragment Shader has not been added to this Shader Program so there is not default Fragment out value to set.");
		GL30.glBindFragDataLocation(id, value, name);
	}
	
	public void setAttribVec3f(int attribID, String name, boolean normalized, int offset, int vboWidth){
		GL20.glEnableVertexAttribArray(attribID);
		GL20.glVertexAttribPointer(attribID, 3, GL11.GL_FLOAT, normalized, vboWidth * PE.FLOAT_BYTE_SIZE, offset * PE.FLOAT_BYTE_SIZE);
	}
	
	public void setAttribVec3f(String name, boolean normalized, int offset, int vboWidth){
		setAttribVec3f(GL20.glGetAttribLocation(id, name), name, normalized, offset, vboWidth);
	}
	
	public void setUniformMat4f(String name, Mat4f matrix){
		int uniformID = GL20.glGetUniformLocation(id, name);
		GL20.glUniformMatrix4fv(uniformID, false, matrix.toFloatBuffer());
	}
	
	public int getID(){
		return id;
	}
	
	public void use(){
		GL20.glUseProgram(id);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void dispose(){
		GL20.glDeleteShader(id);
	}
}
