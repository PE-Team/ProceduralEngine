package pe.engine.graphics.main;

import org.lwjgl.glfw.GLFWWindowPosCallbackI;

import pe.engine.main.PE;

public class WindowPositionChangeHandler implements GLFWWindowPosCallbackI{
	
	private static final int[] positionUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	
	private Window window;
	
	public WindowPositionChangeHandler(Window window){
		this.window = window;
	}

	@Override
	public void invoke(long windowID, int posX, int posY) {
		if(window.getID() != windowID)
			return;
		
		window.setPosition(posX, posY, positionUnits);
	}

}
