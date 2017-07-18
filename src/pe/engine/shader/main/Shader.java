package pe.engine.shader.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public class Shader implements DisposableResourceI{
	
	private int type;
	private String path;
	private int id;
	private boolean compiled = false;

	public Shader(int shaderType, String shaderPath){
		testVersion(shaderType);
		this.type = shaderType;
		this.path = shaderPath;
		
		this.id = GL20.glCreateShader(getGLShaderType(type));
		
		Resources.add(this);
	}
	
	public static int getGLShaderType(int PEShaderType){
		switch(PEShaderType){
		case PE.SHADER_TYPE_VERTEX:
			return GL20.GL_VERTEX_SHADER;
		case PE.SHADER_TYPE_FRAGMENT:
			return GL20.GL_FRAGMENT_SHADER;
		case PE.SHADER_TYPE_GEOMETRY:
			return GL32.GL_GEOMETRY_SHADER;
		}
		return PE.NULL;
	}
	
	public int getID(){
		return id;
	}
	
	public void setType(int shaderType){
		testVersion(shaderType);
		this.type = shaderType;
		
		this.id = GL20.glCreateShader(getGLShaderType(type));
	}
	
	public int getShaderType(){
		return type;
	}
	
	private void testVersion(int shaderType){
		if(GLVersion.isBefore(PE.GL_VERSION_20))
			throw new IllegalStateException("You cannot create a Shader with an OpenGL version before 2.0.\nTry updating your graphics drivers.");
		if(GLVersion.isBefore(PE.GL_VERSION_32) && shaderType == PE.SHADER_TYPE_GEOMETRY)
			throw new IllegalArgumentException("You cannot create a geometry shader with an OpenGL version before 3.2.\nTry updating your graphics drivers.");
	}
	
	public void setPath(String shaderPath){
		this.path = shaderPath;
		this.compiled = false;
	}
	
	public void compile(){
		GL20.glShaderSource(id, getShaderContent());
		GL20.glCompileShader(id);
		compiled = true;
	}
	
	public void compileStatus(){
		int status =  GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS);
		if (status != GL11.GL_TRUE){
			StringBuilder sb = new StringBuilder();
			sb.append("An error occured in compiling a ").append(getShaderTypeName()).append('\n');
			if(!compiled) sb.append("The ").append(getShaderTypeName()).append(" for the file: '").append(path).append("' was not compiled.\nPlease call '.compile()' before checking the compile status.\nYou will need to compile each time the shader is modified at runtime or when the path is changed.");
			sb.append(GL20.glGetShaderInfoLog(id));
		    throw new RuntimeException(sb.toString());
		}
	}
	
	public String getShaderTypeName(){
		switch(type){
		case PE.SHADER_TYPE_VERTEX:
			return "Vertex Shader";
		case PE.SHADER_TYPE_GEOMETRY:
			return "Geometry Shader";
		case PE.SHADER_TYPE_FRAGMENT:
			return "Fragment Shader";
		}
		return "NULL";
	}
	
	private String getShaderContent(){
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void dispose(){
		GL20.glDeleteShader(id);
	}
}
