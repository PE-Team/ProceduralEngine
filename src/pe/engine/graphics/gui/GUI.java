package pe.engine.graphics.gui;

import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;

public class GUI {

	private Window window = null;
	private Root root;
	
	public GUI(){
		root = new Root();
		root.setGUI(this);
	}
	
	public void render(){
		root.render();
	}
	
	public void addComponent(GUIComponent component){
		root.addChild(component);
		component.setGUI(this);
	}
	
	public void removeComponent(GUIComponent component){
		root.removeChild(component);
		component.setGUI(null);
	}
	
	public void setWindow(Window window){
		this.window = window;
		window.setGUI(this);
		root.updateProperties();
	}
	
	public Window getWindow(){
		return window;
	}
	
	public void invokeInputEvent(WindowInputEvent e){
		root.invokeInputEvent(e, false);
	}
}
