package pe.engine.graphics.gui;

import java.util.HashSet;
import java.util.Set;

import pe.engine.graphics.main.Window;

public class GUI {

	private Set<GUIComponent> widgets;
	private Window window = null;
	
	public GUI(){
		widgets = new HashSet<GUIComponent>();
	}
	
	public void render(){
		for(GUIComponent widget:widgets){
			widget.render();
		}
	}
	
	public void addComponent(GUIComponent component){
		widgets.add(component);
	}
	
	public void setWindow(Window window){
		this.window = window;
	}
	
	public Window getWindow(){
		return window;
	}
}
