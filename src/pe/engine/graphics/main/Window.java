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

	private Vec2f monitorSize;
	private float rpixRatio;
	private Vec2f size = new Vec2f(1, 1);
	private Vec2f position = Vec2f.ZERO;
	private WindowSizeChangeHandler sizeChangeHandler;
	private WindowPositionChangeHandler posChangeHandler;
	private String title = "NULL";
	private long id;
	private boolean vsync = true;
	private GUI gui = new GUI();
	private Mat4f orthoProjection;

	public Window(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, String title, boolean vsync, boolean resizeable, boolean border) {

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_TRUE);
		if (GLVersion.isAfter(PE.GL_VERSION_32)) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
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
		GLFW.glfwMakeContextCurrent(id);
		GL.createCapabilities();
		
		generateMonitorStats();
		setSize(size, sizeUnits);
		generateOrthoProjection();
		setPosition(position, positionUnits);
		setVSync(vsync);

		GLFW.glfwSetFramebufferSizeCallback(id, sizeChangeHandler);
		GLFW.glfwSetWindowPosCallback(id, posChangeHandler);

		Resources.add(this);
		show();
	}

	public void addComponent(GUIComponent component) {
		gui.addComponent(component);
	}

	public void putSizeInBuffer(IntBuffer width, IntBuffer height) {
		GLFW.glfwGetFramebufferSize(id, width, height);
	}

	public void dispose() {
		GLFW.glfwDestroyWindow(id);
	}

	public void generateMonitorStats() {
		IntBuffer pysicalSize = BufferUtils.createIntBuffer(2);

		// long monitor = GLFW.glfwGetWindowMonitor(id);
		long monitor = GLFW.glfwGetPrimaryMonitor();
		GLFW.glfwGetMonitorPhysicalSize(monitor, pysicalSize, pysicalSize);
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
		this.monitorSize = new Vec2f(videoMode.width(), videoMode.height());
		this.rpixRatio = monitorSize.x * 1f / pysicalSize.get(0);
	}

	public void generateOrthoProjection() {
		this.orthoProjection = Mat4f.getOrthographicMatrix(0, size.x, size.y, 0, -1, 1);
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
		return size.y;
	}

	/**
	 * Returns the height of this <code>Window</code> object in the units given.
	 * 
	 * @param units
	 *            The units to convert the height to.
	 * 
	 * @return The height of this window.
	 * 
	 * @see #height
	 * 
	 * @since 1.0
	 */
	public float getHeight(int units) {
		return PE.convertFromPix(size.y, units, monitorSize.y, rpixRatio);
	}

	/**
	 * Gets the OpenGL long ID for the window.
	 * 
	 * @return The id for this window.
	 * 
	 * @see #id
	 * 
	 * @since 1.0
	 */
	public long getID() {
		return id;
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
	 * Returns the relative pixel ratio for this window in its monitor.
	 * 
	 * @return The rpix ratio for this window.
	 * 
	 * @see #rpixRatio
	 * 
	 * @since 1.0
	 */
	public float getRPixRatio() {
		return rpixRatio;
	}

	public String getTitle() {
		return title;
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
		return size.x;
	}
	
	/**
	 * Returns the width of this <code>Window</code> object in the units given.
	 * 
	 * @param units
	 *            The units to convert the width to.
	 * 
	 * @return The width of this window.
	 * 
	 * @see #width
	 * 
	 * @since 1.0
	 */
	public float getWidth(int units) {
		return PE.convertFromPix(size.x, units, monitorSize.x, rpixRatio);
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

	public void setHeight(float height, int units) {
		this.size.x = PE.toPixels(height, units, monitorSize.y, rpixRatio);
		
		updateSize();
	}

	public void setKeyHandler(GLFWKeyCallback keyHandler) {
		GLFW.glfwSetKeyCallback(id, keyHandler);
	}

	public void setPosition(float posX, float posY, int[] positionUnits) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.position.x = posX;
		this.position.y = posY;
		
		updatePosition();
	}

	public void setPosition(Vec2f position, int[] positionUnits) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.position = PE.toPixels(position, positionUnits, monitorSize, rpixRatio);
		
		updatePosition();
	}

	public void setSize(Vec2f size, int[] sizeUnits) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.size = PE.toPixels(size, sizeUnits, monitorSize, rpixRatio);

		updateSize();
	}

	public void setSize(float width, float height, int[] sizeUnits) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.size.x = PE.toPixels(width, sizeUnits[0], monitorSize.x, rpixRatio);
		this.size.y = PE.toPixels(height, sizeUnits[1], monitorSize.y, rpixRatio);

		updateSize();
	}

	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(id, title);
		this.title = title;
	}

	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		if (vsync) {
			GLFW.glfwSwapInterval(1);
		} else {
			GLFW.glfwSwapInterval(0);
		}
	}

	public void setWidth(float width, int widthUnits) {
		this.size.x = PE.toPixels(width, widthUnits, monitorSize.x, rpixRatio);
		
		updateSize();
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(id);
	}

	public void show() {
		GLFW.glfwMakeContextCurrent(id);
	}

	public void update() {
		GLFW.glfwSwapBuffers(id);
		GLFW.glfwPollEvents();
	}

	/**
	 * Updates the position of the Window. Generally only used internally, but
	 * may be necessary if the position is changed without the use of methods
	 * such as <code>setPosition</code>.
	 * 
	 * @since 1.0
	 */
	public void updatePosition() {
		GLFW.glfwSetWindowPos(id, (int) position.x, (int) position.y);
		generateMonitorStats();
	}

	public void updateSize() {
		GLFW.glfwSetWindowSize(id, (int) size.x, (int) size.y);
		generateOrthoProjection();
	}
}
