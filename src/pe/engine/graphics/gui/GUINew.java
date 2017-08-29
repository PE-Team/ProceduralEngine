package pe.engine.graphics.gui;

import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;

public class GUINew {

	private Window window = null;
	private Root root;
	
	public GUINew(){
		root = new Root();
		root.setGUI(this);
	}
	
	public void render(){
		root.render(null);
	}
	
	public void addComponent(GUIComponentNew component){
		root.addChild(component);
		component.setGUI(this);
	}
	
	public void removeComponent(GUIComponentNew component){
		root.removeChild(component);
		component.setGUI(null);
	}
	
	public void setWindow(Window window){
		if(this.window == window)
			return;
		
		this.window = window;
		window.setGUINew(this);
		root.updateProperties();
	}
	
	public Window getWindow(){
		return window;
	}
	
	public GUIComponentNew getRoot(){
		return root;
	}
	
	public void invokeInputEvent(WindowInputEvent e){
		root.invokeInputEvent(e, false);
	}
}
