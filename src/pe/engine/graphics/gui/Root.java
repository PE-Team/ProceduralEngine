package pe.engine.graphics.gui;

import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;


public class Root extends Divider{
	
	protected boolean invokeInputEvent(WindowInputEvent e, boolean disposed){
		return invokeChildrenInputEvent(e, disposed);
	}
	
	protected boolean invokeSelfInputEvent(WindowInputEvent e, boolean disposed){
		return false;
	}
	
	protected void updateSelfProperties(){
		Window window = gui.getWindow();

		if(window == null)
			return;
		
		this.size.setMaxValue(window.getSize()).setRPixSource(window);
		this.position.setMaxValue(window.getSize()).setRPixSource(window);
		this.center.setMaxValue(size).setRPixSource(window);
		this.borderWidth.setMaxValue(size).setRPixSource(window);
		this.borderRadius.setMaxValue(size).setRPixSource(window);
	}
	
	public void render(){
		renderChildren();
	}
}
