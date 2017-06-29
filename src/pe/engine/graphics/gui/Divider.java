package pe.engine.graphics.gui;

import pe.engine.data.TextureArrayObject;
import pe.engine.graphics.gui.textures.core.CoreTexturePaths;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.graphics.objects.Texture;
import pe.engine.graphics.objects.Texture2D;
import pe.engine.main.PE;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Polygon;
import pe.util.shapes.Rectangle;

public class Divider extends GUIComponent {

	public static final String NAME = "div";

	public Divider() {

	}

	public Divider(Vec2f size, int[] sizeUnits, Vec2f position, int[] positionUnits, Vec2f center, int[] centerUnits,
			float rotation, Color backgroundColor, Vec4f borderWidth, int[] borderWidthUnits,
			Color borderColor, Vec4f borderRadius, int[] borderRadiusUnits, String text, Color textColor,
			Polygon shape) {

		super(size, sizeUnits, position, positionUnits, center, centerUnits, rotation, backgroundColor,
				borderWidth, borderWidthUnits, borderColor, borderRadius, borderRadiusUnits, text, textColor, shape);
	}

	public Divider(float width, float height, float posX, float posY, Color backgroundColor, Color borderColor) {
		// @formatter:off
		super(
				new Vec2f(0.5f, 0.25f),
				new int[]{PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT},
				new Vec2f(posX, posY),
				new int[]{PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS},
				new Vec2f(0.5f, 0.5f),
				new int[]{PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT},
				0f,
				backgroundColor,
				new Vec4f(10f, 75f, 50f, 25f),
				new int[]{PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS},
				borderColor,
				new Vec4f(100f, 20f, 40f, 80f),
				new int[]{PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS},
				"NULL",
				Color.GREEN,
				new Rectangle(1, 1)
				);
		// @formatter:on
		
		Texture backgroundTexture = new Texture2D(CoreTexturePaths.BACKGROUND_TEST);
		Texture foregroundTexture = new Texture2D(CoreTexturePaths.FOREGROUND_TEST);
		
		TextureArrayObject tao = tsa.get(PE.GUI_EVENT_DEFAULT);
		tao.set(backgroundTexture, BACKGROUND_TEXTURE_INDEX);
		tao.set(foregroundTexture, FOREGROUND_TEXTURE_INDEX);
		
		tao.load();
	}
	
	protected boolean onPress(WindowInputEvent e) {
		System.out.println("PRESSING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_ON_PRESS);
		return true;
	}

	protected boolean onRelease(WindowInputEvent e) {
		System.out.println("RELEASING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_DEFAULT);
		return true;
	}

	protected boolean onClick(WindowInputEvent e) {
		System.out.println("CLICKING IS WORKING!");
		return true;
	}

	protected boolean onScroll(WindowInputEvent e) {
		System.out.println("SCROLLING IS WORKING!");
		return true;
	}

	protected boolean onDrag(WindowInputEvent e) {
		System.out.println("DRAGGING IS WORKING!");
		return false;
	}

	protected boolean onHover(WindowInputEvent e) {
		System.out.println("HOVERING IS WORKING!");
		return false;
	}

	protected boolean onType(WindowInputEvent e) {
		System.out.println("TYPING WOKRING!");
		return false;
	}

	protected boolean onOutsideRelease(WindowInputEvent e) {
		System.out.println("RELEASING IS WORKING!");
		tsa.swap(PE.GUI_EVENT_DEFAULT);
		return false;
	}
}