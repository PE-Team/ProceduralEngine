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

	protected int width = 0;
	protected int height = 0;
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
		program.setAttribIndex(1, "borderVec");

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
		float ratio = gui.getWindow().getWidth() * 1f / gui.getWindow().getHeight();
		float pixWidth = 2f/gui.getWindow().getWidth();
		Vec2f scale = new Vec2f(width/shape.getWidth(), height/shape.getHeight());
		Mat3f transformation = new Mat3f().scale(scale).translate(center.mul(-1)).rotate(rotation).translate(center.mul(-1)).translate(position);
		Mat4f projection = gui.getWindow().getOrthoProjection();
		
		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformFloat("screenRatio", ratio);
		shaderProgram.setUniformFloat("pixWidth", pixWidth);
		shaderProgram.setUniformInt("width", width);
		shaderProgram.setUniformInt("height", height);
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformVec4f("borderWidth", borderWidth);
		shaderProgram.setUniformColor("borderColor", borderColor);
		shaderProgram.setUniformVec4f("borderRadius", borderRadius);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mesh.render();

		shaderProgram.stop();
	}
}
