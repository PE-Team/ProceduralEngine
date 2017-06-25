package pe.engine.graphics.main.handlers;

import pe.util.math.Vec2f;

public class WindowInputEvent {
	
	protected WindowHandler windowHandler;
	protected int inputButton;
	protected int inputAction;
	protected Vec2f delta = null;
	
	public WindowInputEvent(WindowHandler windowHandler, int inputButton, int inputAction){
		this.windowHandler = windowHandler;
		this.inputButton = inputButton;
		this.inputAction = inputAction;
	}
	
	public WindowInputEvent(WindowHandler windowHandler, int inputButton, int inputAction, Vec2f delta){
		this.windowHandler = windowHandler;
		this.inputButton = inputButton;
		this.inputAction = inputAction;
		this.delta = delta;
	}
	
	public WindowInputEvent(WindowHandler windowHandler, int inputButton, int inputAction, float deltaX, float deltaY){
		this.windowHandler = windowHandler;
		this.inputButton = inputButton;
		this.inputAction = inputAction;
		this.delta = new Vec2f(deltaX, deltaY);
	}
	
	public int getButton(){
		return inputButton;
	}
	
	public int getAction(){
		return inputAction;
	}
	
	public Vec2f getDelta(){
		return delta;
	}
	
	public float getDeltaX(){
		return delta.x;
	}
	
	public float getDeltaY(){
		return delta.y;
	}
	
	public Vec2f getPosition(){
		return windowHandler.getMousePosition();
	}
	
	public WindowHandler getWindowHandler(){
		return windowHandler;
	}
}
