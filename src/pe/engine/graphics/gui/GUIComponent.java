package pe.engine.graphics.gui;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import pe.engine.data.TextureArrayObject;
import pe.engine.data.TextureSwapArray;
import pe.engine.graphics.gui.properties.RotationProperty;
import pe.engine.graphics.gui.properties.Unit2Property;
import pe.engine.graphics.gui.properties.Unit4Property;
import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.graphics.objects.Mesh;
import pe.engine.graphics.objects.StaticMesh2D;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.engine.shader.shaders.core.CoreShaderPaths;
import pe.util.Util;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Polygon;

public abstract class GUIComponent {

	protected static final String[] TAO_UNIFORM_NAMES = { "foregroundTexture", "backgroundTexture" };
	
	protected static final int FOREGROUND_TEXTURE_INDEX = 0;
	protected static final int BACKGROUND_TEXTURE_INDEX = 1;
	
	protected static ShaderProgram shaderProgram = null;

	// @formatter:off
	protected Unit2Property size = Unit2Property.createFullPercent();
	protected Unit2Property position = Unit2Property.createZeroPixel();
	protected Unit2Property center = Unit2Property.createHalfPercent();
	protected RotationProperty rotation = new RotationProperty();
	protected Color backgroundColor = Color.CLEAR;
	protected Unit4Property borderWidth = Unit4Property.createZeroPixel();		// {TOP, RIGHT, BOTTOM, LEFT}
	protected Color borderColor = Color.CLEAR;
	protected Unit4Property borderRadius = Unit4Property.createZeroPixel();	// {TOP-LEFT, TOP-RIGHT, BOTTOM-RIGHT, BOTTOM-LEFT}
	protected String text = "";
	protected Color textColor = Color.CLEAR;
	protected Polygon shape = null;
	protected Mesh mesh = null;
	protected TextureSwapArray tsa = null;
	protected boolean autoUnloadTexture = false;
	protected GUI gui = null;
	protected GUIComponent parent = null;
	protected List<GUIComponent> children = new ArrayList<GUIComponent>();
	// @formatter:on

	public GUIComponent(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, Vec2f center,
			int[] centerUnits, float rotation, Color backgroundColor, Vec4f borderWidth, int[] borderWidthUnits,
			Color borderColor, Vec4f borderRadius, int[] borderRadiusUnits, String text, Color textColor,
			Polygon shape) {
		this.size.set(size, sizeUnits);
		this.position.set(position, positionUnits);
		this.center.set(center, centerUnits);
		this.rotation = new RotationProperty(rotation, PE.ANGLE_UNIT_DEGREES);
		this.backgroundColor = backgroundColor;
		this.borderWidth.set(borderWidth, borderWidthUnits);
		this.borderColor = borderColor;
		this.borderRadius.set(borderRadius, borderRadiusUnits);
		this.text = text;
		this.textColor = textColor;
		this.shape = shape;
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		this.tsa = new TextureSwapArray();

		initMeshProperties();
		initShaderProgram();
		initTextures();
	}

	protected void initMeshProperties() {
		Vec2f v0Border = new Vec2f(0, 1);
		Vec2f v1Border = new Vec2f(1, 1);
		Vec2f v2Border = new Vec2f(1, 0);
		Vec2f v3Border = new Vec2f(0, 0);

		FloatBuffer borderVectors = BufferUtils.createFloatBuffer(8);

		v0Border.putInBuffer(borderVectors);
		v1Border.putInBuffer(borderVectors);
		v2Border.putInBuffer(borderVectors);
		v3Border.putInBuffer(borderVectors);
		borderVectors.flip();
		mesh.addShaderAttrib(2, borderVectors);

		mesh.setWireframe(false);
	}

	protected void initShaderProgram() {
		if (shaderProgram != null)
			return;

		Shader vertexShader = new Shader(PE.SHADER_TYPE_VERTEX, CoreShaderPaths.GUI_COMPONENT_VERTEX);
		vertexShader.compile();
		vertexShader.compileStatus();

		Shader geometryShader = new Shader(PE.SHADER_TYPE_GEOMETRY, CoreShaderPaths.GUI_COMPONENT_GEOMETRY);
		geometryShader.compile();
		geometryShader.compileStatus();

		Shader fragmentShader = new Shader(PE.SHADER_TYPE_FRAGMENT, CoreShaderPaths.GUI_COMPONENT_FRAGMENT);
		fragmentShader.compile();
		fragmentShader.compileStatus();

		ShaderProgram program = new ShaderProgram();
		program.addShader(vertexShader);
		program.addShader(geometryShader);
		program.addShader(fragmentShader);

		program.setDefaultFragOutValue("color", 0);

		program.setAttribIndex(0, "position");
		program.setAttribIndex(1, "texCoord");

		program.compile();
		program.compileStatus();

		shaderProgram = program;
	}

