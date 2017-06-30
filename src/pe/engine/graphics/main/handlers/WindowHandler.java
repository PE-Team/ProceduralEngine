package pe.engine.graphics.main.handlers;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import pe.engine.graphics.main.Window;
import pe.engine.main.PE;
import pe.util.Timer;
import pe.util.math.Vec2f;

public class WindowHandler {
	
	public static final float MIN_DRAG_DISTANCE = 10f;
	public static final float MIN_DRAG_TIME = 1.0f;
	public static final float MIN_HOVER_TIME = 1.0f;

	// @formatter:off
	private Window window;
	private DoubleBuffer mousePosXBuffer = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer mousePosYBuffer = BufferUtils.createDoubleBuffer(1);
	private Vec2f mousePosition = Vec2f.zero();
	private boolean[] keys = new boolean[65536];		// Pressed = true, Released = false
	private boolean[] mouseButtons = new boolean[8]; 	// Pressed = true, Released = false
	
	private Timer dragTimer = new Timer(MIN_DRAG_TIME);
	private Timer hoverTimer = new Timer(MIN_HOVER_TIME);
	// @formatter:on
	
	private Vec2f mousePressPosition = null;
	
	public void setWindow(Window window){
		this.window = window;
	}
	
	public void windowFocusLost(){
		this.mousePressPosition = null;
		this.dragTimer.stop();
		this.hoverTimer.stop();
	}
	
	public void fireMouseButtonEvent(int button, boolean pressed){
		if(pressed){
			mousePressPosition = new Vec2f(mousePosition);
			window.fireInputEvent(
					new WindowInputEvent(this, button, PE.MOUSE_ACTION_PRESS)
					);
		}else{
			window.fireInputEvent(
					new WindowInputEvent(this, button, PE.MOUSE_ACTION_RELEASE, mousePressPosition)
					);
			mousePressPosition = null;
		}
	}
	
	public void fireMouseScrollEvent(float scrollX, float scrollY){
		window.fireInputEvent(
				new WindowInputEvent(this, PE.NULL, PE.MOUSE_ACTION_SCROLL, scrollX, scrollY)
				);
	}
	
	public void fireMousePositionEvent(){
		if(mousePressPosition != null && (dragTimer.delayPassedNoReset() || Vec2f.subtract(mousePressPosition, mousePosition).length() >= MIN_DRAG_DISTANCE)){
			window.fireInputEvent(
					new WindowInputEvent(this, PE.NULL, PE.MOUSE_ACTION_DRAG)
					);
		}else if(mousePressPosition == null && hoverTimer.delayPassedNoReset()){
			window.fireInputEvent(
					new WindowInputEvent(this, PE.NULL, PE.MOUSE_ACTION_HOVER)
					);
		}
	}
	
	public void update(){
		GLFW.glfwGetCursorPos(window.getID(), mousePosXBuffer, mousePosYBuffer);
		this.mousePosition.x = (float) mousePosXBuffer.get(0);
		this.mousePosition.y = (float) mousePosYBuffer.get(0);
		fireMousePositionEvent();
	}

	public void setKeyState(int key, boolean state) {
		if(key >= 0)
			keys[key] = state;
	}

	public boolean getKeyState(int key) {
		return keys[key];
	}
	
	public void setMouseButtonState(int mouseButton, boolean pressed){
		this.mouseButtons[mouseButton - PE.MOUSE_BUTTON_1] = pressed;
		this.hoverTimer.restart();
		this.dragTimer.restart();
		
		fireMouseButtonEvent(mouseButton, pressed);
	}
	
	public boolean getMouseButtonState(int mouseButton){
		return mouseButtons[mouseButton - PE.MOUSE_BUTTON_1];
	}

	public void setMousePositionValue(float posX, float posY) {
		this.mousePosition.x = posX;
		this.mousePosition.y = posY;
		this.hoverTimer.restart();
		
		fireMousePositionEvent();
	}

	public void setMousePositionValue(Vec2f pos) {
		this.mousePosition = pos;
		this.hoverTimer.restart();
		
		fireMousePositionEvent();
	}

	public Vec2f getMousePosition() {
		return mousePosition;
	}

	public boolean isNotWindow(long windowID) {
		return window == null || windowID != window.getID();
	}

	public void setSize(float width, float height, int[] units) {
		window.setSize(width, height, units);
	}

	public void updateWindow() {
		window.update();
	}

	public Window getWindow() {
		return window;
	}

	public void setPosition(float posX, float posY, int[] units) {
		window.setPosition(posX, posY, units);
	}
}
