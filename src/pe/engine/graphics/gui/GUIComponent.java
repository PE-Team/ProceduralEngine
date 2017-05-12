package pe.engine.graphics.gui;

import pe.engine.graphics.objects.Mesh;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.util.color.Color;

public abstract class GUIComponent{

	protected int width = 0;
	protected int height = 0;
	protected Color backgroundColor = Color.CLEAR;
	protected Color foregroundColor = Color.CLEAR;
	protected int borderRadius = 0;
	protected int borderWidth = 0;
	protected Color borderColor = Color.CLEAR;
	protected String text = "";
	protected Mesh mesh = null;
	protected GUI gui;
	
	protected ShaderProgram program;
	
	protected static ShaderProgram shaderProgram = null;
	
	protected void initShaderProgram(){
		if(shaderProgram != null) return;
		
		Shader vertexShader = new Shader(PE.SHADER_TYPE_VERTEX, "src/pe/engine/shader/shaders/core/GUIComponent.vertx");
		vertexShader.compile();
		vertexShader.compileStatus();
		
		Shader fragmentShader = new Shader(PE.SHADER_TYPE_FRAGMENT, "src/pe/engine/shader/shaders/core/GUIComponent.frag");
		fragmentShader.compile();
		fragmentShader.compileStatus();
		
		ShaderProgram program = new ShaderProgram();
		program.addShader(vertexShader);
		program.addShader(fragmentShader);
		
		program.setDefaultFragOutValue("fragColor", 0);
		
		program.compile();
		program.compileStatus();
		
		program.use();
		
		program.setAttribVec2f("position", false, 0, 2);
		
		program.stop();
		
		shaderProgram = program;
	}
	
	public GUIComponent(){};

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
		
		shaderProgram.setUniformColor("backgroundColor", backgroundColor);
		shaderProgram.setUniformColor("foregroundColor", foregroundColor);
		shaderProgram.setUniformInt("borderRadius", borderRadius);
		shaderProgram.setUniformInt("borderWidth", borderWidth);
		shaderProgram.setUniformColor("borderColor", borderColor);
		
		mesh.render();
		
		shaderProgram.stop();
	}
}