	protected void initTextures() {
		TextureArrayObject taoDefault = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoPress = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoRelease = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoClick = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoDrag = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoHover = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoType = TextureArrayObject.fillStaticTexture2DClear(2);
		TextureArrayObject taoScroll = TextureArrayObject.fillStaticTexture2DClear(2);
		
		tsa.add(taoDefault, PE.GUI_EVENT_DEFAULT);
		tsa.add(taoPress, PE.GUI_EVENT_ON_PRESS);
		tsa.add(taoRelease, PE.GUI_EVENT_ON_RELEASE);
		tsa.add(taoClick, PE.GUI_EVENT_ON_CLICK);
		tsa.add(taoDrag, PE.GUI_EVENT_ON_DRAG);
		tsa.add(taoHover, PE.GUI_EVENT_ON_HOVER);
		tsa.add(taoType, PE.GUI_EVENT_ON_TYPE);
		tsa.add(taoScroll, PE.GUI_EVENT_ON_SCROLL);
		
		tsa.autoUnload(true);
		tsa.swap(PE.GUI_EVENT_DEFAULT);
	}

	public GUIComponent() {

	};

	public void setGUI(GUI gui) {
		this.gui = gui;

		updateProperties();
	}

	protected void updateSelfProperties() {
		Window window = gui.getWindow();

		this.size.setMaxValue(parent.getSize()).setRPixSource(window);
		this.position.setMaxValue(parent.getSize()).setRPixSource(window);
		this.center.setMaxValue(size).setRPixSource(window);
		this.borderWidth.setMaxValue(size).setRPixSource(window);
		this.borderRadius.setMaxValue(size).setRPixSource(window);
	}

	public void updateProperties() {
		updateSelfProperties();

		for (GUIComponent child : children) {
			child.updateProperties();
		}
	}

