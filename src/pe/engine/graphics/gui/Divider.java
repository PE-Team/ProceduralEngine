package pe.engine.graphics.gui;

import pe.engine.graphics.gui.properties.RotationProperty;
import pe.engine.graphics.objects.StaticMesh2D;
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
			float rotation, int rotationUnits, Color backgroundColor, Vec4f borderWidth, int[] borderWidthUnits,
			Color borderColor, Vec4f borderRadius, int[] borderRadiusUnits, String text, Color textColor,
			Polygon shape) {

		super(size, sizeUnits, position, positionUnits, center, centerUnits, rotation, backgroundColor,
				borderWidth, borderWidthUnits, borderColor, borderRadius, borderRadiusUnits, text, textColor, shape);
	}

	public Divider(float width, float height, float posX, float posY, Color backgroundColor, Color borderColor) {
		this.size = new Vec2f(width, height);
		this.size = new Vec2f(0.5f, 0.25f);
		this.sizeUnits = new int[]{PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT};
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderWidth = new Vec4f(10f, 75f, 50f, 25f);
		this.borderRadius = new Vec4f(100f, 20f, 40f, 80f);
		this.position = new Vec2f(posX, posY);
		this.rotation = new RotationProperty();

		this.shape = new Rectangle(1, 1);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());

		initMeshProperties();

		initShaderProgram();
	}
}