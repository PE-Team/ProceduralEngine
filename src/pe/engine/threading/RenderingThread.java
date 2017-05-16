package pe.engine.threading;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import pe.engine.data.VertexArrayObject;
import pe.engine.graphics.gui.Divider;
import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.gui.GUIComponent;
import pe.engine.graphics.main.Window;
import pe.engine.graphics.objects.StaticMesh2D;
import pe.engine.graphics.objects.StaticMesh3D;
import pe.engine.input.KeyHandler;
import pe.engine.main.InitializationProcesses;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.util.Timer;
import pe.util.color.Color;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec3f;

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

			Window window = new Window(700, 400, "Test", true, true, true);
			window.setKeyHandler(keyHandler);
			window.show();
			
			VertexArrayObject vertexArrayObject = new VertexArrayObject();
			vertexArrayObject.use();

			GUI gui = new GUI();
			gui.setWindow(window);
			Divider div = new Divider(1, 1, Color.BLUE);
			gui.addComponent(div);

			while (MasterThread.isRunning()) {
				if (timer.delayPassed()) {
					MasterThread.println("Rendering Thread", "I am Rendering some stuff. Look at me!");
					GL11.glClearColor(1, 0, 0, 1);
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
