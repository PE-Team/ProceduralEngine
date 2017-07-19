package pe.engine.graphics.gui;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import pe.engine.data.FrameBufferObject;
import pe.engine.data.TextureArrayObject;
import pe.engine.data.TextureSwapArray;
import pe.engine.graphics.gui.properties.RotationProperty;
import pe.engine.graphics.gui.properties.Unit2Property;
import pe.engine.graphics.gui.properties.Unit4Property;
import pe.engine.graphics.main.Window;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.graphics.objects.StaticMesh2D;
import pe.engine.graphics.objects.Texture;
import pe.engine.graphics.objects.Texture2D;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.engine.shader.shaders.core.CoreShaders;
import pe.util.Util;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Rectangle;

public class GUIComponentNew extends GUIRenderable {

	protected static final String[] TAO_UNIFORM_NAMES = { "foregroundTexture", "backgroundTexture" };

	protected static final int FOREGROUND_TEXTURE_INDEX = 0;
	protected static final int BACKGROUND_TEXTURE_INDEX = 1;

	protected static ShaderProgram backgroundProgram = null;
	protected static ShaderProgram contentProgram = null;
	protected static ShaderProgram foregroundProgram = null;

	protected Unit2Property position;
	protected RotationProperty rotation;
	protected Unit2Property rotationOffset;
	protected Unit4Property margin;
	protected Color backgroundColor;
	protected Unit4Property borderWidth; // top, right, bottom, left
	protected Unit4Property borderRadius; // top-left, top-right, bottom-right,
											// bottom-left
	protected Color borderColor;
	protected Unit4Property padding;

	protected TextureSwapArray tsa;

	protected List<GUIRenderable> children;
	protected boolean clipChildren;

	

