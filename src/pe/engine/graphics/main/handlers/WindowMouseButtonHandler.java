package pe.engine.graphics.main.handlers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.main.UnitConversions;

public class WindowMouseButtonHandler extends GLFWMouseButtonCallback implements WindowEventHandlerI, DisposableResourceI{
	
	private WindowHandler windowHandler;
	
	public WindowMouseButtonHandler(){
		Resources.add(this);
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		windowHandler.setMouseButtonState(UnitConversions.toPEMouseButton(button), action == GLFW.GLFW_PRESS);
	}

	@Override
	public void setWindowHanlder(WindowHandler windowHandler) {
		this.windowHandler = windowHandler;
	}

	@Override
	public void dispose() {
		free();
	}
}
