package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.main.PE;

public class WindowFrameSizeHandler extends GLFWFramebufferSizeCallback implements WindowEventHandlerI, DisposableResourceI{
	
	private static final int[] sizeUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	
	private WindowHandler windowHandler;
	
	public WindowFrameSizeHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long windowID, int width, int height) {
		if(windowHandler.isNotWindow(windowID))
			return;

		windowHandler.setSize(width, height, sizeUnits);
		windowHandler.updateWindow();
	}

	@Override
	public void dispose() {
		free();
	}

	@Override
	public void setWindowHanlder(WindowHandler windowHandler) {
		this.windowHandler = windowHandler;
	}
}
