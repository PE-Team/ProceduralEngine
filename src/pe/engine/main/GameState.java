package pe.engine.main;

import java.util.Set;

import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.main.Window;
import pe.engine.input.KeyHandler;

public class GameState {

	private GUI gui;
	private Set<Window> windows;
	
	public GUI getGUI(){
		return gui;
	}
	
	public void createWindow(int width, int height, String title, boolean vsync, boolean resizeable, boolean border, KeyHandler keyHandler){
		Window window = new Window(width, height, title, vsync, resizeable, border);
		windows.add(window);
	}
}
