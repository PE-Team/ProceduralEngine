package pe.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;

public class KeyHandler extends GLFWKeyCallback implements DisposableResource{

	/* key[keyCode] */
	private boolean[] keys = new boolean[65536];
	
	public KeyHandler(){
		Resources.add(this);
	}
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = (action != GLFW.GLFW_RELEASE);
	}
	
	public boolean getKey(int keyCode){
		return keys[keyCode];
	}
	
	public void dispose(){
		free();
	}
}
