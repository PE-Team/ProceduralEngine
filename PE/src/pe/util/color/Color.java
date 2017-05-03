package pe.util.color;

import java.nio.FloatBuffer;

import pe.engine.main.PE;
import pe.util.math.Maths;
import pe.util.math.Vec3f;
import pe.util.math.Vec4f;

public class Color {
	public float r, g, b, a;
	private int colorType;

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color DARK_GREY = new Color(64, 64, 64);
	public static final Color GREY = new Color(128, 128, 128);
	public static final Color LIGHT_GREY = new Color(192, 192, 192);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color ORANGE = new Color(255, 165, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color GREEN = new Color(0, 255, 0);

	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
		this.colorType = PE.COLOR_TYPE_RGB;
	}

	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
		this.colorType = PE.COLOR_TYPE_RGB;
	}

	public Color(Vec3f vec) {
		this.r = vec.x;
		this.g = vec.y;
		this.b = vec.z;
		this.a = 1;
		this.colorType = PE.COLOR_TYPE_RGB;
	}

	public Color(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.colorType = PE.COLOR_TYPE_RGBA;
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.colorType = PE.COLOR_TYPE_RGBA;
	}

	public Color(Vec3f vec, float a) {
		this.r = vec.x;
		this.g = vec.y;
		this.b = vec.z;
		this.a = a;
		this.colorType = PE.COLOR_TYPE_RGBA;
	}

	public Color(Vec4f vec) {
		this.r = vec.x;
		this.g = vec.y;
		this.b = vec.z;
		this.a = vec.w;
		this.colorType = PE.COLOR_TYPE_RGBA;
	}

	public void setColorType(int colorType) {
		if (!isValidColorType(colorType))
			throw new IllegalArgumentException("An unknown 'colorType' vaule of: '" + colorType + "' was given.");
		this.colorType = colorType;
	}

	public int colorType() {
		return colorType;
	}

	public int colorChannels() {
		switch (colorType) {
		case PE.COLOR_TYPE_RGB:
			return 3;
		case PE.COLOR_TYPE_RGBA:
			return 4;
		}
		throw new IllegalStateException("This color has a unknown 'colorType' value of: " + colorType);
	}

	public static int colorChannels(int colorType) {
		switch (colorType) {
		case PE.COLOR_TYPE_RGB:
			return 3;
		case PE.COLOR_TYPE_RGBA:
			return 4;
		}
		throw new IllegalArgumentException("This color has a unknown 'colorType' value of: " + colorType);
	}

	public static boolean isValidColorType(int colorType) {
		switch (colorType) {
		case PE.COLOR_TYPE_RGB:
			return true;
		case PE.COLOR_TYPE_RGBA:
			return true;
		}
		return false;
	}

	public String toHex() {
		return Maths.intToHex(this.toInt());
	}
	
	public void putIntBuffer(FloatBuffer buffer){
		switch (colorType) {
		case PE.COLOR_TYPE_RGB:
			putInBufferRGB(buffer);
		case PE.COLOR_TYPE_RGBA:
			putInBufferRGBA(buffer);
		}
		throw new IllegalArgumentException("This color has a unknown 'colorType' value of: " + colorType);
	}

	public void putInBufferRGB(FloatBuffer buffer) {
		buffer.put(r).put(g).put(b);
	}

	public void putInBufferRGBA(FloatBuffer buffer) {
		buffer.put(r).put(g).put(b).put(a);
	}

	public int toInt() {
		return ((int) r) << 24 + ((int) g) << 16 + ((int) b) << 8 + ((int) a);
	}

	public Color toDecimal() {
		this.r /= 255;
		this.g /= 255;
		this.b /= 255;
		return this;
	}

	public Color toCMY() {
		this.r = 255 - r;
		this.g = 255 - g;
		this.b = 255 - b;
		return this;
	}

	public java.awt.Color getJColor() {
		this.toDecimal();
		return new java.awt.Color(this.r, this.g, this.b, this.a);
	}
}
