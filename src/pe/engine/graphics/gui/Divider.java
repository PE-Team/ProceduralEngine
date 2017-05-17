package pe.engine.graphics.gui;

import pe.engine.graphics.objects.StaticMesh2D;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.shapes.Polygon;
import pe.util.shapes.Rectangle;

public class Divider extends GUIComponent{

	public static final String NAME = "div";
	
	public Divider(int width, int height, Color backgroundColor, Color borderColor){
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.center = new Vec2f(width / 2f, height / 2f);
		
		Vec2f[] polygon = { new Vec2f(1, 1), new Vec2f(0, 1), new Vec2f(0, 2), new Vec2f(1, 2), new Vec2f(1, 3),
				new Vec2f(2, 3), new Vec2f(2, 2), new Vec2f(3, 2), new Vec2f(3, 1), new Vec2f(2, 1), new Vec2f(2, 0),
				new Vec2f(1, 0) };
		
		this.shape = new Rectangle(1, 1);
//		this.shape = new Polygon(polygon);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		
		initShaderProgram();
	}
}
