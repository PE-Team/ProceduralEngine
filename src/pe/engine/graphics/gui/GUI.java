package pe.engine.graphics.gui;

import pe.engine.graphics.main.Window;

public class GUI {

	private Window window = null;
	private Divider root;
	
	public GUI(){
		root = new Divider();
		root.setGUI(this);
	}
	
	public void render(){
		root.renderChildren();
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
	}
	
	public Window getWindow(){
		return window;
	}
}
