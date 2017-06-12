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
import pe.util.math.Vec2f;

public class Window implements DisposableResource {

	private int monitorWidth;
	private int monitorHeight;
	private float rpixRatio;
	private float width = 1;
	private int widthUnit = PE.GUI_UNIT_RPIXELS;
	private float height = 1;
	private int heightUnit = PE.GUI_UNIT_RPIXELS;
	private Vec2f position = Vec2f.ZERO;
	private int[] positionUnits = {PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS};
	private WindowSizeChangeHandler sizeChangeHandler;
	private WindowPositionChangeHandler posChangeHandler;
	private String title = "NULL";
	private long id;
	private boolean vsync = true;
	private GUI gui;
	private Mat4f orthoProjection;

	public Window(int width, int height, String title, boolean vsync, boolean resizeable, boolean border) {
		this.width = width;
		this.height = height;
		this.position = new Vec2f(5, 5);
		this.sizeChangeHandler = new WindowSizeChangeHandler(this);
		this.posChangeHandler = new WindowPositionChangeHandler(this);
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

		this.id = GLFW.glfwCreateWindow(1, 1, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (this.id == MemoryUtil.NULL) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		generateMonitorStats();
		generateOrthoProjection();
		updatePosition();
		updateSize();

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
	
	public void setPosition(float posX, float posY){
		this.position.x = posX;
		this.position.y = posY;
	}
	
	public void setPosition(Vec2f position){
		this.position = position;
	}

	/**
	 * Sets the position of the window with the given units.
	 * 
	 * @param posX
	 *            The x pixel position of the window.
	 * @param posXUnit
	 *            The units for the x position.
	 * @param posY
	 *            The y pixel position of the window.
	 * @param posYUnit
	 *            The units for the y position.
	 * 
	 * @since 1.0
	 */
	public void setPosition(Vec2f position, int posXUnit, int posYUnit) {
		this.position = position;
		this.positionUnits[0] = posXUnit;
		this.positionUnits[1] = posYUnit;

		updatePosition();
	}

	/**
	 * Updates the position of the Window. Generally only used internally, but
	 * may be necessary if the position is changed without the use of methods
	 * such as <code>setPosition</code>.
	 * 
	 * @since 1.0
	 */
	public void updatePosition() {
		int xPix = (int) PE.toPixels(position.x, positionUnits[0], monitorWidth, rpixRatio);
		int yPix = (int) PE.toPixels(position.y, positionUnits[1], monitorHeight, rpixRatio);

		GLFW.glfwSetWindowPos(id, xPix, yPix);
	}
	
	/**
	 * Returns the relative pixel ratio for this window in its monitor.
	 * 
	 * @return The rpix ratio for this window.
	 * 
	 * @see #rpixRatio
	 * 
	 * @since 1.0
	 */
	public float getRPixRatio(){
		return rpixRatio;
	}

	public void generateOrthoProjection() {
		float widthPix = PE.toPixels(width, widthUnit, monitorWidth, rpixRatio);
		float heightPix = PE.toPixels(height, heightUnit, monitorHeight, rpixRatio);
		
		this.orthoProjection = Mat4f.getOrthographicMatrix(0, widthPix, heightPix, 0, -1, 1);
	}

	public void setWidth(float width, int units) {
		this.width = width;
		this.widthUnit = units;
	}

	public void setHeight(float height, int units) {
		this.height = height;
		this.heightUnit = units;
	}
	
	public void setSize(float width, int widthUnits, float height, int heightUnits){
		this.width = width;
		this.widthUnit = widthUnits;
		this.height = height;
		this.heightUnit = heightUnits;
		
		updateSize();
	}
	
	public void updateSize(){
		int widthPix = (int) PE.toPixels(width, widthUnit, monitorWidth, rpixRatio);
		int heightPix = (int) PE.toPixels(height, heightUnit, monitorHeight, rpixRatio);
		GLFW.glfwSetWindowSize(id, widthPix, heightPix);
	}

	public void generateMonitorStats() {
		IntBuffer pysicalSize = BufferUtils.createIntBuffer(2);
		
		//long monitor = GLFW.glfwGetWindowMonitor(id);
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFW.glfwGetMonitorPhysicalSize(monitor, pysicalSize, pysicalSize);
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
		this.monitorWidth = videoMode.width();
		this.monitorHeight = videoMode.height();
		this.rpixRatio = monitorWidth * 1f / pysicalSize.get(0);
	}
}