	public GUIComponentNew(Color backgroundColor, Color borderColor, Vec4f borderRadius, Vec4f borderWidth,
			boolean clipChildren, Vec4f margin, Vec4f padding, Vec2f position, Vec2f positionOffset, float rotation,
			Vec2f rotationOffset, Vec2f size, float zIndex) {

		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderRadius = new Unit4Property(borderRadius,
				new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.borderWidth = new Unit4Property(borderWidth,
				new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.children = new ArrayList<GUIRenderable>();
		this.clipChildren = clipChildren;
		this.fbo = new FrameBufferObject();
		this.gui = null;
		this.margin = new Unit4Property(margin,
				new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.padding = new Unit4Property(padding,
				new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.parent = null;
		this.position = new Unit2Property(position, new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.positionOffset = new Unit2Property(positionOffset, new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.rotation = new RotationProperty(rotation, PE.ANGLE_UNIT_DEGREES);
		this.rotationOffset = new Unit2Property(rotationOffset, new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.size = new Unit2Property(size, new int[] { PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS });
		this.treeTier = -1;
		this.tsa = null;
		this.zIndex = 0;

		initShaderPrograms();
		initMeshProperties();
		initTextures();
	}

	protected void initShaderPrograms() {

		if (backgroundProgram != null)
			return;

		backgroundProgram = CoreShaders.guiBackgroundProgram();

		foregroundProgram = CoreShaders.guiForegroundProgram();

		// ShaderProgram programF = new ShaderProgram();
		// programF.addShader(CoreShaders.GUI_FOREGROUND_VERTEX);
		// programF.addShader(CoreShaders.GUI_FOREGROUND_FRAGMENT);
		// programF.setDefaultFragOutValue("color", 0);
		// programF.setAttribIndex(0, "position");
		// programF.setAttribIndex(0, "textureCoord");
		// programF.compile();
		// programF.compileStatus();
		//
		// contentProgram = programF;
	}

	protected void initTextures() {
		tsa = new TextureSwapArray();

		TextureArrayObject taoDefault = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoPress = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoRelease = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoClick = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoDrag = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoHover = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoType = TextureArrayObject.fillTexture2DClear(2);
		TextureArrayObject taoScroll = TextureArrayObject.fillTexture2DClear(2);

		tsa.add(taoDefault, PE.GUI_EVENT_DEFAULT);
		tsa.add(taoPress, PE.GUI_EVENT_ON_PRESS);
		tsa.add(taoRelease, PE.GUI_EVENT_ON_RELEASE);
		tsa.add(taoClick, PE.GUI_EVENT_ON_CLICK);
		tsa.add(taoDrag, PE.GUI_EVENT_ON_DRAG);
		tsa.add(taoHover, PE.GUI_EVENT_ON_HOVER);
		tsa.add(taoType, PE.GUI_EVENT_ON_TYPE);
		tsa.add(taoScroll, PE.GUI_EVENT_ON_SCROLL);
		tsa.loadAll();
		tsa.swap(PE.GUI_EVENT_DEFAULT);
	}

	@Override
	protected void updateSelfProperties() {
		Window window = gui == null ? null : gui.getWindow();

		Unit2Property parentSize = gui == null ? null
				: (parent == null ? (window == null ? null : window.getSize()) : parent.getSize());

		this.size.setMaxValue(parentSize).setRPixSource(window);
		this.position.setMaxValue(parentSize).setRPixSource(window);
		this.positionOffset.setMaxValue(size).setRPixSource(window);
		this.rotationOffset.setMaxValue(size).setRPixSource(window);
		this.borderWidth.setMaxValue(size).setRPixSource(window);
		this.borderRadius.setMaxValue(size).setRPixSource(window);
	}

	@Override
	public void updateProperties() {
		updateSelfProperties();

		for (GUIRenderable child : children) {
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

	/**
	 * This event is fired whenever a mouse button is released when not over
	 * this widget.
	 * 
	 * @param e
	 *            The <code>WindowInputEvent</code> object which contains
	 *            information specific to this event as well as the
	 *            <code>WindowHandler</code> which fired the event.
	 * 
	 * @return Whether this event handler did anything with the event
	 */
	protected boolean onOutsideRelease(WindowInputEvent e) {
		System.out.println("OUTSIDE RELEASING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_DEFAULT);
		return false;
	}

	protected boolean invokeInputEvent(WindowInputEvent e, boolean disposed) {
		boolean isDisposed = invokeChildrenInputEvent(e, disposed);

		return invokeSelfInputEvent(e, isDisposed);
	}

	protected boolean invokeChildrenInputEvent(WindowInputEvent e, boolean disposed) {
		boolean isDisposed = disposed;

		ListIterator<GUIRenderable> iterator = children.listIterator(children.size());
		while (iterator.hasPrevious()) {
			GUIRenderable comp = iterator.previous();
			if (comp instanceof GUIComponentNew) {
				isDisposed = ((GUIComponentNew) comp).invokeInputEvent(e, isDisposed);
			}
		}
		return isDisposed;
	}

	protected boolean invokeSelfInputEvent(WindowInputEvent e, boolean disposed) {

		Vec2f sizePix = size.pixels();
		Vec2f rotationOffsetPix = rotationOffset.pixels();
		float rotationDeg = rotation.degrees();
		Vec2f positionPix = position.pixels();
		Vec2f positionOffsetPix = positionOffset.pixels();

		if (disposed)
			return true;

		int action = e.getAction();
		boolean isDisposed = false;
		if (Util.isInRectangle(positionPix, positionOffsetPix, sizePix, rotationOffsetPix, rotationDeg,
				e.getPosition())) {

			if (action == PE.MOUSE_ACTION_PRESS)
				isDisposed = isDisposed | onPress(e);

			if (action == PE.MOUSE_ACTION_RELEASE)
				isDisposed = isDisposed | onRelease(e);

			if (action == PE.MOUSE_ACTION_RELEASE && Util.isInRectangle(positionPix, positionOffsetPix, sizePix,
					rotationOffsetPix, rotationDeg, e.getDelta()))
				isDisposed = isDisposed | onClick(e);

			if (action == PE.MOUSE_ACTION_SCROLL)
				isDisposed = isDisposed | onScroll(e);

			if (action == PE.MOUSE_ACTION_DRAG && e.getWindowHandler().getMouseButtonState(PE.MOUSE_BUTTON_LEFT))
				isDisposed = isDisposed | onDrag(e);

			if (action == PE.MOUSE_ACTION_HOVER)
				isDisposed = isDisposed | onHover(e);

			return isDisposed;
		} else {
			if (action == PE.MOUSE_ACTION_RELEASE)
				isDisposed = isDisposed | onOutsideRelease(e);
		}

		return isDisposed;
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

	public void addChild(GUIRenderable child) {
		this.children.add(child);
		child.setParent(this);
		child.setTreeTier(this.treeTier + 1);
	}

	public void removeChild(GUIRenderable child) {
		this.children.remove(child);
		child.setParent(null);
		child.setTreeTier(-1);
	}

	public void setZIndex(float index) {
		this.zIndex = index;
	}
	
	public boolean clipChildren(){
		return clipChildren;
	}

	public Unit2Property getRotationOffset() {
		return rotationOffset;
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

	@Override
	public Texture2D render(FrameBufferObject fbo) {

		Vec2f sizePix = size.pixels();
		
		fbo.setColorBufferTexture(DEFAULT_RENDER_LOCATION, (int) sizePix.x, (int) sizePix.y);
		
		Vec2f rotationOffsetPix = rotationOffset.pixels();
		float rotationDeg = rotation.degrees();
		Vec2f positionPix = position.pixels();
		Vec2f positionOffsetPix = positionOffset.pixels().negation();
		Vec4f borderWidthPix = borderWidth.pixels();
		Vec4f borderRadiusPix = borderRadius.pixels();

		Mat3f transformation = new Mat3f().scale(sizePix).translate(rotationOffsetPix.negation()).rotate(rotationDeg)
				.translate(rotationOffsetPix).translate(positionPix).translate(positionOffsetPix);
		Mat4f projection = fbo.getOrthographicMatrix(DEFAULT_RENDER_LOCATION);

		if (clipChildren) {
			tsa.bind();

			renderBackground(transformation, projection, sizePix, tsa.get().getTexture(BACKGROUND_TEXTURE_INDEX),
					borderWidthPix, borderRadiusPix);
			
			renderChildren(fbo, transformation, projection);
			
			renderForeground(transformation, projection, sizePix, tsa.get().getTexture(FOREGROUND_TEXTURE_INDEX),
					borderWidthPix, borderRadiusPix);

			tsa.unbind();

			return fbo.getColorBufferTexture(DEFAULT_RENDER_LOCATION);
		} else {
			
			tsa.bind();

			renderBackground(transformation, projection, sizePix, tsa.get().getTexture(BACKGROUND_TEXTURE_INDEX),
					borderWidthPix, borderRadiusPix);
			renderForeground(transformation, projection, sizePix, tsa.get().getTexture(FOREGROUND_TEXTURE_INDEX),
					borderWidthPix, borderRadiusPix);

			tsa.unbind();

			return fbo.getColorBufferTexture(DEFAULT_RENDER_LOCATION);
		}
	}
	
	private void renderChildren(FrameBufferObject fbo, Mat3f transformation, Mat4f projection){
		tsa.unbind();
		fbo.unbind();
		
		Texture2D clippedResult = GUIRenderingOrder.renderChildComponents(this);
		
		fbo.bind();
		tsa.bind();
		
		contentProgram.start();
		
		contentProgram.setUniformMat3f("transformation", transformation);
		contentProgram.setUniformMat4f("projection", projection);
		contentProgram.setUniformTexture("texture", clippedResult);
		
		mesh.render();
		
		contentProgram.stop();
	}

	public void render() {
		Vec2f sizePix = size.pixels();
		Vec2f rotationOffsetPix = rotationOffset.pixels();
		float rotationDeg = rotation.degrees();
		Vec2f positionPix = position.pixels();
		Vec2f positionOffsetPix = positionOffset.pixels().negation();
		Vec4f borderWidthPix = borderWidth.pixels();
		Vec4f borderRadiusPix = borderRadius.pixels();

		Mat3f transformation = new Mat3f().scale(sizePix).translate(rotationOffsetPix.negation()).rotate(rotationDeg)
				.translate(rotationOffsetPix).translate(positionPix).translate(positionOffsetPix);
		Mat4f projection = gui.getWindow().getOrthoProjection();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tsa.bind();

		renderBackground(transformation, projection, sizePix, tsa.get().getTexture(BACKGROUND_TEXTURE_INDEX),
				borderWidthPix, borderRadiusPix);
		// renderChildren
		renderForeground(transformation, projection, sizePix, tsa.get().getTexture(FOREGROUND_TEXTURE_INDEX),
				borderWidthPix, borderRadiusPix);

		tsa.unbind();
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderBackground(Mat3f transformation, Mat4f projection, Vec2f size, Texture backgroundTexture,
			Vec4f borderWidth, Vec4f borderRadius) {

		backgroundProgram.start();

		backgroundProgram.setUniformMat3f("transformation", transformation);
		backgroundProgram.setUniformMat4f("projection", projection);
		backgroundProgram.setUniformFloat("width", size.x);
		backgroundProgram.setUniformFloat("height", size.y);
		backgroundProgram.setUniformColor("backgroundColor", backgroundColor);
		backgroundProgram.setUniformVec4f("borderWidth", borderWidth);
		backgroundProgram.setUniformVec4f("borderRadius", borderRadius);
		backgroundProgram.setUniformTexture("backgroundTexture", backgroundTexture);

		mesh.render();

		backgroundProgram.stop();
	}

	private void renderForeground(Mat3f transformation, Mat4f projection, Vec2f size, Texture foregroundTexture,
			Vec4f borderWidth, Vec4f borderRadius) {

		foregroundProgram.start();

		foregroundProgram.setUniformMat3f("transformation", transformation);
		foregroundProgram.setUniformMat4f("projection", projection);
		foregroundProgram.setUniformFloat("width", size.x);
		foregroundProgram.setUniformFloat("height", size.y);
		foregroundProgram.setUniformColor("borderColor", borderColor);
		foregroundProgram.setUniformVec4f("borderWidth", borderWidth);
		foregroundProgram.setUniformVec4f("borderRadius", borderRadius);
		foregroundProgram.setUniformTexture("foregroundTexture", foregroundTexture);

		mesh.render();

		foregroundProgram.stop();
	}
}
