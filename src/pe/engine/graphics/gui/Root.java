package pe.engine.graphics.gui;

import pe.engine.graphics.main.handlers.WindowInputEvent;

public class Root extends Divider{
	
	protected boolean invokeInputEvent(WindowInputEvent e, boolean disposed){
		return invokeChildrenInputEvent(e, disposed);
	}
	
	protected boolean invokeSelfInputEvent(WindowInputEvent e, boolean disposed){
		return false;
	}
}
