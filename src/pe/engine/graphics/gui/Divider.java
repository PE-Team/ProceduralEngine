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

public class Divider extends GUIComponent {

	public static final String NAME = "div";

	public Divider(int width, int height, int posX, int posY, Color backgroundColor, Color borderColor, int borderWidth) {
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
		this.center = new Vec2f(width / 2f, height / 2f);
		this.position = new Vec2f(posX, posY);
		this.rotation = 0;

		this.shape = new Rectangle(1, 1);
		this.mesh = new StaticMesh2D(shape.getVertices(), shape.getIndices());
		
		Vec2f v0Border = Vec2f.ZERO;
		Vec2f v1Border = new Vec2f(1, 0);
		Vec2f v2Border = Vec2f.ZERO;
		Vec2f v3Border = new Vec2f(0, 1);
		
		FloatBuffer borderVectors = BufferUtils.createFloatBuffer(8); // dim * # of vectors = 2 * 4
		v0Border.putInBuffer(borderVectors);
		v1Border.putInBuffer(borderVectors);
		v2Border.putInBuffer(borderVectors);
		v3Border.putInBuffer(borderVectors);
		borderVectors.flip();
		mesh.addShaderAttrib(2, borderVectors);

		mesh.setWireframe(false);

		initShaderProgram();
	}
}