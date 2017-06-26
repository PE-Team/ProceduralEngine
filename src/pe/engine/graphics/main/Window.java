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
import pe.engine.graphics.gui.properties.RPixSourceI;
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
import pe.engine.main.UnitConversions;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;

public class Window implements RPixSourceI, DisposableResourceI{

	private WindowHandler windowHandler;
	private Vec2f monitorSize = Vec2f.ZERO; // Always in pixels
	private Vec2f mousePosition = Vec2f.ZERO;
	private float rpixRatio = 1;
	private Vec2f size = new Vec2f(1, 1);
	private Vec2f position = Vec2f.ZERO;
	private Vec2f center = Vec2f.ZERO;
	private long id;
	private GUI gui = null;
	private Mat4f orthoProjection = null;

	private int[] sizeUnits = { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS };
	private int[] positionUnits = { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS };
	private int[] centerUnits = { PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT };

	public Window(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, Vec2f center, int[] centerUnits, String title, boolean vsync,
			boolean resizeable, boolean border) {

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
	
	public void setGUI(GUI gui){
		this.gui = gui;
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
		float widthPix = UnitConversions.toPixels(size.x, sizeUnits[0], monitorSize.x, rpixRatio);
		float heightPix = UnitConversions.toPixels(size.y, sizeUnits[1], monitorSize.y, rpixRatio);
				
		this.orthoProjection = Mat4f.getOrthographicMatrix(0, widthPix, heightPix, 0, -1, 1);
	}

	/**
	 * Returns the height of this <code>Window</code> object in the units of
	 * <code>sizeUnits</code>.
	 * 
	 * @return The height of this window.
	 * 
	 * @see #size
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public float getHeight() {
		return size.y;
	}

	/**
	 * Returns the currently used units of the height of the window.
	 * 
	 * @return the units of the height for the window.
	 */
	public int getHeightUnits() {
		return sizeUnits[1];
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
		float pixValue = UnitConversions.toPixels(size.y, sizeUnits[1], monitorSize.y, rpixRatio);
		return UnitConversions.convertFromPix(pixValue, units, monitorSize.y, rpixRatio);
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

	/**
	 * Returns the width of this <code>Window</code> object in the units of
	 * <code>sizeUnits</code>.
	 * 
	 * @return The width of this window.
	 * 
	 * @see #size
	 * @see #sizeUnits
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
	 * @see #size
	 * 
	 * @since 1.0
	 */
	public float getWidth(int units) {
		float pixValue = UnitConversions.toPixels(size.x, sizeUnits[0], monitorSize.x, rpixRatio);
		return UnitConversions.convertFromPix(pixValue, units, monitorSize.x, rpixRatio);
	}

	/**
	 * Returns the currently used units for the width of the Window.
	 * 
	 * @return The units for the width.
	 * 
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public int getWidthUnits() {
		return sizeUnits[0];
	}

	public Vec2f getSizePix() {
		return UnitConversions.toPixels(size, sizeUnits, monitorSize, rpixRatio);
	}
	
	public Vec2f getCenterPix(){
		return UnitConversions.toPixels(center, centerUnits, getSizePix(), rpixRatio);
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
		this.size.y = height;
		this.sizeUnits[1] = units;

		updateSize();
	}

	/**
	 * Sets the height of the Window. Does not override the units for the
	 * height, but instead converts from the units given to the currently
	 * assigned units.
	 * 
	 * @param height
	 *            The new height of the Window in the given units.
	 * @param units
	 *            The units for the given height.
	 * 
	 * @see #size
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public void setHeightValue(float height, int units) {
		float heightPix = UnitConversions.toPixels(height, units, monitorSize.y, rpixRatio);
		size.y = UnitConversions.convertFromPix(heightPix, sizeUnits[1], monitorSize.y, rpixRatio);

		updateSize();
	}
	
	public void setWindowHandler(WindowHandler windowHandler){
		this.windowHandler = windowHandler;
		windowHandler.setWindow(this);
	}

	public void setKeyHandler(WindowKeyHandler keyHandler) {
		keyHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetKeyCallback(id, keyHandler);
	}
	
	public void fireInputEvent(WindowInputEvent e){
		gui.invokeInputEvent(e);
	}
	
	public void setMousePositionValues(float posX, float posY){
		this.mousePosition.x = posX;
		this.mousePosition.y = posY;
	}
	
	public void setScrollHandler(WindowScrollHandler scrollHandler){
		scrollHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetScrollCallback(id, scrollHandler);
	}
	
	public void setMouseButtonHandler(WindowMouseButtonHandler mouseButtonHandler){
		mouseButtonHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetMouseButtonCallback(id, mouseButtonHandler);
	}
	
	public void setMousePositionHandler(WindowMousePositionHandler mousePosHandler){
		mousePosHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetCursorPosCallback(id, mousePosHandler);
	}

	public void setFrameSizeHandler(WindowFrameSizeHandler sizeHandler) {
		sizeHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetFramebufferSizeCallback(id, sizeHandler);
	}

	public void setPositionHandler(WindowPositionHandler posHandler) {
		posHandler.setWindowHanlder(windowHandler);
		GLFW.glfwSetWindowPosCallback(id, posHandler);
	}
	
	public void setCenter(Vec2f center, int[] centerUnits){
		if (centerUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.center = center;
		this.centerUnits = centerUnits;
		
		updatePosition();
	}
	
	public void setPosition(Vec2f position, int[] units) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.position = position;
		this.positionUnits = units;

		updatePosition();
	}
	
	public void setPosition(float posX, float posY, int[] units) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		this.position.x = posX;
		this.position.y = posY;
		this.positionUnits = units;

		updatePosition();
	}

	public void setPositionValue(Vec2f position, int[] units) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		Vec2f positionPix = UnitConversions.toPixels(position, units, monitorSize, rpixRatio);
		this.position = UnitConversions.convertFromPix(positionPix, positionUnits, monitorSize, rpixRatio);

		updatePosition();
	}
	
	public void setPositionValue(float posX, float posY, int[] units) {
		if (positionUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		float posXPix = UnitConversions.toPixels(posX, units[0], monitorSize.x, rpixRatio);
		position.x = UnitConversions.convertFromPix(posXPix, positionUnits[0], monitorSize.x, rpixRatio);
		
		float posYPix = UnitConversions.toPixels(posY, units[1], monitorSize.y, rpixRatio);
		position.y = UnitConversions.convertFromPix(posYPix, positionUnits[1], monitorSize.y, rpixRatio);

		updatePosition();
	}
	
	public void setSize(Vec2f size, int[] units) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		this.size = size;
		this.sizeUnits = units;

		updateSize();
	}

	public void setSize(float width, float height, int[] units) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		this.size.x = width;
		this.size.y = height;
		this.sizeUnits = units;

		updateSize();
	}

	public void setSizeValue(Vec2f size, int[] units) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		Vec2f sizePix = UnitConversions.toPixels(size, units, monitorSize, rpixRatio);
		this.size = UnitConversions.convertFromPix(sizePix, sizeUnits, monitorSize, rpixRatio);

		updateSize();
	}

	public void setSizeValue(float width, float height, int[] units) {
		if (sizeUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float widthPix = UnitConversions.toPixels(width, units[0], monitorSize.x, rpixRatio);
		size.x = UnitConversions.convertFromPix(widthPix, sizeUnits[0], monitorSize.x, rpixRatio);
		
		float heightPix = UnitConversions.toPixels(height, units[1], monitorSize.y, rpixRatio);
		size.y = UnitConversions.convertFromPix(heightPix, sizeUnits[1], monitorSize.y, rpixRatio);

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
		this.size.x = width;
		this.sizeUnits[0] = units;

		updateSize();
	}

	/**
	 * Sets the width of the Window. Does not override the units for the
	 * width, but instead converts from the units given to the currently
	 * assigned units.
	 * 
	 * @param height
	 *            The new width of the Window in the given units.
	 * @param units
	 *            The units for the given width.
	 * 
	 * @see #size
	 * @see #sizeUnits
	 * 
	 * @since 1.0
	 */
	public void setWidthValue(float width, int units) {
		float widthPix = UnitConversions.toPixels(width, units, monitorSize.x, rpixRatio);
		size.y = UnitConversions.convertFromPix(widthPix, sizeUnits[0], monitorSize.x, rpixRatio);

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
		if(windowHandler != null)
			windowHandler.update();
	}

	/**
	 * Updates the position of the Window. Generally only used internally, but
	 * may be necessary if the position is changed without the use of methods
	 * such as <code>setPosition</code>.
	 * 
	 * @since 1.0
	 */
	public void updatePosition() {
		Vec2f sizePix = getSizePix();
		int posX = (int) UnitConversions.toPixels(position.x, positionUnits[0], monitorSize.x, rpixRatio);
		int posY = (int) UnitConversions.toPixels(position.y, positionUnits[1], monitorSize.y, rpixRatio);
		
		int centerX = (int) UnitConversions.toPixels(center.x, centerUnits[0], sizePix.x, rpixRatio);
		int centerY = (int) UnitConversions.toPixels(center.y, centerUnits[1], sizePix.y, rpixRatio);
		
		GLFW.glfwSetWindowPos(id, posX - centerX, posY - centerY);
		generateMonitorStats();
	}

	/**
	 * Updates the size of the Window. Generally only used internally, but
	 * may be necessary if the position is changed without the use of methods
	 * such as <code>setSize</code>.
	 * 
	 * @since 1.0
	 */
	public void updateSize() {
		int width = (int) UnitConversions.toPixels(size.x, sizeUnits[0], monitorSize.x, rpixRatio);
		int height = (int) UnitConversions.toPixels(size.y, sizeUnits[1], monitorSize.y, rpixRatio);

		GLFW.glfwSetWindowSize(id, width, height);
		GL11.glViewport(0, 0, width, height);
		generateOrthoProjection();
	}
}
