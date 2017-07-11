package pe.engine.graphics.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.system.MemoryUtil;

import pe.engine.graphics.gui.textures.core.CoreTextures;

public class Fonts {

	public static final int BASE_HEIGHT_DEFAULT = 32;
	public static final int BASE_HEIGHT_ANY = -1;

	public static final String FAMILY_MONOSPACED = Font.MONOSPACED;

	private static Map<FontAtlas, FontAtlas> fonts = new HashMap<FontAtlas, FontAtlas>();

	public static synchronized FontAtlas loadFont() {
		return loadFont(BASE_HEIGHT_ANY);
	}

	public static synchronized FontAtlas loadFont(int baseHeight) {
		return loadFont(FAMILY_MONOSPACED, false, false, baseHeight);
	}

	public static synchronized FontAtlas loadFont(boolean bold, boolean italics) {
		return loadFont(FAMILY_MONOSPACED, bold, italics);
	}
	
	public static synchronized FontAtlas loadFont(String textFamily) {
		return loadFont(textFamily, BASE_HEIGHT_ANY);
	}

	public static synchronized FontAtlas loadFont(String textFamily, int baseHeight) {
		return loadFont(textFamily, false, false, baseHeight);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics) {
		return loadFont(textFamily, bold, italics, BASE_HEIGHT_ANY);
	}
	
	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, boolean antiAilize) {
		return loadFont(textFamily, bold, italics, antiAilize, BASE_HEIGHT_ANY);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, int baseHeight) {
		return loadFont(textFamily, bold, italics, true, baseHeight);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, boolean antiAilize, int baseHeight) {
		
		/* Check to see if a satisfactory font atlas already exits */
		FontAtlas atlas = new FontAtlas(textFamily, bold, italics, antiAilize);
		
		String namePattern = "font_" + textFamily + "_(\\d+).*";
		
		if(baseHeight == BASE_HEIGHT_ANY)
			baseHeight = BASE_HEIGHT_DEFAULT;
		
		Texture2D fontTexture = null;
		
		/* Check to see if a satisfactory texture exits in CustomTextures */
		//TODO
		
		/* Check to see if a satisfactory texture exists in CoreTextures */
		fontTexture = CoreTextures.getTexture(CoreTextures.PATH_DEFAULT, namePattern, baseHeight);
		if(fontTexture != null){
			fontTexture.bind();
			fontTexture.load();
			fontTexture.unbind();
			atlas.setTextGlyphTexture(fontTexture);
			fonts.put(atlas, atlas);
		}
		
		/* Create a new texture and save it in CoreTextures */
		
		int style = (bold ? Font.BOLD : 0) + (italics ? Font.ITALIC : 0);
		Font font = new Font(textFamily, style, baseHeight);

		int imageWidth = 0;
		int imageHeight = 0;

		for (int i = 32; i < 256; i++) {
			if (i == 127)
				continue;
			
			char c = (char) i;
			BufferedImage ch = generateCharImage(font, c, antiAilize);

			imageWidth += ch.getWidth();
			imageHeight = Math.max(imageHeight, ch.getHeight());
		}

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		int glyphOffset = 0;

		for (int i = 32; i < 256; i++) {
			if (i == 127)
				continue;

			char c = (char) i;
			BufferedImage ch = generateCharImage(font, c, antiAilize);

			int charWidth = ch.getWidth();
			
			if(charWidth == 0)
				continue;

			TextGlyph glyph = new TextGlyph(glyphOffset, charWidth);
			g.drawImage(ch, glyphOffset, 0, null);

			glyphOffset += ch.getWidth();
			atlas.addTextGlyph(c, glyph);
		}
		
		g.dispose();
		
		String fileName = "font_" + textFamily + "_" + baseHeight;
		CoreTextures.saveTexture(image, CoreTextures.PATH_DEFAULT, fileName);
		
		fontTexture = CoreTextures.getTexture(CoreTextures.PATH_DEFAULT, namePattern, baseHeight);
		if(fontTexture == null)
			throw new IllegalArgumentException("Texture could not be loaded from core texture directory after being written to it.");
		
		fontTexture.bind();
		fontTexture.load();
		fontTexture.unbind();
		atlas.setTextGlyphTexture(fontTexture);
		fonts.put(atlas, atlas);
		
		return atlas;
	}

	private static BufferedImage generateCharImage(Font font, char c, boolean antiAilize) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		if (antiAilize)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.dispose();

		image = new BufferedImage(fontMetrics.charWidth(c), fontMetrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		if (antiAilize)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		g.setPaint(Color.WHITE);
		g.drawString(String.valueOf(c), 0, fontMetrics.getAscent());
		g.dispose();

		return image;
	}

	public static int getCharIndex(char c) {
		int ch = (int) c;
		if (ch > 127)
			ch--;

		return ch - 32;
	}

	public synchronized static FontAtlas getFont() {
		return getFont(FAMILY_MONOSPACED, false, false);
	}

	public synchronized static FontAtlas getFont(boolean bold, boolean italics) {
		return getFont(FAMILY_MONOSPACED, bold, italics);
	}

	public synchronized static FontAtlas getFont(String textFamily) {
		return getFont(textFamily, false, false);
	}

	public synchronized static FontAtlas getFont(String textFamily, boolean bold, boolean italics) {
		return getFont(textFamily, bold, italics, true);
	}

	public synchronized static FontAtlas getFont(String textFamily, boolean bold, boolean italics, boolean antiAilize) {
		FontAtlas atlas = new FontAtlas(textFamily, bold, italics, antiAilize);
		atlas = fonts.get(atlas);
		if(atlas == null)
			return loadFont(textFamily, bold, italics, antiAilize);
		return atlas;
	}
}
