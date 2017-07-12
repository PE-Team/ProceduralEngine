package pe.engine.data;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import pe.engine.graphics.objects.Texture;

public class FrameBufferObject extends BufferObject {
	
	private Map<Integer, Texture> attatchedTextures;

	public FrameBufferObject() {
		this.id = GL30.glGenFramebuffers();
		this.attatchedTextures = new HashMap<Integer, Texture>();

		bind();
		useColorBuffer(0);
		unbind();

		Resources.add(this);
	}

	public void addColorBufferTexture(int bufferIndex, Texture texture) {
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + bufferIndex, texture.getID(), 0);
		attatchedTextures.replace(bufferIndex, texture);
	}
	
	public Texture getColorBufferTexture(int bufferIndex){
		return attatchedTextures.get(bufferIndex);
	}

	public void useColorBuffer(int bufferIndex) {
		if (bufferIndex < 0 || bufferIndex > 29)
			throw new IllegalArgumentException("You must use a Color Buffer of index at least 0 and at most 29");

		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + bufferIndex);
	}

	@Override
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, id);
	}

	@Override
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
	}

	@Override
	public void dispose() {
		GL30.glDeleteFramebuffers(id);
	}
}
