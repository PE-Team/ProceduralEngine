package pe.engine.threading;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import pe.engine.data.VertexArrayObject;
import pe.engine.graphics.gui.Divider;
import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.main.Window;
import pe.engine.input.KeyHandler;
import pe.engine.main.InitializationProcesses;
import pe.util.Timer;
import pe.util.color.Color;

public class RenderingThread implements Runnable {

	private Timer timer;

	@Override
	public void run() {
		try {

			InitializationProcesses.glInit("Rendering Thread");

			timer = new Timer(0.5f);
			timer.start();

			KeyHandler keyHandler = new KeyHandler();
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_ESCAPE);
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_F4);
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_F4);

			Window window = new Window(1400, 800, "Test", true, true, true);
			window.setKeyHandler(keyHandler);
			window.show();
			
			VertexArrayObject vertexArrayObject = new VertexArrayObject();
			vertexArrayObject.use();

			GUI gui = new GUI();
			gui.setWindow(window);
			Divider div1 = new Divider(800, 400, 0, 0, Color.BLUE, Color.ORANGE);
			Divider div2 = new Divider(25, 400, 0, 0, Color.GRAY, Color.DARK_GRAY);
			gui.addComponent(div1);
			//gui.addComponent(div2);
			

			while (MasterThread.isRunning()) {
				if (timer.delayPassed()) {
					//MasterThread.println("Rendering Thread", "I am Rendering some stuff. Look at me!");
					GL11.glClearColor(0, 0, 0, 1);
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
					
					gui.render();

					window.update();
				}
			}
		} catch (Exception e) {
			MasterThread.println("Rendering Thread",
					"An Exception has Occured Which Will Cause the Main Thread to Shutdown");
			e.printStackTrace(MasterThread.getConsoleStream());
			MasterThread.shutdown();
		}
	}
}
