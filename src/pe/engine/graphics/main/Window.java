package pe.engine.graphics.main;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.gui.GUIComponent;
import pe.engine.main.GLVersion;
import pe.engine.main.PE;
import pe.util.math.Mat4f;

public class Window implements DisposableResource{

	private int monitorWidth;
	private int monitorHeight;
	private float rpixScale;
	private float width;
	private float height;
	private int positionX;
	private int positionY;
	private WindowSizeChangeHandler sizeChangeHandler;
	private WindowPositionChangeHandler posChangeHandler;
	private String title;
	private long id;
	private boolean vsync;
	private GUI gui;
	private Mat4f orthoProjection;

	public Window(int width, int height, String title, boolean vsync, boolean resizeable, boolean border) {
		this.width = width;
		this.height = height;
		this.positionX = 200;
		this.positionY = 200;
		this.sizeChangeHandler = new WindowSizeChangeHandler(this);
		this.posChangeHandler = new WindowPositionChangeHandler(this);
		this.orthoProjection = Mat4f.getOrthographicMatrix(0, width, height, 0, -1, 1);
		this.title = title;
		this.vsync = vsync;
		this.gui = new GUI();

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_TRUE);
		if (GLVersion.isAfter(PE.GL_VERSION_32)) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		} else if (GLVersion.isAfter(PE.GL_VERSION_21)) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		} else {
			throw new RuntimeException("OpenGL Version " + GLVersion.versionName()
					+ " is not supported.\nPlease change to another version or update your graphics driver.");
		}

		if (resizeable) {
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		} else {
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		}

		if (border) {
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
		} else {
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
		}

		this.id = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (this.id == MemoryUtil.NULL) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Center the window
		GLFW.glfwSetWindowPos(id, positionX, positionY);

		show();
		GL.createCapabilities();

		setVSync(vsync);

		GLFW.glfwSetFramebufferSizeCallback(id, sizeChangeHandler);
		GLFW.glfwSetWindowPosCallback(id, posChangeHandler);

		Resources.add(this);
	}

	public void setKeyHandler(GLFWKeyCallback keyHandler) {
		GLFW.glfwSetKeyCallback(id, keyHandler);
	}

	public void show() {
		GLFW.glfwMakeContextCurrent(id);
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(id);
	}

	public void buffersToFrameSize(IntBuffer width, IntBuffer height) {
		GLFW.glfwGetFramebufferSize(id, width, height);
	}

	public void dispose() {
		GLFW.glfwDestroyWindow(id);
	}

	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(id, title);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public long getID() {
		return id;
	}

	public void update() {
		GLFW.glfwSwapBuffers(id);
		GLFW.glfwPollEvents();
	}

	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		if (vsync) {
			GLFW.glfwSwapInterval(1);
		} else {
			GLFW.glfwSwapInterval(0);
		}
	}

	public void addComponent(GUIComponent component) {
		gui.addComponent(component);
	}

	/**
	 * Returns the value of <code>vsync</code>. Is true if vsync is enabled and
	 * false if it is not.
	 * 
	 * @return whether VSync is enabled for this window or not
	 * 
	 * @see #vsync
	 * 
	 * @since 1.0
	 */
	public boolean isVSynced() {
		return vsync;
	}

	/**
	 * Returns the width of this <code>Window</code> object in pixels.
	 * 
	 * @return The width of this window.
	 * 
	 * @see #width
	 * 
	 * @since 1.0
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height of this <code>Window</code> object in pixels.
	 * 
	 * @return The height of this window.
	 * 
	 * @see #height
	 * 
	 * 
	 * @since 1.0
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Returns the orthographic projection matrix for the pixel space of the
	 * screen. This projection assumes that (0,0) is the top-left corner and
	 * (width, height) is the bottom right.
	 * 
	 * @return The orthographic projection matrix for the window in pixel space.
	 * 
	 * @see #orthoProjection
	 * 
	 * @since 1.0
	 */
	public Mat4f getOrthoProjection() {
		return orthoProjection;
	}
	
	/**
	 * Sets the position of the window.
	 * 
	 * @param posX	The x pixel position of the window.
	 * @param posY	The y pixel position of the window.
	 * 
	 * @since 1.0
	 */
	public void setPosition(int posX, int posY){
		this.positionX = posX;
		this.positionY = posY;
	}
	
	public void generateOrthoProjection(){
		//Mat4f.getOrthographicMatrix(0, width, height, 0, -1, 1);
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public void generateMonitorStats(){
		IntBuffer pysicalSize = BufferUtils.createIntBuffer(2);
		
		long monitor = GLFW.glfwGetWindowMonitor(id);
		GLFW.glfwGetMonitorPhysicalSize(monitor, pysicalSize, pysicalSize);
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
		this.monitorWidth = videoMode.width();
		this.monitorHeight = videoMode.height();
		this.rpixScale = monitorWidth * 1f/pysicalSize.get(0);
	}
}
