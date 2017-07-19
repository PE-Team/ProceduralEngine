package pe.engine.graphics.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import pe.engine.data.FrameBufferObject;
import pe.engine.graphics.objects.Texture2D;

public class GUIRenderingOrder {

	private GUIRenderingOrder() {
	}

	private List<GUIRenderable> order = new ArrayList<GUIRenderable>();

	public static Texture2D renderChildComponents(GUIComponentNew parent) {
		GUIRenderingOrder renderingOrder = new GUIRenderingOrder();
		renderingOrder.sortComponents(parent);
		
		FrameBufferObject fbo = parent.getFBO();
		
		fbo.bind();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for(GUIRenderable obj:renderingOrder.order){
			obj.render(parent.getFBO());
		}
		GL11.glDisable(GL11.GL_BLEND);
		fbo.unbind();
		
		return fbo.getColorBufferTexture(GUIRenderable.DEFAULT_RENDER_LOCATION);
	}

	private void sortComponents(GUIComponentNew parent) {
		for(GUIRenderable child:parent.children){
			if(child instanceof GUIComponentNew && !((GUIComponentNew) child).clipChildren()){
				sortComponents((GUIComponentNew) child);
			}else{
				sortComponent(child);
			}
		}
	}

	private void sortComponent(GUIRenderable comp) {
		int min = 0;
		int max = order.size();
		while(min != max){
			int mid = middle(min, max);
			if(isBefore(comp, order.get(mid))){
				max = mid;
			}else{
				min = mid + 1;
			}
		}
		order.add(min, comp);
	}
	
	private static int middle(int min, int max){
		return (min + max) / 2;
	}

	private static boolean isBefore(GUIRenderable comp1, GUIRenderable comp2) {
		return comp1.getZIndex() < comp2.getZIndex() ? true
				: (comp1.getZIndex() > comp2.getZIndex() ? false : (comp1.getTreeTier() < comp2.getTreeTier()));
	}
}
