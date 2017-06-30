package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;

public class WindowMousePositionHandler extends GLFWCursorPosCallback implements WindowEventHandlerI, DisposableResourceI{

	WindowHandler windowHandler;
	
	public WindowMousePositionHandler(){
		Resources.add(this);
	}
	
	@Override
	public void invoke(long window, double posX, double posY) {
		float xPos = (float) posX;
		float yPos = (float) posY;
		windowHandler.setMousePositionValue(xPos, yPos);
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
