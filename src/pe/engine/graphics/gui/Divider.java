package pe.engine.graphics.gui;

import pe.engine.graphics.objects.StaticMesh2D;
import pe.util.color.Color;
import pe.util.math.Vec2f;

public class Divider extends GUIComponent{

	public static final String NAME = "div";
	
	public Divider(int width, int height, Color color){
		this.width = width;
		this.height = height;
		this.backgroundColor = color;
		
		Vec2f[] verteces = {
				new Vec2f(-0.5f, 0.5f),
				new Vec2f(0.5f, 0.5f),
				new Vec2f(0.5f, -0.5f),
				new Vec2f(-0.5f, -0.5f)
		};
		
		int[] indeces = {
				0, 1, 3,
				3, 1, 2
		};
		
		this.mesh = new StaticMesh2D(verteces, indeces);
		
		initShaderProgram();
	}
}
