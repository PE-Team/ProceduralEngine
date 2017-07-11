package pe.engine.graphics.objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import pe.engine.graphics.main.Window;
import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;
import pe.engine.shader.shaders.core.CoreShaders;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.shapes.Rectangle;

public class GlyphModel implements RenderableI{
	
	protected static ShaderProgram shaderProgram;

	private Mesh mesh;
	private FontAtlas atlas;
	private Vec2f size;
	private Vec2f position;
	private Window window;
	private Color textColor;
	private TextGlyph textChar;
	
	public GlyphModel(){
		this((char) 0, null, null, 0, 0, 0, 0, Color.WHITE);
	}
	
	public GlyphModel(char baseChar, Window window, FontAtlas atlas, float width, float height, float posX, float posY, Color color){
		this.atlas = atlas;
		this.textChar = atlas == null ? null : atlas.getGlyph(baseChar);
		Rectangle rectangle = new Rectangle(1, 1);
		this.mesh = new StaticMesh2D(rectangle.getVertices(), rectangle.getIndices());
		this.size = new Vec2f(width, height);
		this.textColor = color;
		
		initMeshProperties();
		initShaderProgram();
	}
	
	protected void initMeshProperties() {
		Vec2f texCoordTL = new Vec2f(0, 1);
		Vec2f texCoordTR = new Vec2f(1, 1);
		Vec2f texCoordBR = new Vec2f(1, 0);
		Vec2f texCoordBL = new Vec2f(0, 0);

		FloatBuffer texCoordVectors = MemoryUtil.memAllocFloat(8);

		texCoordTL.putInBuffer(texCoordVectors);
		texCoordTR.putInBuffer(texCoordVectors);
		texCoordBR.putInBuffer(texCoordVectors);
		texCoordBL.putInBuffer(texCoordVectors);
		texCoordVectors.flip();
		
		mesh.addShaderAttrib(2, texCoordVectors);
		
		MemoryUtil.memFree(texCoordVectors);

		mesh.setWireframe(false);
	}
	
	protected void initShaderProgram() {
		if (shaderProgram != null)
			return;

		Shader vertexShader = new Shader(PE.SHADER_TYPE_VERTEX, CoreShaders.TEXT_GLYPH_VERTEX);
		vertexShader.compile();
		vertexShader.compileStatus();

		Shader fragmentShader = new Shader(PE.SHADER_TYPE_FRAGMENT, CoreShaders.TEXT_GLYPH_FRAGMENT);
		fragmentShader.compile();
		fragmentShader.compileStatus();

		ShaderProgram program = new ShaderProgram();
		program.addShader(vertexShader);
		program.addShader(fragmentShader);

		program.setDefaultFragOutValue("color", 0);

		program.setAttribIndex(0, "position");
		program.setAttribIndex(1, "textureCoord");

		program.compile();
		program.compileStatus();

		shaderProgram = program;
	}

	public void setFontAtlas(FontAtlas atlas){
		this.atlas = atlas;
	}
	
	public void setChar(char c){
		this.textChar = atlas.getGlyph(c);
	}

	public void setSize(Vec2f size) {
		this.size = size;
	}

	public void setPosition(Vec2f position) {
		this.position = position;
	}

	public void setWindow(Window window) {
		this.window = window;
	}
	
	public void setColor(Color color){
		this.textColor = color;
	}

	@Override
	public void render() {
		shaderProgram.start();
		atlas.bind();

		Mat3f transformation = new Mat3f().scale(size).translate(position);
		Mat4f projection = window.getOrthoProjection();

		shaderProgram.setUniformMat3f("transformation", transformation);
		shaderProgram.setUniformMat4f("projection", projection);
		shaderProgram.setUniformInt("charX", textChar.getPixelOffset());
		shaderProgram.setUniformInt("charWidth", textChar.getWidth());
		shaderProgram.setUniformInt("atlasWidth", atlas.getWidth());
		shaderProgram.setUniformColor("textColor", textColor);
		shaderProgram.setUniformTexture("texture", atlas.getFontTexture());
		
		System.out.println((textChar.getWidth() + " | " + atlas.getWidth()));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mesh.render();
		GL11.glDisable(GL11.GL_BLEND);

		atlas.unbind();
		shaderProgram.stop();
	}
	
	
}
