package pe.engine.data;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;

import pe.engine.main.GLVersion;
import pe.engine.main.PE;

public class ElementBufferObject extends BufferObject {

	public ElementBufferObject() {
		GLVersion.checkAfter(PE.GL_VERSION_15);
		this.id = GL15.glGenBuffers();

		Resources.add(this);
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
	}

	public void setData(IntBuffer data) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}

	public void setData(IntBuffer data, boolean mutable) {
		if (mutable) {
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_DYNAMIC_DRAW);
		} else {
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		}
	}

	public void setData(IntBuffer data, boolean mutable, boolean highUsage, boolean readOnly, boolean forDrawing) {
		int usage;
		if (mutable && highUsage) {
			// DYNAMIC
			if (readOnly && forDrawing) {
				// COPY
				usage = GL15.GL_DYNAMIC_COPY;
			} else if (readOnly && !forDrawing) {
				// READ
				usage = GL15.GL_DYNAMIC_READ;
			} else {
				// DRAW
				usage = GL15.GL_DYNAMIC_DRAW;
			}
		} else if (!mutable && highUsage) {
			// STATIC
			if (readOnly && forDrawing) {
				// COPY
				usage = GL15.GL_STATIC_COPY;
			} else if (readOnly && !forDrawing) {
				// READ
				usage = GL15.GL_STATIC_READ;
			} else {
				// DRAW
				usage = GL15.GL_STATIC_DRAW;
			}
		} else {
			// STREAM
			if (readOnly && forDrawing) {
				// COPY
				usage = GL15.GL_STREAM_COPY;
			} else if (readOnly && !forDrawing) {
				// READ
				usage = GL15.GL_STREAM_READ;
			} else {
				// DRAW
				usage = GL15.GL_STREAM_DRAW;
			}
		}

		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage);
	}

	@Override
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void dispose() {
		GL15.glDeleteBuffers(id);
	}
}
