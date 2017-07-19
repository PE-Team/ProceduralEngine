package pe.engine.graphics.gui;

import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.main.PE;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;

public class GUINew {

	private Window window = null;
	private GUIComponentNew root;
	
	public GUINew(){
		root =  new GUIComponentNew(Color.CLEAR, Color.CLEAR, Vec4f.zero(), Vec4f.zero(),
				false, Vec4f.zero(), Vec4f.zero(), Vec2f.zero(), Vec2f.zero(), 0, Vec2f.zero(),
				new Vec2f(1.0f, 1.0f), 0);
		root.size.set(new Vec2f(1.0f, 1.0f), new int[]{PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT}); 
		root.clipChildren = true;
		root.setGUI(this);
	}
	
	public void render(){
		Vec2f size = window.getSize().pixels();
		root.getFBO().setColorBufferTexture(GUIComponentNew.DEFAULT_RENDER_LOCATION, (int) size.x, (int) size.y);
		root.render(root.getFBO());
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
