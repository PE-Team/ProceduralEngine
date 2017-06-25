package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWScrollCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;

public class WindowScrollHandler extends GLFWScrollCallback implements WindowEventHandler, DisposableResource{
	
	private WindowHandler windowHandler;
	
	public WindowScrollHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long window, double offsetX, double offsetY) {
		windowHandler.fireMouseScrollEvent((float) offsetX, (float) offsetY); 
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
