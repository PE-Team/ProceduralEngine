package pe.engine.graphics.gui;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import pe.engine.data.TextureArrayObject;
import pe.engine.data.TextureSwapArray;
import pe.engine.graphics.objects.Mesh;
import pe.engine.graphics.objects.StaticMesh2D;
import pe.engine.main.PE;
import pe.engine.main.UnitConversions;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Polygon;

public abstract class GUIComponent {

	protected static ShaderProgram shaderProgram = null;

	// @formatter:off
	protected Vec2f size = Vec2f.ZERO;
	protected Vec2f position = Vec2f.ZERO;
	protected Vec2f center = new Vec2f(0.5f, 0.5f);
	protected float rotation = 0;
	protected Color backgroundColor = Color.CLEAR;
	protected Vec4f borderWidth = Vec4f.ZERO;			// {TOP, RIGHT, BOTTOM, LEFT}
	protected Color borderColor = Color.CLEAR;
	protected Vec4f borderRadius = Vec4f.ZERO;			// {TOP-LEFT, TOP-RIGHT, BOTTOM-RIGHT, BOTTOM-LEFT}
	protected String text = "";
	protected Color textColor = Color.CLEAR;
	protected Polygon shape = null;
	protected Mesh mesh = null;
	protected TextureSwapArray tsa = null;
	protected GUI gui = null;
	protected GUIComponent parent = null;
	protected List<GUIComponent> children = new ArrayList<GUIComponent>();
	
	protected int[] sizeUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	protected int[] positionUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	protected int[] centerUnits = {PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT};
	protected int rotationUnits = PE.ANGLE_UNIT_DEGREES;
	protected int[] borderWidthUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	protected int[] borderRadiusUnits = {PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	// @formatter:on

	public GUIComponent(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, Vec2f center,
			int[] centerUnits, float rotation, int rotationUnits, Color backgroundColor, Vec4f borderWidth,
			int[] borderWidthUnits, Color borderColor, Vec4f borderRadius, int[] borderRadiusUnits, String text,
			Color textColor, Polygon shape) {
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());

		initMeshProperties();
		initShaderProgram();
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

		Shader vertexShader = new Shader(PE.SHADER_TYPE_VERTEX, "src/pe/engine/shader/shaders/core/GUIComponent.vertx");
		vertexShader.compile();
		vertexShader.compileStatus();

		Shader geometryShader = new Shader(PE.SHADER_TYPE_GEOMETRY,
				"src/pe/engine/shader/shaders/core/GUIComponent.geom");
		geometryShader.compile();
		geometryShader.compileStatus();

		Shader fragmentShader = new Shader(PE.SHADER_TYPE_FRAGMENT,
				"src/pe/engine/shader/shaders/core/GUIComponent.frag");
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

	public GUIComponent() {

	};

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	protected void onPress() {
	}

	protected void onRelease() {
	}

	protected void onClick() {
	}

	protected void onDrag() {
	}

	protected void onHover() {
	}

	protected void onType() {
	}

	public void setRotation(float degrees) {
		this.rotation = degrees;
	}

	public float getRotation() {
		return rotation;
	}

	public void rotate(float degrees) {
		this.rotation += degrees;
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

	public Vec2f getSizePix(float rpixRatio) {
		if (parent == null)
			return gui.getWindow().getSizePix();

		return UnitConversions.toPixels(size, sizeUnits, parent.getSizePix(rpixRatio), rpixRatio);
	}

	public Vec2f getCenterPix(float rpixRatio, Vec2f sizePix) {
		return UnitConversions.toPixels(center, centerUnits, sizePix, rpixRatio);
	}

	public float getRotationDeg() {
		return UnitConversions.toDegrees(rotation, rotationUnits);
	}

	public Vec2f getPositionPix(float rpixRatio, Vec2f maxPosition) {
		return UnitConversions.toPixels(position, positionUnits, maxPosition, rpixRatio);
	}

	public Vec4f getBorderWidthPix(float rpixRatio, Vec2f maxBorderWidth) {
		return UnitConversions.toPixels(borderWidth, borderWidthUnits, maxBorderWidth, rpixRatio);
	}

	public Vec4f getBorderRadiusPix(float rpixRatio, Vec2f maxBorderRadius) {
		return UnitConversions.toPixels(borderRadius, borderRadiusUnits, maxBorderRadius, rpixRatio);
	}

	public void render() {
		shaderProgram.start();
		float rpixRatio = gui.getWindow().getRPixRatio();
		float windowWidth = gui.getWindow().getWidth();
		float windowHeight = gui.getWindow().getHeight();
		float ratio = windowWidth / windowHeight;
		float pixWidth = 2f / windowWidth;

		Vec2f sizePix = getSizePix(rpixRatio);
		Vec2f centerPix = getCenterPix(rpixRatio, sizePix);
		float rotationDeg = getRotationDeg();
		Vec2f positionPix = getPositionPix(rpixRatio, parent.getSizePix(rpixRatio));
		Vec4f borderWidthPix = getBorderWidthPix(rpixRatio, Vec2f.mul(sizePix, 0.5f));
		Vec4f borderRadiusPix = getBorderRadiusPix(rpixRatio, sizePix);

		Vec2f scale = new Vec2f(sizePix.x / shape.getWidth(), sizePix.y / shape.getHeight());
		Mat3f transformation = new Mat3f().scale(scale).translate(centerPix.mul(-1)).rotate(rotationDeg)
				.translate(centerPix.mul(-1)).translate(positionPix);
		Mat4f projection = gui.getWindow().getOrthoProjection();

		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformFloat("screenRatio", ratio);
		shaderProgram.setUniformFloat("pixWidth", pixWidth);
		shaderProgram.setUniformFloat("width", sizePix.x);
		shaderProgram.setUniformFloat("height", sizePix.y);
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformVec4f("borderWidth", borderWidthPix);
		shaderProgram.setUniformColor("borderColor", borderColor);
		shaderProgram.setUniformVec4f("borderRadius", borderRadiusPix);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mesh.render();
		GL11.glDisable(GL11.GL_BLEND);

		shaderProgram.stop();

		renderChildren();
	}

	public void renderChildren() {
		for (GUIComponent child : children) {
			child.render();
		}
	}
}
