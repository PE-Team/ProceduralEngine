package pe.engine.threading;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import pe.engine.data.VertexArrayObject;
import pe.engine.graphics.gui.Divider;
import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowFrameSizeHandler;
import pe.engine.graphics.main.handlers.WindowHandler;
import pe.engine.graphics.main.handlers.WindowKeyHandler;
import pe.engine.graphics.main.handlers.WindowPositionHandler;
import pe.engine.main.InitializationProcesses;
import pe.engine.main.PE;
import pe.util.Timer;
import pe.util.color.Color;
import pe.util.math.Vec2f;

public class RenderingThread implements Runnable {

	private Timer timer;

	@Override
	public void run() {
		try {

			InitializationProcesses.glInit("Rendering Thread");

			timer = new Timer(0.016f);
			timer.start();

			Vec2f windowSize = new Vec2f(1400f, 800f);
			int[] sizeUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
			Vec2f windowPosition = new Vec2f(0.5f, 0.5f);
			int[] positionUnits = {PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT};
			Vec2f windowCenter = new Vec2f(0.5f, 0.5f);
			int[] centerUnits = {PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT};
			
			Window window = new Window(windowSize, sizeUnits, windowPosition, positionUnits, windowCenter, centerUnits, "Test", true, true, true);
			
			WindowHandler windowHandler = new WindowHandler();

			WindowKeyHandler keyHandler = new WindowKeyHandler();
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_ESCAPE);
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_F4);
			keyHandler.addShutdownHotkeys(GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_F4);
			
			WindowFrameSizeHandler sizeHandler = new WindowFrameSizeHandler();
			
			WindowPositionHandler posHandler = new WindowPositionHandler();
			
			window.setWindowHandler(windowHandler);
			window.setKeyHandler(keyHandler);
			window.setFrameSizeHandler(sizeHandler);
			window.setPositionHandler(posHandler);
			window.show();
			
			VertexArrayObject vertexArrayObject = new VertexArrayObject();
			vertexArrayObject.bind();

			GUI gui = new GUI();
			gui.setWindow(window);
			Divider div1 = new Divider(800, 400, 0, 0, Color.BLUE, Color.ORANGE);
			Divider div2 = new Divider(25, 400, 0, 0, Color.GRAY, Color.DARK_GRAY);
			gui.addComponent(div1);
			//gui.addComponent(div2);
			
			GL11.glClearColor(0, 0, 0, 1);
			while (MasterThread.isRunning()) {
				if (timer.delayPassed()) {
					//MasterThread.println("Rendering Thread", "I am Rendering some stuff. Look at me!");
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
					
					gui.render();

					window.update();
				}
			}
		} catch (Exception e) {
			MasterThread.errln("Rendering Thread",
					"An Exception has Occured Which Will Cause the Main Thread to Shutdown");
			e.printStackTrace(MasterThread.getConsoleErrStream());
			MasterThread.shutdown();
		}
	}
}
