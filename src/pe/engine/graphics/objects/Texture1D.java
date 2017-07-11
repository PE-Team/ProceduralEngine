package pe.engine.graphics.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import pe.engine.data.Resources;
import pe.engine.main.PE;

public class Texture1D extends Texture {
	
	public Texture1D(String path) {
		this(path, PE.TEXTURE_WRAP_BORDER, true, false, PE.NULL);

	}

	public Texture1D(String path, int textureWrapX, boolean pixelatedFilter, boolean generateMipMap, int mipMapFilter) {

		this.id = GL11.glGenTextures();
		this.path = path;
		this.glDim = GL11.GL_TEXTURE_1D;

		bind();

		setTextureWrap(textureWrapX);
		setTextureFilter(pixelatedFilter);
		if (generateMipMap)
			generateMipMap(mipMapFilter);

		unbind();

		Resources.add(this, this);
	}

	@Override
	public void setTextureWrap(int... textureWrapVars) {
		if (textureWrapVars.length != 1)
			throw new IllegalArgumentException("Only 1 texture wrap option may be given to a 1D texture.");

		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapVars[0]));
	}

	@Override
	public void setTextureFilter(boolean pixelatedFilter) {
		int glTexFilter = getGLTextureFilter(pixelatedFilter);

		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glTexFilter);
		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glTexFilter);
	}

	@Override
	public void generateMipMap(int mipMapFilter) {
		int glMipMapFilter = getGLMipMapFilter(mipMapFilter);

		GL30.glGenerateMipmap(glDim);
		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glMipMapFilter);
		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glMipMapFilter);
	}
}
