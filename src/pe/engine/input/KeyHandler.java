package pe.engine.input;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.threading.MasterThread;

public class KeyHandler extends GLFWKeyCallback implements DisposableResource {

	/* key[keyCode] */
	private boolean[] keys = new boolean[65536];
	private Set<Set<Integer>> shutdownKeys = new HashSet<Set<Integer>>();

	public KeyHandler() {
		Resources.add(this);
	}

	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = (action != GLFW.GLFW_RELEASE);

		for (Set<Integer> hotKeys : shutdownKeys) {
			boolean shutdown = true;
			for (int hotKey : hotKeys) {
				if (!keys[hotKey]) {
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
		return keys[keyCode];
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
}
