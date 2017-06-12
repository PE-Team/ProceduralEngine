package pe.engine.graphics.main;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;

import pe.engine.main.PE;

public class WindowSizeChangeHandler implements GLFWFramebufferSizeCallbackI{
	
	private Window window;
	
	public WindowSizeChangeHandler(Window window){
		this.window = window;
	}

	@Override
	public void invoke(long windowID, int width, int height) {
		if (window.getID() != windowID)
			return;

		window.setWidth(width, PE.GUI_UNIT_PIXELS);
		window.setHeight(height, PE.GUI_UNIT_PIXELS);
		window.generateOrthoProjection();
	}
}
