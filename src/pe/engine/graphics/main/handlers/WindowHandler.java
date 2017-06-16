package pe.engine.graphics.main.handlers;

import pe.engine.graphics.main.Window;
import pe.util.math.Vec2f;

public class WindowHandler {

	private Window window;
	private Vec2f mousePosition = Vec2f.ZERO;
	private boolean[] keys = new boolean[65536];
	
	public void setWindow(Window window){
		this.window = window;
	}

	public void setKeyState(int key, boolean state) {
		keys[key] = state;
	}

	public boolean getKeyState(int key) {
		return keys[key];
	}

	public void setMousePositionValue(float posX, float posY) {
		this.mousePosition.x = posX;
		this.mousePosition.y = posY;
	}

	public void setMousePositionValue(Vec2f pos) {
		this.mousePosition = pos;
	}

	public Vec2f getMousePosition() {
		return mousePosition;
	}

	public boolean isNotWindow(long windowID) {
		return window == null || windowID != window.getID();
	}

	public void setSizeValue(float width, float height, int[] units) {
		window.setSizeValue(width, height, units);
	}

	public void updateWindow() {
		window.update();
	}

	public Window getWindow() {
		return window;
	}

	public void setPositionValue(float posX, float posY, int[] units) {
		window.setPositionValue(posX, posY, units);
	}
}
