package pe.engine.graphics.gui;

import pe.engine.graphics.objects.Mesh;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.shapes.Polygon;

public abstract class GUIComponent {

	protected int width = 0;
	protected int height = 0;
	protected Vec2f position = Vec2f.ZERO;
	protected Vec2f center = Vec2f.ZERO;
	protected float rotation;
	protected Color backgroundColor = Color.CLEAR;
	protected int borderRadius = 0;
	protected int borderWidth = 0;
	protected Color borderColor = Color.CLEAR;
	protected String text = "";
	protected Color textColor = Color.CLEAR;
	protected Polygon shape = null;
	protected Mesh mesh = null;
	protected GUI gui = null;

	protected ShaderProgram program;

	protected static ShaderProgram shaderProgram = null;

	protected void initShaderProgram() {
		if (shaderProgram != null)
			return;

		Shader vertexShader = new Shader(PE.SHADER_TYPE_VERTEX, "src/pe/engine/shader/shaders/core/GUIComponent.vertx");
		vertexShader.compile();
		vertexShader.compileStatus();

		Shader fragmentShader = new Shader(PE.SHADER_TYPE_FRAGMENT,
				"src/pe/engine/shader/shaders/core/GUIComponent.frag");
		fragmentShader.compile();
		fragmentShader.compileStatus();

		ShaderProgram program = new ShaderProgram();
		program.addShader(vertexShader);
		program.addShader(fragmentShader);

		program.setDefaultFragOutValue("fragColor", 0);
		
		program.setAttribIndex(0, "position");
		program.setAttribIndex(1, "barycentric");

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

	public void render() {
		shaderProgram.use();
		
		float windowWidth = gui.getWindow().getWidth();
		float windowHeight = gui.getWindow().getHeight();
		Vec2f scale = new Vec2f(width/shape.getWidth(), height/shape.getHeight());
		Mat3f transformation = new Mat3f().scale(scale).translate(center.mul(-1)).rotate(rotation).translate(center.mul(-1)).translate(position);
		
		Mat4f projection = Mat4f.getOrthographicMatrix(0, windowWidth, windowHeight, 0, -1, 1);
		
		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformInt("borderRadius", borderRadius);
		shaderProgram.setUniformInt("borderWidth", borderWidth);
		shaderProgram.setUniformColor("borderColor", borderColor);

		mesh.render();

		shaderProgram.stop();
	}
}
