package pe.engine.graphics.objects;

public class TextGlyph {

	private int pixelOffset;
	private int width;

	public TextGlyph(int pixelOffset, int width) {
		this.pixelOffset = pixelOffset;
		this.width = width;
	}

	public int getPixelOffset() {
		return pixelOffset;
	}

	public int getWidth() {
		return width;
	}
}