	/**
	 * This event is fired whenever a mouse button is pressed when over this
	 * widget.
	 * 
	 * @param e
	 *            The <code>WindowInputEvent</code> object which contains
	 *            information specific to this event as well as the
	 *            <code>WindowHandler</code> which fired the event.
	 * 
	 * @return Whether this event handler did anything with the event
	 */
	protected boolean onPress(WindowInputEvent e) {
		System.out.println("PRESSING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_PRESS);
		return true;
	}

	/**
	 * This event is fired whenever a mouse button is released when over this
	 * widget.
	 * 
	 * @param e
	 *            The <code>WindowInputEvent</code> object which contains
	 *            information specific to this event as well as the
	 *            <code>WindowHandler</code> which fired the event.
	 * 
	 * @return Whether this event handler did anything with the event
	 */
	protected boolean onRelease(WindowInputEvent e) {
		System.out.println("RELEASING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_RELEASE);
		return true;
	}

	/**
	 * This event is fired whenever a mouse button is both pressed and released
	 * over this widget. The mouse may leave the widget, but the start of the
	 * click and the end of the click must be over the widget.
	 * 
	 * @param e
	 *            The <code>WindowInputEvent</code> object which contains
	 *            information specific to this event as well as the
	 *            <code>WindowHandler</code> which fired the event.
	 * 
	 * @return Whether this event handler did anything with the event
	 */
	protected boolean onClick(WindowInputEvent e) {
		System.out.println("CLICKING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_CLICK);
		return true;
	}

	protected boolean onScroll(WindowInputEvent e) {
		System.out.println("SCROLLING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_SCROLL);
		return true;
	}

	protected boolean onDrag(WindowInputEvent e) {
		System.out.println("DRAGGING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_DRAG);
		return false;
	}

	protected boolean onHover(WindowInputEvent e) {
		System.out.println("HOVERING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_HOVER);
		return false;
	}

	protected boolean onType(WindowInputEvent e) {
		System.out.println("TYPING WOKRING!");
		tsa.swap(PE.GUI_EVENT_ON_TYPE);
		return false;
	}

	protected boolean invokeInputEvent(WindowInputEvent e, boolean disposed) {
		boolean isDisposed = invokeChildrenInputEvent(e, disposed);

		return invokeSelfInputEvent(e, isDisposed);
	}

	protected boolean invokeChildrenInputEvent(WindowInputEvent e, boolean disposed) {
		boolean isDisposed = disposed;
		for (GUIComponent child : children) {
			isDisposed = child.invokeInputEvent(e, isDisposed);
		}
		return isDisposed;
	}

	protected boolean invokeSelfInputEvent(WindowInputEvent e, boolean disposed) {

		Vec2f sizePix = size.pixels();
		Vec2f centerPix = center.pixels();
		float rotationDeg = rotation.degrees();
		Vec2f positionPix = position.pixels();

		if (disposed)
			return true;

		if (Util.isInRectangle(positionPix, sizePix, centerPix, rotationDeg, e.getPosition())) {
			int action = e.getAction();
			boolean isDisposed = false;

			if (action == PE.MOUSE_ACTION_PRESS)
				isDisposed = isDisposed | onPress(e);

			if (action == PE.MOUSE_ACTION_RELEASE)
				isDisposed = isDisposed | onRelease(e);

			if (action == PE.MOUSE_ACTION_RELEASE
					&& Util.isInRectangle(positionPix, sizePix, centerPix, rotationDeg, e.getDelta()))
				isDisposed = isDisposed | onClick(e);

			if (action == PE.MOUSE_ACTION_SCROLL)
				isDisposed = isDisposed | onScroll(e);

			if (action == PE.MOUSE_ACTION_DRAG && e.getWindowHandler().getMouseButtonState(PE.MOUSE_BUTTON_LEFT))
				isDisposed = isDisposed | onDrag(e);

			if (action == PE.MOUSE_ACTION_HOVER)
				isDisposed = isDisposed | onHover(e);

			return isDisposed;
		}

		return false;
	}

	public void setRotation(float degrees) {
		this.rotation.set(degrees);
	}

	public RotationProperty getRotation() {
		return rotation;
	}

	public void rotate(float degrees) {
		this.rotation.increase(degrees);
	}

	public void addChild(GUIComponent child) {
		this.children.add(child);
		child.setParent(this);
	}

	public void removeChild(GUIComponent child) {
		this.children.remove(child);
		child.setParent(null);
	}

	public void setParent(GUIComponent parent) {
		this.parent = parent;
	}

	public Unit2Property getSize() {
		return size;
	}

	public Unit2Property getCenter() {
		return center;
	}

	public Unit2Property getPosition() {
		return position;
	}

	public Unit4Property getBorderWidth() {
		return borderWidth;
	}

	public Unit4Property getBorderRadius() {
		return borderRadius;
	}

	public void render() {
		shaderProgram.start();
		tsa.bind();

		Vec2f sizePix = size.pixels();
		Vec2f centerPix = center.pixels();
		float rotationDeg = rotation.degrees();
		Vec2f positionPix = position.pixels();
		Vec4f borderWidthPix = borderWidth.pixels();
		Vec4f borderRadiusPix = borderRadius.pixels();

		Vec2f scale = new Vec2f(sizePix.x / shape.getWidth(), sizePix.y / shape.getHeight());
		Mat3f transformation = new Mat3f().scale(scale).translate(centerPix.mul(-1)).rotate(rotationDeg)
				.translate(centerPix.mul(-1)).translate(positionPix);
		Mat4f projection = gui.getWindow().getOrthoProjection();

		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformFloat("width", sizePix.x);
		shaderProgram.setUniformFloat("height", sizePix.y);
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformVec4f("borderWidth", borderWidthPix);
		shaderProgram.setUniformColor("borderColor", borderColor);
		shaderProgram.setUniformVec4f("borderRadius", borderRadiusPix);
		shaderProgram.setUniformTAO(TAO_UNIFORM_NAMES, tsa.get());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mesh.render();
		GL11.glDisable(GL11.GL_BLEND);

		tsa.unbind();
		shaderProgram.stop();

		renderChildren();
	}

	public void renderChildren() {
		for (GUIComponent child : children) {
			child.render();
		}
	}
}
