package pe.engine.graphics.objects;

public interface BatchRenderableI {

	public abstract void batchStart();
	
	public abstract void batchRender();
	
	public abstract void batchStop();
}
