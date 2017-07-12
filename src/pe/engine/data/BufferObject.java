package pe.engine.data;

public abstract class BufferObject implements DisposableResourceI {

	protected int id;

	public abstract void bind();

	public abstract void unbind();

	public abstract void dispose();

	public int getID() {
		return id;
	}
}
