package pe.engine.graphics.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import pe.engine.data.Resources;

public class Texture3D extends Texture {

	public Texture3D(String path, int textureWrapX, int textureWrapY, int textureWrapZ, boolean pixelatedFilter,
			boolean generateMipMap, int mipMapFilter) {

		this.id = GL11.glGenTextures();
		this.path = path;
		this.glDim = GL12.GL_TEXTURE_3D;

		bind();

		setTextureWrap(textureWrapX, textureWrapY, textureWrapZ);
		setTextureFilter(pixelatedFilter);
		if (generateMipMap)
			generateMipMap(mipMapFilter);

		unbind();

		Resources.add(this, this);
	}

	@Override
	public void setTextureWrap(int... textureWrapVars) {
		if (textureWrapVars.length != 3)
			throw new IllegalArgumentException("Only 3 texture wrap options may be given to a 3D texture.");

		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapVars[0]));
		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_T, getGLTextureWrap(textureWrapVars[1]));
		GL11.glTexParameteri(glDim, GL12.GL_TEXTURE_WRAP_R, getGLTextureWrap(textureWrapVars[2]));
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
