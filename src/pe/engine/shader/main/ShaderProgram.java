package pe.engine.shader.main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.data.TextureArrayObject;
import pe.engine.graphics.objects.Texture;
import pe.engine.main.GLVersion;
import pe.engine.main.PE;
import pe.util.color.Color;
import pe.util.math.Mat2f;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec3f;
import pe.util.math.Vec4f;

public class ShaderProgram implements DisposableResourceI {

	private int id;
	private boolean compiled = false;
	private byte attachedTypes = 0b0000;

	private FloatBuffer bufferVec2f = BufferUtils.createFloatBuffer(2);
	private FloatBuffer bufferVec3f = BufferUtils.createFloatBuffer(3);
	private FloatBuffer bufferVec4f = BufferUtils.createFloatBuffer(4);

	private FloatBuffer bufferMat2f = BufferUtils.createFloatBuffer(4);
	private FloatBuffer bufferMat3f = BufferUtils.createFloatBuffer(9);
	private FloatBuffer bufferMat4f = BufferUtils.createFloatBuffer(16);

	public ShaderProgram() {
		GLVersion.checkAfter(PE.GL_VERSION_20);
		this.id = GL20.glCreateProgram();

		Resources.add(this);
	}

	public void addShader(Shader shader) {
		switch (shader.getShaderType()) {
		case PE.SHADER_TYPE_VERTEX:
			if ((attachedTypes & 0b0001) == 0b0001)
				throw new IllegalArgumentException(
						"Cannot attach more than one Vertex Shader to the same Shader Program.");
			attachedTypes |= 0b0001;
			break;
		case PE.SHADER_TYPE_FRAGMENT:
			if ((attachedTypes & 0b0010) == 0b0010)
				throw new IllegalArgumentException(
						"Cannot attach more than one Fragment Shader to the same Shader Program.");
			attachedTypes |= 0b0010;
			break;
		case PE.SHADER_TYPE_GEOMETRY:
			if ((attachedTypes & 0b0100) == 0b0100)
				throw new IllegalArgumentException(
						"Cannot attach more than one Geometry Shader to the same Shader Program.");
			attachedTypes |= 0b0100;
			break;
		}
		GL20.glAttachShader(id, shader.getID());
	}

	public void compile() {
		GL20.glLinkProgram(id);
		compiled = true;
	}

	public void compileStatus() {
		int status = GL20.glGetProgrami(id, GL20.GL_LINK_STATUS);
		if (status != GL11.GL_TRUE) {
			StringBuilder sb = new StringBuilder();
			sb.append("An error occured while compiling a Shader Program");
			if (!compiled)
				sb.append(
						"The Shader Program was not compiled.\nPlease call '.compile()' before checking the compile status.");
			sb.append(GL20.glGetShaderInfoLog(id)).append('\n');
			sb.append(Thread.currentThread().getStackTrace());
			throw new RuntimeException(sb.toString());
		}
	}

	public void setDefaultFragOutValue(String name, int value) {
		GLVersion.checkAfter(PE.GL_VERSION_30);
		if ((attachedTypes & 0b0010) != 0b0010)
			throw new IllegalStateException(
					"A Fragment Shader has not been added to this Shader Program so there is not default Fragment out value to set.");
		GL30.glBindFragDataLocation(id, value, name);
	}

	public void setAttribIndex(int index, String name) {
		GL20.glBindAttribLocation(id, index, name);
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param value
	 *            The integer value you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformInt(String name, int value) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform1i(uniformId, value);
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param value
	 *            The float value you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformFloat(String name, float value) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform1f(uniformId, value);
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param vec
	 *            The vector you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformVec2f(String name, Vec2f vec) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform2fv(uniformId, vec.putInBufferC(bufferVec2f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param vec
	 *            The vector you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformVec3f(String name, Vec3f vec) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform3fv(uniformId, vec.putInBufferC(bufferVec3f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param vec
	 *            The vector you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformVec4f(String name, Vec4f vec) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform4fv(uniformId, vec.putInBufferC(bufferVec4f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader. Color is considered the same as a
	 * <code>vec4</code>.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param color
	 *            The color you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformColor(String name, Color color) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		GL20.glUniform4fv(uniformId, color.putInBuffer4C(bufferVec4f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param matrix
	 *            The matrix you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformMat2f(String name, Mat2f matrix) {
		int uniformID = GL20.glGetUniformLocation(id, name);
		GL20.glUniformMatrix2fv(uniformID, false, matrix.putInBufferC(bufferMat2f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param matrix
	 *            The matrix you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformMat3f(String name, Mat3f matrix) {
		int uniformID = GL20.glGetUniformLocation(id, name);
		GL20.glUniformMatrix3fv(uniformID, false, matrix.putInBufferC(bufferMat3f));
	}

	/**
	 * Sets the value of the uniform with the specified <code>name</code> in the
	 * currently used shader.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param matrix
	 *            The matrix you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformMat4f(String name, Mat4f matrix) {
		int uniformID = GL20.glGetUniformLocation(id, name);
		GL20.glUniformMatrix4fv(uniformID, false, matrix.putInBufferC(bufferMat4f));
	}

	/**
	 * Pairs the given texture with the shader variable name.
	 * 
	 * @param name
	 *            The name of the uniform in the shader file.
	 * @param texture
	 *            The texture to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformTexture(String name, Texture texture) {
		int uniformID = GL20.glGetUniformLocation(id, name);
		GL20.glUniform1i(uniformID, texture.getLocation());
	}

	/**
	 * Pairs all the textures located in a TextureArrayObject with their uniform
	 * variables. The strings in <code>names</code> must match up to the
	 * ordering of the Textures in the TAO. The TAO is iterated through and uses
	 * <code>setUniformTexture()</code> to set the uniform, so the ordering in
	 * the TAO does not matter, only that it coincides with the ordering of
	 * <code>names</code>.
	 * 
	 * @param names
	 *            The names of the uniforms to set the textures of. Must be in
	 *            the same ordering as the TAO.
	 * @param tao
	 *            The array of textures to set the uniform variables to. Must be
	 *            in the same ordering as the given uniform names.
	 * 
	 * @see #setUniformTexture(String, Texture)
	 * 
	 * @since 1.0
	 */
	public void setUniformTAO(String[] names, TextureArrayObject tao) {
		if (names.length != tao.length())
			throw new IllegalArgumentException(
					"There must be an equal number of uniform names to textures in the TAO.");

		for (int i = 0; i < names.length; i++) {
			setUniformTexture(names[i], tao.getTexture(i));
		}
	}

	/**
	 * Pairs all of the textures in a <code>TextureArrayObject</code> with its
	 * respective uniform name. The index of the uniform name in
	 * <code>name</code> should be the same as the location of the texture it
	 * corresponds with.
	 * 
	 * @param name
	 *            An array of the uniform names in the shader file.
	 * @param tao
	 *            The <code>TextureArrayObject</code> whose textures are to be
	 *            paired with their uniforms.
	 */
	public void setUniformTextureArrayObject(String[] name, TextureArrayObject tao) {
		if (name.length != tao.length())
			throw new IllegalArgumentException(
					"There must be an equal amount of names and textures in order to set their uniforms.");

		for (Texture texture : tao.getTextures()) {
			setUniformTexture(name[texture.getLocation()], texture);
		}
	}

	public int getID() {
		return id;
	}

	public void start() {
		GL20.glUseProgram(id);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void dispose() {
		GL20.glDeleteShader(id);
	}
}
