package pe.engine.shader.shaders.core;

import pe.engine.main.PE;
import pe.engine.shader.main.Shader;
import pe.engine.shader.main.ShaderProgram;

public class CoreShaders {

	public static final String PATH = "src/pe/engine/shader/shaders/core/";
	
	public static final String SHADER_VERTEX_EXT = ".vertx";
	public static final String SHADER_GEOMENTRY_EXT = ".geom";
	public static final String SHADER_FRAGMENT_EXT = ".frag";
	
	private static final String PATH_BASIC_FILE_BASE = "Basic_";
	public static final String PATH_BASIC_VERTEX_MP = PATH + PATH_BASIC_FILE_BASE + "Vertex_MP" + SHADER_VERTEX_EXT;
	
	private static final String PATH_TEXT_GLYPH_FILE_BASE = "TextGlyph";
	public static final String PATH_TEXT_GLYPH_VERTEX = PATH_BASIC_VERTEX_MP;//PATH + PATH_TEXT_GLYPH_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String PATH_TEXT_GLYPH_FRAGMENT = PATH + PATH_TEXT_GLYPH_FILE_BASE + SHADER_FRAGMENT_EXT;
	
	private static final String PATH_GUI_COMPONENT_FILE_BASE = "GUIComponent";
	public static final String PATH_GUI_COMPONENT_VERTEX = PATH + PATH_GUI_COMPONENT_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String PATH_GUI_COMPONENT_FRAGMENT = PATH + PATH_GUI_COMPONENT_FILE_BASE + SHADER_FRAGMENT_EXT;

	private static final String PATH_GUI_BACKGROUND_FILE_BASE = "GUI_Background";
	public static final String PATH_GUI_BACKGROUND_VERTEX = PATH_BASIC_VERTEX_MP;//PATH + PATH_GUI_BACKGROUND_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String PATH_GUI_BACKGROUND_FRAGMENT = PATH + PATH_GUI_BACKGROUND_FILE_BASE + SHADER_FRAGMENT_EXT;

	private static final String PATH_GUI_FOREGROUND_FILE_BASE = "GUI_Foreground";
	public static final String PATH_GUI_FOREGROUND_VERTEX = PATH_BASIC_VERTEX_MP;//PATH + PATH_GUI_FOREGROUND_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String PATH_GUI_FOREGROUND_FRAGMENT = PATH + PATH_GUI_FOREGROUND_FILE_BASE + SHADER_FRAGMENT_EXT;

	private static final String PATH_GUI_CONTENT_FILE_BASE = "GUI_Content";
	public static final String PATH_GUI_CONTENT_VERTEX = PATH_BASIC_VERTEX_MP;
	public static final String PATH_GUI_CONTENT_FRAGMENT = PATH + PATH_GUI_CONTENT_FILE_BASE + SHADER_FRAGMENT_EXT;
	
	private static Shader basicVertexMP;
	
	private static Shader guiBackgroundVertex;
	private static Shader guiBackgroundFragment;
	private static ShaderProgram guiBackgroundProgram;
	
	private static Shader guiForegroundVertex;
	private static Shader guiForegroundFragment;
	
	private static Shader guiContentVertex;
	private static Shader guiContentFragment;
	
	public static Shader guiBackgroundVertex(){
		if(guiBackgroundVertex == null){
			guiBackgroundVertex = new Shader(PE.SHADER_TYPE_VERTEX, PATH_GUI_BACKGROUND_VERTEX);
			guiBackgroundVertex.compile();
			guiBackgroundVertex.compileStatus();
		}
		
		return guiBackgroundVertex;
	}
	
	public static Shader guiBackgroundFragment(){
		if(guiBackgroundFragment == null){
			guiBackgroundFragment = new Shader(PE.SHADER_TYPE_FRAGMENT, PATH_GUI_BACKGROUND_FRAGMENT);
			guiBackgroundFragment.compile();
			guiBackgroundFragment.compileStatus();
		}
		
		return guiBackgroundFragment;
	}
	
	public static ShaderProgram guiBackgroundProgram(){
		if(guiBackgroundProgram == null){
			guiBackgroundProgram = new ShaderProgram();
			guiBackgroundProgram.addShader(guiBackgroundVertex());
			guiBackgroundProgram.addShader(guiBackgroundFragment());
			guiBackgroundProgram.setDefaultFragOutValue("color", 0);
			guiBackgroundProgram.setAttribIndex(0, "position");
			guiBackgroundProgram.setAttribIndex(0, "textureCoord");
			guiBackgroundProgram.compile();
			guiBackgroundProgram.compileStatus();
		}
		
		return guiBackgroundProgram;
	}
}
