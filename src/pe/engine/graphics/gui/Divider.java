package pe.engine.graphics.gui;

import pe.engine.graphics.objects.StaticMesh2D;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;
import pe.util.shapes.Rectangle;

public class Divider extends GUIComponent {

	public static final String NAME = "div";

	public Divider(int width, int height, int posX, int posY, Color backgroundColor, Color borderColor) {
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderWidth = new Vec4f(100f, 75f, 50f, 25f);
		this.borderRadius = new Vec4f(100f, 100f, 100f, 100f);
		this.center = new Vec2f(width / 2f, height / 2f);
		this.position = new Vec2f(posX, posY);
		this.rotation = 0;

		this.shape = new Rectangle(1, 1);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		
		initMeshProperties();

		initShaderProgram();
	}
}