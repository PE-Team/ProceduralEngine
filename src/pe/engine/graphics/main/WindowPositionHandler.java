package pe.engine.graphics.main;

import org.lwjgl.glfw.GLFWWindowPosCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.main.PE;
import pe.util.math.Vec2f;

public class WindowPositionHandler extends GLFWWindowPosCallback implements DisposableResource{
	
	private static final int[] positionUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	
	private Window window;
	
	public WindowPositionHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long windowID, int posX, int posY) {
		if(window == null || window.getID() != windowID)
			return;
		Vec2f windowCenter = window.getCenterPix();
		window.setPositionValue(posX + windowCenter.x, posY + windowCenter.y, positionUnits);
	}

	public void setWindow(Window window){
		this.window = window;
	}

	@Override
	public void dispose() {
		free();
	}
}
