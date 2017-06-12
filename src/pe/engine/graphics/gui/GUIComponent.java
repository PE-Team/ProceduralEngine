package pe.engine.graphics.gui;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import pe.engine.graphics.objects.Mesh;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Polygon;

public abstract class GUIComponent {

	protected float width = 0;
	protected float height = 0;
	protected Vec2f position = Vec2f.ZERO;
	protected Vec2f center = Vec2f.ZERO;
	protected float rotation;
	protected Color backgroundColor = Color.CLEAR;
	protected Vec4f borderWidth = Vec4f.ZERO;			// {TOP, RIGHT, BOTTOM, LEFT}
	protected Color borderColor = Color.CLEAR;
	protected Vec4f borderRadius = Vec4f.ZERO;			// {TOP-LEFT, TOP-RIGHT, BOTTOM-RIGHT, BOTTOM-LEFT}
	protected String text = "";
	protected Color textColor = Color.CLEAR;
	protected Polygon shape = null;
	protected Mesh mesh = null;
	protected GUI gui = null;
	
	protected int widthUnit = PE.GUI_UNIT_RPIXELS;
	protected int heightUnit = PE.GUI_UNIT_RPIXELS;
	protected int[] positionUnit = {PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS};
	protected int[] centerUnit = {PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS};
	protected int rotationUnit = PE.ANGLE_UNIT_DEGREES;
	protected int[] borderWidthUnit = {PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS};
	protected int[] borderRadiusUnit = {PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS, PE.GUI_UNIT_RPIXELS};

	protected ShaderProgram program;

	protected static ShaderProgram shaderProgram = null;
	
	protected void initMeshProperties()	{
		Vec2f v0Border = new Vec2f(1, 1);
		Vec2f v1Border = new Vec2f(0, 1);
		Vec2f v2Border = new Vec2f(0, 0);
		Vec2f v3Border = new Vec2f(1, 0);
		
		FloatBuffer borderVectors = BufferUtils.createFloatBuffer(8); // dim * # of vectors = 2 * 4
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
	
	public void setRotation(float degrees){
		this.rotation = degrees;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public void rotate(float degrees){
		this.rotation += degrees;
	}

	public void render() {
		shaderProgram.use();
		float rpixRatio = gui.getWindow().getRPixRatio();
		float windowWidth = gui.getWindow().getWidth();
		float windowHeight = gui.getWindow().getHeight();
		float ratio = windowWidth / windowHeight;
		float pixWidth = 2f/windowWidth;
		
		float rotationDeg = PE.toDegrees(rotation, rotationUnit);
		float widthPix = PE.toPixels(width, widthUnit, windowWidth, rpixRatio);
		float heightPix = PE.toPixels(height, heightUnit, windowHeight, rpixRatio);
		Vec4f borderWidthMax = new Vec4f(heightPix, widthPix, heightPix, widthPix);
		Vec4f borderWidthPix = PE.toPixels(borderWidth, borderWidthUnit, borderWidthMax, rpixRatio);
		float borderRadiusMax = heightPix > widthPix ? widthPix : heightPix;
		Vec4f borderRadiusPix = PE.toPixels(borderRadius, borderRadiusUnit, borderRadiusMax, rpixRatio);
		
		Vec2f scale = new Vec2f(widthPix/shape.getWidth(), heightPix/shape.getHeight());
		Mat3f transformation = new Mat3f().scale(scale).translate(center.mul(-1)).rotate(rotationDeg).translate(center.mul(-1)).translate(position);
		Mat4f projection = gui.getWindow().getOrthoProjection();
		
		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformFloat("screenRatio", ratio);
		shaderProgram.setUniformFloat("pixWidth", pixWidth);
		shaderProgram.setUniformFloat("width", widthPix);
		shaderProgram.setUniformFloat("height", heightPix);
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformVec4f("borderWidth", borderWidthPix);
		shaderProgram.setUniformColor("borderColor", borderColor);
		shaderProgram.setUniformVec4f("borderRadius", borderRadiusPix);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mesh.render();
		GL11.glDisable(GL11.GL_BLEND);

		shaderProgram.stop();
	}
}
