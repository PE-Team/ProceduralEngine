package pe.engine.graphics.objects;

import java.util.HashMap;
import java.util.Map;

public class FontAtlas {

	private String textFamily;
	private boolean bold;
	private boolean italics;
	private boolean anitAilize;

	private Texture2D textGlyphTextures;
	private Map<Character, TextGlyph> textGlyphs = new HashMap<Character, TextGlyph>();

	public FontAtlas(String textFamily, boolean bold, boolean italics, boolean antiAilize) {
		this.textFamily = textFamily;
		this.bold = bold;
		this.italics = italics;
		this.anitAilize = antiAilize;
	}
	
	public void bind(){
		textGlyphTextures.bind();
	}
	
	public void unbind(){
		textGlyphTextures.unbind();
	}

	public void addTextGlyph(char c, TextGlyph glyph) {
		textGlyphs.remove(c);
		textGlyphs.put(c, glyph);
	}
	
	public void setTextGlyphTexture(Texture2D glyphTexture){
		this.textGlyphTextures = glyphTexture;
	}
	
	public TextGlyph getGlyph(char c){
		return textGlyphs.get(c);
	}
	
	public Texture2D getFontTexture(){
		return textGlyphTextures;
	}
	
	public int getHeight(){
		return textGlyphTextures.getHeight();
	}
	
	public int getWidth(){
		return textGlyphTextures.getWidth();
	}

	public boolean equals(Object obj) {
		FontAtlas fa = (FontAtlas) obj;
		return fa.bold == this.bold && fa.italics == this.italics && fa.anitAilize == this.anitAilize
				&& fa.textFamily.equals(this.textFamily);
	}
}
