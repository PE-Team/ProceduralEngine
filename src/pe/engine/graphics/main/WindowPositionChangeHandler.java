package pe.engine.graphics.main;

import org.lwjgl.glfw.GLFWWindowPosCallbackI;

public class WindowPositionChangeHandler implements GLFWWindowPosCallbackI{
	
	private Window window;
	
	public WindowPositionChangeHandler(Window window){
		this.window = window;
	}

	@Override
	public void invoke(long windowID, int posX, int posY) {
		if(window.getID() != windowID)
			return;
		
		window.setPosition(posX, posY);
		window.generateMonitorStats();
	}

}
