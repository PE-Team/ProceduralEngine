package pe.engine.main;

import org.lwjgl.glfw.GLFW;

import pe.engine.data.Resources;
import pe.engine.graphics.main.Window;
import pe.engine.shader.main.Shader;
import pe.engine.shader.shaders.core.CoreShaders;
import pe.engine.threading.AudioThread;
import pe.engine.threading.MasterThread;
import pe.engine.threading.NetworkingThread;
import pe.engine.threading.RenderingThread;

/**
 * Contains the main initialization methods for the entirety of the Procedural
 * Engine. This includes initializing GLFW, getting the GLVersion for the
 * computer, and more.
 * 
 * @author Ethan Penn
 *
 * @see #init()
 * @see MasterThread
 * 
 * @since 1.0
 */
public class InitializationProcesses {

	/**
	 * The method to be called to initialize all of Procedural Engine by the
	 * Master Thread. Needs to be one of the first methods called from the
	 * Master Thread. Note: within itself it does call <code>glInit</code>.
	 * 
	 * @param source
	 *            A name of the thread or process which called it. Will output
	 *            to <code>MasterThread.println</code> with the parameter of
	 *            that function, source, equal to "Init: [source]", where the
	 *            second source was the input for this function.
	 * 
	 * @see #glInit(String)
	 * @see MasterThread
	 * 
	 * @since 1.0
	 */
	public static void init(String source) {
		MasterThread.println("Init: " + source, "Starting Initialization Processes");

		MasterThread.println("Init: " + source, "Setting the GLFW Error Callback");
		GLFW.glfwSetErrorCallback(MasterThread.getErrorHandler());

		glInit(source);

		MasterThread.println("Init: " + source, "Creating the Rendering Thread");
		RenderingThread renderer = new RenderingThread();
		MasterThread.addRenderingThread(renderer);

		MasterThread.println("Init: " + source, "Creating the Audio Thread");
		AudioThread audio = new AudioThread();
		MasterThread.addAudioThread(audio);

		MasterThread.println("Init: " + source, "Creating the Networking Thread");
		NetworkingThread networking = new NetworkingThread();
		MasterThread.addNetworkingThread(networking);

		MasterThread.println("Init: " + source, "Finished Initialization Processes");
	}

	/**
	 * The method to be called to initialize OpenGL for use in a thread. This is
	 * used for threads like Rendering Threads which
	 *
	 * @param source
	 *            A name of the thread or process which called it. Will output
	 *            to <code>MasterThread.println</code> with the parameter of
	 *            that function, source, equal to "Init: [source]", where the
	 *            second source was the input for this function.
	 * @see MasterThread
	 * 
	 * @since 1.0
	 */
	public static void glInit(String source) {
		MasterThread.println("Init: " + source, "Starting OpenGL Initializtion Processes");

		MasterThread.println("Init: " + source, "Initializing GLFW");
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		MasterThread.println("Init: " + source, "Loading the Highest OpenGL Version Supported by this Device.");
		GLVersion.loadVersion();

		MasterThread.println("Init: " + source, "Finished OpenGL Initializtion Processes");
	}

	/**
	 * The method to be called to stop and dispose of the resources used by
	 * Procedural Engine. Should be the last method called in the Master Thread.
	 * 
	 * @param source
	 *            A name of the thread or process which called it. Will output
	 *            to <code>MasterThread.println</code> with the parameter of
	 *            that function, source, equal to "Deinit: [source]", where the
	 *            second source was the input for this function.
	 * 
	 * @see MasterThread
	 * 
	 * @since 1.0
	 */
	public static void deinit(String source) {
		MasterThread.println("Deinit: " + source, "Starting Deinitialization Processes");

		MasterThread.println("Deinit: " + source, "Disposing All Resources");
		Resources.disposeAll();

		MasterThread.println("Deinit: " + source, "Terminating GLFW");
		GLFW.glfwTerminate();

		MasterThread.println("Deinit: " + source, "Finished Deinitialization Processes");
	}
}
