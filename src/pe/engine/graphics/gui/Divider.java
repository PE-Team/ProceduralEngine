package pe.engine.graphics.gui;

import pe.engine.graphics.objects.StaticMesh2D;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.shapes.Rectangle;

public class Divider extends GUIComponent{

	public static final String NAME = "div";
	
	public Divider(int width, int height, Color color){
		this.width = width;
		this.height = height;
		this.backgroundColor = color;
		
		this.shape = new Rectangle(width, height);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		
		initShaderProgram();
	}
}
