package pe.engine.graphics.gui;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import pe.engine.graphics.objects.StaticMesh2D;
import pe.util.Util;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.shapes.Polygon;
import pe.util.shapes.Rectangle;
import pe.util.shapes.Triangle;

public class Divider extends GUIComponent{

	public static final String NAME = "div";
	
	public Divider(int width, int height, Color backgroundColor, Color borderColor, int borderWidth){
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
		this.center = new Vec2f(width / 2f, height / 2f);
		this.position = new Vec2f(300, 200);
		
		Vec2f[] polygon = { new Vec2f(1, 1), new Vec2f(0, 1), new Vec2f(0, 2), new Vec2f(1, 2), new Vec2f(1, 3),
				new Vec2f(2, 3), new Vec2f(2, 2), new Vec2f(3, 2), new Vec2f(3, 1), new Vec2f(2, 1), new Vec2f(2, 0),
				new Vec2f(1, 0) };
		
//		this.shape = new Triangle(new Vec2f(0, 0), new Vec2f(1, 1), new Vec2f(1, 0));
		this.shape = new Rectangle(1, 1);
//		this.shape = new Polygon(polygon);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		
		Vec2f[][] inOut = Util.getBorderVertexInOut(shape.getVertices());
		
		FloatBuffer inVectors = BufferUtils.createFloatBuffer(2 * mesh.getVerticesCount());
		for(Vec2f in:inOut[0]){
			in.putInBuffer(inVectors);
			System.out.println("in: " + in);
		}
		inVectors.flip();
		mesh.addShaderAttrib(2, inVectors);
		
		FloatBuffer outVectors = BufferUtils.createFloatBuffer(2 * mesh.getVerticesCount());
		for(Vec2f out:inOut[1]){
			out.putInBuffer(outVectors);
			System.out.println("out: " + out);
		}
		outVectors.flip();
		mesh.addShaderAttrib(2, outVectors);
		
		mesh.setWireframe(true);
		
		initShaderProgram();
	}
}
