package pe.engine.graphics.main;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.main.PE;

public class WindowFrameSizeHandler extends GLFWFramebufferSizeCallback implements DisposableResource{
	
	private static final int[] sizeUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	
	private Window window;
	
	public WindowFrameSizeHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long windowID, int width, int height) {
		if (window == null || window.getID() != windowID)
			return;

		window.setSizeValue(width, height, sizeUnits);
		window.update();
	}
	
	public void setWindow(Window window){
		this.window = window;
	}

	@Override
	public void dispose() {
		free();
	}
}
