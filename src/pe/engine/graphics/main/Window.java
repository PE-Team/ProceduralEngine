package pe.engine.graphics.main;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import pe.engine.data.DisposableResourceI;
import pe.engine.data.Resources;
import pe.engine.graphics.gui.GUI;
import pe.engine.graphics.gui.GUINew;
import pe.engine.graphics.gui.properties.RPixSourceI;
import pe.engine.graphics.gui.properties.Unit2Property;
import pe.engine.graphics.main.handlers.WindowFocusHandler;
import pe.engine.graphics.main.handlers.WindowFrameSizeHandler;
import pe.engine.graphics.main.handlers.WindowHandler;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.graphics.main.handlers.WindowKeyHandler;
import pe.engine.graphics.main.handlers.WindowMouseButtonHandler;
import pe.engine.graphics.main.handlers.WindowMousePositionHandler;
import pe.engine.graphics.main.handlers.WindowPositionHandler;
import pe.engine.graphics.main.handlers.WindowScrollHandler;
import pe.engine.main.GLVersion;
import pe.engine.main.PE;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;

public class Window implements RPixSourceI, DisposableResourceI {

	private WindowHandler windowHandler;
	private Unit2Property monitorSize = Unit2Property.createZeroPixel(); // Always
																			// in
																			// pixels
	private Unit2Property mousePosition = Unit2Property.createZeroPixel();
	private float rpixRatio = 1;
	private Unit2Property size = Unit2Property.createHalfPercent();
	private Unit2Property position = Unit2Property.createHalfPercent();
	private Unit2Property center = Unit2Property.createHalfPercent();
	private long id;
	private GUI gui = null;
	private GUINew guiNew = null;
	private Mat4f orthoProjection = null;
	private boolean inputEventFired = false;

