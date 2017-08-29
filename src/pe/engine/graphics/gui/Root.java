package pe.engine.graphics.gui;

import pe.engine.data.FrameBufferObject;
import pe.engine.graphics.main.handlers.WindowInputEvent;
import pe.engine.graphics.objects.Texture;
import pe.engine.graphics.objects.Texture2D;
import pe.engine.main.PE;
import pe.util.color.Color;
import pe.util.math.Mat3f;
import pe.util.math.Mat4f;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;

public class Root extends GUIComponentNew {
	
	private static Texture2D test = Texture2D.createClear();
	private static Vec4f zero = Vec4f.zero();

	public Root(){
		super(Color.BLUE, Color.CLEAR, Vec4f.zero(), Vec4f.zero(),
				true, Vec4f.zero(), Vec4f.zero(), Vec2f.zero(), Vec2f.zero(), 0, Vec2f.zero(),
				new Vec2f(1.0f, 1.0f), 0);
		
		this.size.set(new Vec2f(1.0f, 1.0f), new int[]{PE.GUI_UNIT_PERCENT, PE.GUI_UNIT_PERCENT}); 
	}

	@Override
	@Deprecated
	protected boolean onPress(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onRelease(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onClick(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onScroll(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onDrag(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onHover(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onType(WindowInputEvent e) {
		return false;
	}

	@Override
	@Deprecated
	protected boolean onOutsideRelease(WindowInputEvent e) {
		return false;
	}

	@Override
	protected boolean invokeInputEvent(WindowInputEvent e, boolean disposed) {
		return invokeChildrenInputEvent(e, disposed);
	}

	@Override
	@Deprecated
	protected boolean invokeSelfInputEvent(WindowInputEvent e, boolean disposed) {
		return disposed;
	}

	@Override
	@Deprecated
	public void setRotation(float degrees) {
		return;
	}

	@Override
	@Deprecated
	public void rotate(float degrees) {
		return;
	}

	@Override
	@Deprecated
	public void setZIndex(float index) {
		return;
	}

	@Override
	public Texture2D render(FrameBufferObject UNUSED) {

		Vec2f sizePix = size.pixels();
		
//		fbo.bind();
//		fbo.setColorBufferTexture(DEFAULT_RENDER_LOCATION, (int) sizePix.x, (int) sizePix.y);
//		fbo.useColorBuffer(DEFAULT_RENDER_LOCATION);

		Mat3f transformation = new Mat3f().scale(sizePix);
		Mat4f projection = gui.getWindow().getOrthoProjection();
//		Mat4f projection = fbo.getOrthographicMatrix(DEFAULT_RENDER_LOCATION);
		
//		renderChildren(fbo, transformation, projection);
		
//		Texture2D result = fbo.getColorBufferTexture(DEFAULT_RENDER_LOCATION);

//		fbo.unbind();
		
//		FrameBufferObject.bindDefault();
		
//		result.bind();
		
//		contentProgram.start();
//		test.bind();
//		
//		contentProgram.setUniformMat3f("transformation", transformation);
//		contentProgram.setUniformMat4f("projection", projection);
//		contentProgram.setUniformTexture("texture", test);
//		
//		mesh.render();
//		
//		test.unbind();
//		contentProgram.stop();
		
		renderBackground(transformation, projection, sizePix, test, zero, zero);
		
//		result.unbind();
		
		return null;
	}

//	@Override
//	@Deprecated
//	protected void renderBackground(Mat3f transformation, Mat4f projection, Vec2f size, Texture backgroundTexture,
//			Vec4f borderWidth, Vec4f borderRadius) {
//
//		return;
//	}

	@Override
	@Deprecated
	protected void renderForeground(Mat3f transformation, Mat4f projection, Vec2f size, Texture foregroundTexture,
			Vec4f borderWidth, Vec4f borderRadius) {

		return;
	}
}
