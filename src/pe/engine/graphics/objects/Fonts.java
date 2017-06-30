package pe.engine.graphics.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;

public class Fonts {

	public static final int DEFAULT_BASE_HEIGHT = 32;

	public static final String FAMILY_MONOSPACED = Font.MONOSPACED;

	private static Map<FontAtlas, FontAtlas> fonts = new HashMap<FontAtlas, FontAtlas>();

	public static synchronized FontAtlas loadFont() {
		return loadFont(DEFAULT_BASE_HEIGHT);
	}

	public static synchronized FontAtlas loadFont(int baseHeight) {
		return loadFont(FAMILY_MONOSPACED, false, false, baseHeight);
	}

	public static synchronized FontAtlas loadFont(boolean bold, boolean italics) {
		return loadFont(FAMILY_MONOSPACED, bold, italics);
	}

	public static synchronized FontAtlas loadFont(String textFamily, int baseHeight) {
		return loadFont(textFamily, false, false, baseHeight);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics) {
		return loadFont(textFamily, bold, italics, DEFAULT_BASE_HEIGHT);
	}
	
	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, boolean antiAilize) {
		return loadFont(textFamily, bold, italics, antiAilize, DEFAULT_BASE_HEIGHT);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, int baseHeight) {
		return loadFont(textFamily, bold, italics, true, baseHeight);
	}

	public static synchronized FontAtlas loadFont(String textFamily, boolean bold, boolean italics, boolean antiAilize, int baseHeight) {
		int style = (bold ? Font.BOLD : 0) + (italics ? Font.ITALIC : 0);
		Font font = new Font(textFamily, style, baseHeight);

		int imageWidth = 0;
		int imageHeight = 0;

		for (int i = 32; i < 256; i++) {
			if (i == 127) {
				continue;
			}
			char c = (char) i;
			BufferedImage ch = generateCharImage(font, c, antiAilize);
			if (ch == null)
				continue;

			imageWidth += ch.getWidth();
			imageHeight = Math.max(imageHeight, ch.getHeight());
		}

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		FontAtlas atlas = new FontAtlas(textFamily, antiAilize, antiAilize, antiAilize);

		int glyphOffset = 0;

		for (int i = 32; i < 256; i++) {
			if (i == 127)
				continue;

			char c = (char) i;
			BufferedImage ch = generateCharImage(font, c, antiAilize);
			if (ch == null)
				continue;

			int charWidth = ch.getWidth();

			TextGlyph glyph = new TextGlyph(glyphOffset, charWidth);
			atlas.addTextGlyph(c, glyph);

			glyphOffset += ch.getWidth();
		}

		int[] pixels = new int[imageWidth * imageHeight];
		image.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

		ByteBuffer buffer = MemoryUtil.memAlloc(imageWidth * imageHeight * 4);
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				int pixel = pixels[i * imageWidth + j];
				buffer.put((byte) ((pixel >> 16) & 0xFF)).put((byte) ((pixel >> 8) & 0xFF)).put((byte) (pixel & 0xFF))
						.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();

		Texture2D fontTexture = new BufferedTexture2D(buffer, imageWidth, imageHeight);
		atlas.setTextGlyphTexture(fontTexture);
		fonts.put(atlas, atlas);
		
		MemoryUtil.memFree(buffer);
		
		return atlas;
	}

	private static BufferedImage generateCharImage(Font font, char c, boolean antiAilize) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		if (antiAilize)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();

		image = new BufferedImage(fontMetrics.charWidth(c), fontMetrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		if (antiAilize)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(font);
		g.setPaint(Color.BLACK);
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

	private synchronized static FontAtlas getFont(String textFamily, boolean bold, boolean italics, boolean antiAilize) {
		FontAtlas atlas = new FontAtlas(textFamily, bold, italics, antiAilize);
		atlas = fonts.get(atlas);
		if(atlas == null)
			return loadFont(textFamily, bold, italics, antiAilize);
		return atlas;
	}
}
