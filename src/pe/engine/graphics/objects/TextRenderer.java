package pe.engine.graphics.objects;

import pe.engine.graphics.main.Window;
import pe.util.color.Color;
import pe.util.math.Vec2f;

public class TextRenderer{
	
	private FontAtlas currentFont = Fonts.getFont();
	private GlyphModel glyphModel = new GlyphModel();
	
	public TextRenderer(){
			
	}
	
	public void setCurrentFont(String textFamily, boolean bold, boolean italics, boolean antiAilize){
		this.currentFont = Fonts.getFont(textFamily, bold, italics, antiAilize);
	}
	
	public void render(char c, Window window, Vec2f position, Vec2f size, Color color){
		glyphModel.setWindow(window);
		glyphModel.setPosition(position);
		glyphModel.setSize(size);
		glyphModel.setFontAtlas(currentFont);
		glyphModel.setColor(color);
		glyphModel.setChar(c);
		glyphModel.render();
	}

	public void render(String sentence, Window window, Vec2f position, Vec2f size, Color color){
		Vec2f tempPos = new Vec2f(position);
		float totalSize = 0;
		for(char c:sentence.toCharArray()){
			totalSize += currentFont.getGlyph(c).getWidth();
		}
		
		float ratio = size.x / totalSize;
		
		for(char c:sentence.toCharArray()){
			TextGlyph glyph = currentFont.getGlyph(c);
			float glyphWidth = glyph.getWidth() * ratio;
			render(c, window, tempPos, new Vec2f(glyphWidth, size.y), color);
			tempPos.x += glyphWidth;
		}
	}
}
