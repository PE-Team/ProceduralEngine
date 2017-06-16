package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;

public class WindowMousePositionHandler extends GLFWCursorPosCallback implements WindowEventHandler, DisposableResource{

	WindowHandler windowHandler;
	
	public WindowMousePositionHandler(){
		Resources.add(this);
	}
	
	@Override
	public void invoke(long window, double posX, double posY) {
		windowHandler.setMousePositionValue((float) posX, (float) posY);
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
