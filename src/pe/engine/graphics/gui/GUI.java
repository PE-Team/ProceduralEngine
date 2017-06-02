package pe.engine.graphics.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pe.engine.graphics.main.Window;

public class GUI {

	private List<GUIComponent> widgets;
	private Window window = null;
	
	public GUI(){
		widgets = new ArrayList<GUIComponent>();
	}
	
	public void render(){
		for(GUIComponent widget:widgets){
			widget.render();
		}
	}
	
	public void addComponent(GUIComponent component){
		widgets.add(component);
		component.setGUI(this);
	}
	
	public void removeComponent(GUIComponent component){
		widgets.remove(component);
		component.setGUI(null);
	}
	
	public void setWindow(Window window){
		this.window = window;
	}
	
	public Window getWindow(){
		return window;
	}
}
