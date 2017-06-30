package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWWindowFocusCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;

public class WindowFocusHandler extends GLFWWindowFocusCallback implements DisposableResourceI, WindowEventHandlerI{
	
	private WindowHandler windowHandler;
	
	public WindowFocusHandler(){
		Resources.add(this);
	}
	
	@Override
	public void invoke(long windowID, boolean focused) {
		if(windowHandler.isNotWindow(windowID))
			return;
		
		if(!focused){
			windowHandler.windowFocusLost();
		}
	}
	
	@Override
	public void dispose() {
		free();
	}

	@Override
	public void setWindowHandler(WindowHandler windowHandler) {
		this.windowHandler = windowHandler;
	}
}
