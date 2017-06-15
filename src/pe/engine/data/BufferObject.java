package pe.engine.data;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public abstract class BufferObject implements DisposableResource {

	protected int id;

	public abstract void bind();

	public void unbind(){
		GLVersion.checkAfter(PE.GL_VERSION_30);
		GL30.glBindVertexArray(0);
	}

	public void dispose(){
		GL15.glDeleteBuffers(id);
	}

	public int getID() {
		return id;
	}
}
