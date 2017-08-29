package pe.engine.data;

public abstract class BufferObject implements DisposableResourceI {

	protected int id;
	protected boolean bound;

	public abstract void bind();

	public abstract void unbind();

	public abstract void dispose();

	public boolean isBound() {
		return bound;
	}

	public int getID() {
		return id;
	}

	protected void checkBound() {
		if (!bound)
			throw new IllegalStateException(
					"This buffer object (" + this.getClass().getSimpleName() + ") needs to be bound before use.");
	}
}
