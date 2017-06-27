package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFWWindowPosCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.main.PE;
import pe.util.math.Vec2f;

public class WindowPositionHandler extends GLFWWindowPosCallback implements WindowEventHandlerI, DisposableResourceI{
	
	private static final int[] positionUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	
	private WindowHandler windowHandler;
	
	public WindowPositionHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long windowID, int posX, int posY) {
		if(windowHandler.isNotWindow(windowID))
			return;
		
		Vec2f windowCenter = windowHandler.getWindow().getCenter().pixels();
		windowHandler.setPosition(posX + windowCenter.x, posY + windowCenter.y, positionUnits);
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
