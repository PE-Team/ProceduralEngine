package pe.engine.shader.main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.main.GLVersion;
import pe.engine.main.PE;
import pe.util.Util;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec3f;
import pe.util.math.Vec4f;

public class ShaderProgram implements DisposableResource {

	private int id;
	private boolean compiled = false;
	private byte attachedTypes = 0b0000;

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

	/**
	 * Will set where in the currently used Attribute Array
	 * (<code>VertexBufferObject</code>) each attribute is. The attribute ID is
	 * automatically retrieved. This is used for the 'in' values for the shader.
	 * 
	 * @param name
	 *            The name of the attribute in the shader itself.
	 * @param normalized
	 *            Whether the attribute has been normalized. Most often false.
	 * @param offset
	 *            The offset of the data in the Attribute Array
	 * @param vboWidth
	 *            The width of the Attribute Array, which is to say the number
	 *            of data elements in each row of the array.
	 * 
	 * @see #setAttribVec2f(int, String, boolean, int, int)
	 * @see pe.engine.data.VertexBufferObject
	 * 
	 * @since 1.0
	 */
	public void setAttribVec2f(String name, boolean normalized, int offset, int vboWidth) {
		int attribID = GL20.glGetAttribLocation(id, name);
		GL20.glEnableVertexAttribArray(attribID);
		GL20.glVertexAttribPointer(attribID, 2, GL11.GL_FLOAT, normalized, vboWidth * Util.FLOAT_BYTE_SIZE,
				offset * Util.FLOAT_BYTE_SIZE);
	}

	public void setAttribVec3f(String name, boolean normalized, int offset, int vboWidth) {
		int attribID = GL20.glGetAttribLocation(id, name);
		GL20.glEnableVertexAttribArray(attribID);
		GL20.glVertexAttribPointer(attribID, 3, GL11.GL_FLOAT, normalized, vboWidth * Util.FLOAT_BYTE_SIZE,
				offset * Util.FLOAT_BYTE_SIZE);
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
	 * @param vec
	 *            The vector you wish to set the uniform to.
	 * 
	 * @since 1.0
	 */
	public void setUniformVec3f(String name, Vec3f vec) {
		int uniformId = GL20.glGetUniformLocation(id, name);
		FloatBuffer buffer = vec.putInBuffer(BufferUtils.createFloatBuffer(3));
		buffer.flip();
		GL20.glUniform3fv(uniformId, buffer);
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
		FloatBuffer buffer = vec.putInBuffer(BufferUtils.createFloatBuffer(4));
		buffer.flip();
		GL20.glUniform4fv(uniformId, buffer);
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
		FloatBuffer buffer = color.putInBuffer4(BufferUtils.createFloatBuffer(4));
		buffer.flip();
		GL20.glUniform4fv(uniformId, buffer);
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
		FloatBuffer buffer = matrix.putInBuffer(BufferUtils.createFloatBuffer(9));
		buffer.flip();
		GL20.glUniformMatrix3fv(uniformID, false, buffer);
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
		FloatBuffer buffer = matrix.putInBuffer(BufferUtils.createFloatBuffer(16));
		buffer.flip();
		GL20.glUniformMatrix4fv(uniformID, false, buffer);
	}

	public int getID() {
		return id;
	}

	public void use() {
		GL20.glUseProgram(id);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void dispose() {
		GL20.glDeleteShader(id);
	}
}