	public Window() {
		this(new Vec2f(1.0f, 1.0f), new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS }, new Vec2f(0.0f, 0.0f),
				new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS }, new Vec2f(0.5f, 0.5f),
				new int[] { PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT }, "NULL", true, false, false);
	}

	public Window(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, Vec2f center, int[] centerUnits,
			String title, boolean vsync, boolean resizeable, boolean border) {

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

		show();
		GL.createCapabilities();

		generateMonitorStats();
		updateProperties();
		setSize(size, sizeUnits);
		generateOrthoProjection();
		setPosition(position, positionUnits);
		setCenter(center, centerUnits);
		setVSync(vsync);

		Resources.add(this);

		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		update();
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	public void setGUINew(GUINew gui) {
		this.guiNew = gui;
	}

	public void updateProperties() {
		mousePosition.setMaxValue(monitorSize).setRPixSource(this);
		size.setMaxValue(monitorSize).setRPixSource(this);
		position.setMaxValue(monitorSize).setRPixSource(this);
		center.setMaxValue(size).setRPixSource(this);
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
		this.monitorSize.set(new Vec2f(videoMode.width(), videoMode.height()));
		this.rpixRatio = videoMode.width() * 1f / pysicalSize.get(0);
	}

	public void generateOrthoProjection() {
		Vec2f sizePix = size.pixels();

		this.orthoProjection = Mat4f.getOrthographicMatrix(0, sizePix.x, sizePix.y, 0, -1, 1);
		// this.orthoProjection = Mat4f.getOrthographicMatrix(0, 300, 300, 0,
		// -1, 1);
	}

	public Unit2Property getSize() {
		return size;
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

	public Unit2Property getCenter() {
		return center;
	}

	/**
	 * Sets the height of the Window. Overrides both the old height and the old
	 * units for the height.
	 * 
	 * @param height
	 *            The new height of the Window in the new units.
	 * @param units
	 *            The new units for the height of the Window.
	 * 
	 * @see #size
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public void setHeight(float height, int units) {
		this.size.getValue().y = height;
		this.size.getUnits()[1] = units;

		updateSize();
	}

	public void setWindowHandler(WindowHandler windowHandler) {
		this.windowHandler = windowHandler;
		windowHandler.setWindow(this);
	}

	public void setKeyHandler(WindowKeyHandler keyHandler) {
		keyHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetKeyCallback(id, keyHandler);
	}

	public void fireInputEvent(WindowInputEvent e) {
		this.inputEventFired = true;
		gui.invokeInputEvent(e);
	}

	public void setMousePositionValues(float posX, float posY) {
		this.mousePosition.getValue().x = posX;
		this.mousePosition.getValue().y = posY;
	}

	public void setScrollHandler(WindowScrollHandler scrollHandler) {
		scrollHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetScrollCallback(id, scrollHandler);
	}

	public void setMouseButtonHandler(WindowMouseButtonHandler mouseButtonHandler) {
		mouseButtonHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetMouseButtonCallback(id, mouseButtonHandler);
	}

	public void setMousePositionHandler(WindowMousePositionHandler mousePosHandler) {
		mousePosHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetCursorPosCallback(id, mousePosHandler);
	}

	public void setFrameSizeHandler(WindowFrameSizeHandler sizeHandler) {
		sizeHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetFramebufferSizeCallback(id, sizeHandler);
	}

	public void setPositionHandler(WindowPositionHandler posHandler) {
		posHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetWindowPosCallback(id, posHandler);
	}

	public void setFocusHandler(WindowFocusHandler focusHandler) {
		focusHandler.setWindowHandler(windowHandler);
		GLFW.glfwSetWindowFocusCallback(id, focusHandler);
	}

	public void setCenter(Vec2f center, int[] units) {
		this.center.set(center, units);

		updatePosition();
	}

	public void setPosition(Vec2f position, int[] units) {
		this.position.set(position, units);

		updatePosition();
	}

	public void setPosition(float posX, float posY, int[] units) {
		this.position.set(new Vec2f(posX, posY), units);

		updatePosition();
	}

	public void setSize(Vec2f size, int[] units) {
		this.size.set(size, units);

		updateSize();
	}
	
	public void hide(){
		GLFW.glfwMakeContextCurrent(0);
	}

	public void setSize(float width, float height, int[] units) {
		this.size.set(new Vec2f(width, height), units);

		updateSize();
	}

	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(id, title);
	}

	public void setVSync(boolean vsync) {
		if (vsync) {
			GLFW.glfwSwapInterval(1);
		} else {
			GLFW.glfwSwapInterval(0);
		}
	}

	/**
	 * Sets the width of the Window. Overrides both the old width and the old
	 * units for the width.
	 * 
	 * @param height
	 *            The new width of the Window in the new units.
	 * @param units
	 *            The new units for the width of the Window.
	 * 
	 * @see #size
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public void setWidth(float width, int units) {
		this.size.getValue().x = width;
		this.size.getUnits()[1] = units;

		updateSize();
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(id);
	}

	public void show() {
		GLFW.glfwMakeContextCurrent(id);
	}

	public void update() {
		this.inputEventFired = false;
		GLFW.glfwSwapBuffers(id);
		GLFW.glfwPollEvents();
		if (windowHandler != null)
			windowHandler.update();

		if (!inputEventFired && gui != null && windowHandler != null)
			fireInputEvent(new WindowInputEvent(windowHandler, PE.NULL, PE.NULL));
	}

	/**
	 * Updates the position of the Window. Generally only used internally, but
	 * may be necessary if the position is changed without the use of methods
	 * such as <code>setPosition</code>.
	 * 
	 * @since 1.0
	 */
	public void updatePosition() {
		Vec2f posPix = position.pixels();
		Vec2f centerPix = center.pixels();

		GLFW.glfwSetWindowPos(id, (int) (posPix.x - centerPix.x), (int) (posPix.y - centerPix.y));
		generateMonitorStats();
	}

	/**
	 * Updates the size of the Window. Generally only used internally, but may
	 * be necessary if the position is changed without the use of methods such
	 * as <code>setSize</code>.
	 * 
	 * @since 1.0
	 */
	public void updateSize() {
		Vec2f sizePix = size.pixels();
		int width = (int) sizePix.x;
		int height = (int) sizePix.y;

		GLFW.glfwSetWindowSize(id, width, height);
		GL11.glViewport(0, 0, width, height);
		generateOrthoProjection();
	}
}
