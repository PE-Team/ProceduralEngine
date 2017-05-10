package pe.engine.main;

import org.lwjgl.glfw.GLFW;

import pe.engine.data.Resources;
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
	 * The method to be called to initialize all of Procedural Engine. Needs to
	 * be one of the first methods called from the <code>Master Thread</code>.
	 * 
	 * @see MasterThread
	 * 
	 * @since 1.0
	 */
	public static void init() {
		MasterThread.println("Initialization", "Starting Initialization Processes");

		MasterThread.println("Initialization", "Setting the GLFW Error Callback");
		GLFW.glfwSetErrorCallback(MasterThread.getErrorHandler());

		MasterThread.println("Initialization", "Initializing GLFW");
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		MasterThread.println("Initialization", "Loading the Highest OpenGL Version Supported by this Device.");
		GLVersion.loadVersion();

		MasterThread.println("Initialization", "Creating the Rendering Thread");
		RenderingThread renderer = new RenderingThread();
		MasterThread.addRenderingThread(renderer);

		MasterThread.println("Initialization", "Creating the Audio Thread");
		AudioThread audio = new AudioThread();
		MasterThread.addAudioThread(audio);

		MasterThread.println("Initialization", "Creating the Networking Thread");
		NetworkingThread networking = new NetworkingThread();
		MasterThread.addNetworkingThread(networking);

		MasterThread.println("Initialization", "Finished Initialization Processes");
	}

	/**
	 * The method to be called to stop and dispose of the resources used by
	 * Procedural Engine. Should be the last method called in the Master Thread.
	 * 
	 * @see MasterThread
	 * 
	 * @since 1.0
	 */
	public static void deinit(){
		MasterThread.println("Deinitialization", "Starting Deinitialization Processes");
		
		MasterThread.println("Deinitialization", "Disposing of Resources");
		Resources.disposeAll();
		
		MasterThread.println("Deinitialization", "Terminating GLFW");
		GLFW.glfwTerminate();
		
		MasterThread.println("Deinitialization", "Finished Deinitialization Processes");
	}
}
