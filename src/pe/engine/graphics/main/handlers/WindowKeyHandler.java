package pe.engine.graphics.main.handlers;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.threading.MasterThread;

public class WindowKeyHandler extends GLFWKeyCallback implements WindowEventHandlerI, DisposableResourceI {

	/* key[keyCode] */
	private WindowHandler windowHandler;
	private Set<Set<Integer>> shutdownKeys = new HashSet<Set<Integer>>();

	public WindowKeyHandler() {
		Resources.add(this);
	}

	public void invoke(long window, int key, int scancode, int action, int mods) {
		windowHandler.setKeyState(key, action != GLFW.GLFW_RELEASE);

		for (Set<Integer> hotKeys : shutdownKeys) {
			boolean shutdown = true;
			for (int hotKey : hotKeys) {
				if (!windowHandler.getKeyState(hotKey)) {
					shutdown = false;
					break;
				}
			}

			if (shutdown) {
				MasterThread.shutdown();
				break;
			}
		}
	}

	public boolean getKey(int keyCode) {
		return windowHandler.getKeyState(keyCode);
	}

	public void dispose() {
		free();
	}

	/**
	 * Will add a new set of HotKeys which when pressed will call
	 * <code>MasterThread.shutdown</code> to begin shutting down all processes.
	 * Any number of different HotKeys may be added, although it should be noted
	 * that for each set of HotKeys, this function must be called each time.
	 * 
	 * @param keys
	 *            The keys to be pressed to cause the shutdown to start. The key
	 *            integer values can be found in <code>GLFW</code>.
	 * 
	 * @see #shutdownKeys
	 * @see #MasterThread.shutdown()
	 * @see GLFW
	 * 
	 * @since 1.0
	 */
	public void addShutdownHotkeys(int... keys) {
		Set<Integer> hotKeys = new HashSet<Integer>();
		for (int key : keys) {
			hotKeys.add(key);
		}
		shutdownKeys.add(hotKeys);
	}

	@Override
	public void setWindowHanlder(WindowHandler windowHandler) {
		this.windowHandler = windowHandler;
	}
}
