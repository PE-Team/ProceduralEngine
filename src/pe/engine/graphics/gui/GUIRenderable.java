package pe.engine.graphics.gui;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import pe.engine.data.FrameBufferObject;
import pe.engine.graphics.gui.properties.RotationProperty;
import pe.engine.graphics.gui.properties.Unit2Property;
import pe.engine.graphics.gui.properties.Unit4Property;
import pe.engine.graphics.main.Window;
import pe.engine.graphics.objects.StaticMesh2D;
import pe.engine.graphics.objects.Texture2D;
import pe.util.color.Color;
import pe.util.math.Vec2f;
import pe.util.shapes.Rectangle;

public abstract class GUIRenderable {

	protected static final int DEFAULT_RENDER_LOCATION = 0;
	
	protected static StaticMesh2D mesh;
	
	protected Unit2Property size;
	protected Unit2Property positionOffset;
	protected FrameBufferObject fbo;
	protected GUINew gui;
	protected GUIRenderable parent;
	
	protected int treeTier = -1;

	public GUIRenderable() {
		this.fbo = new FrameBufferObject();
		this.treeTier = -1;
		
		initMeshProperties();
	}
	
	protected void initMeshProperties() {
		if(mesh != null)
			return;
		
		StaticMesh2D m = StaticMesh2D.fromPolygon(new Rectangle(1, 1));
		Vec2f v0Border = new Vec2f(0, 1);
		Vec2f v1Border = new Vec2f(1, 1);
		Vec2f v2Border = new Vec2f(1, 0);
		Vec2f v3Border = new Vec2f(0, 0);

		FloatBuffer borderVectors = BufferUtils.createFloatBuffer(8);

		v0Border.putInBuffer(borderVectors);
		v1Border.putInBuffer(borderVectors);
		v2Border.putInBuffer(borderVectors);
		v3Border.putInBuffer(borderVectors);
		borderVectors.flip();
		m.addShaderAttrib(2, borderVectors);

		m.setWireframe(false);
		
		mesh = m;
	}
	
	public abstract Texture2D render(Vec2f projectedPosition, FrameBufferObject fbo);

	public GUIRenderable getParent() {
		return parent;
	}

	public void setParent(GUIRenderable parent) {
		this.parent = parent;
		
		if(parent != null){
			this.treeTier = parent.getTreeTier() + 1;
		}
	}
	
	public GUINew getGUI(){
		return gui;
	}
	
	public void setGUI(GUINew gui) {
		this.gui = gui;

		updateProperties();
	}

	protected void updateSelfProperties() {
		Window window = gui.getWindow();

		Unit2Property parentSize = parent == null ? window.getSize() : parent.getSize();
		
		this.size.setMaxValue(parentSize).setRPixSource(window);
		this.positionOffset.setMaxValue(size).setRPixSource(window);
	}

	public void updateProperties() {
		updateSelfProperties();
	}
	
	public FrameBufferObject getFBO(){
		return fbo;
	}
	
	public void setTreeTier(int tier){
		this.treeTier = tier;
	}
	
	public int getTreeTier(){
		return treeTier;
	}

	public Unit2Property getSize() {
		return size;
	}

	public void setSize(Unit2Property size) {
		this.size = size;
	}

	public Unit2Property getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(Unit2Property positionOffset) {
		this.positionOffset = positionOffset;
	}

	public GUINew getGui() {
		return gui;
	}
}
