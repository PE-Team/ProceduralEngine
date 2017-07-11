package pe.engine.shader.shaders.core;

public class CoreShaders {

	public static final String PATH = "src/pe/engine/shader/shaders/core/";
	
	private static final String SHADER_VERTEX_EXT = ".vertx";
	private static final String SHADER_GEOMENTRY_EXT = ".geom";
	private static final String SHADER_FRAGMENT_EXT = ".frag";
	
	private static final String TEXT_GLYPH_FILE_BASE = "TextGlyph";
	public static final String TEXT_GLYPH_VERTEX = PATH + TEXT_GLYPH_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String TEXT_GLYPH_FRAGMENT = PATH + TEXT_GLYPH_FILE_BASE + SHADER_FRAGMENT_EXT;
	
	private static final String GUI_COMPONENT_FILE_BASE = "GUIComponent";
	public static final String GUI_COMPONENT_VERTEX = PATH + GUI_COMPONENT_FILE_BASE + SHADER_VERTEX_EXT;
	public static final String GUI_COMPONENT_FRAGMENT = PATH + GUI_COMPONENT_FILE_BASE + SHADER_FRAGMENT_EXT;
}
