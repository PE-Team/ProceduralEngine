package pe.engine.graphics.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import pe.engine.data.Resources;
import pe.engine.main.PE;

public class Texture2D extends Texture {
	
	public static final Texture2D CLEAR = createClear();

	public static Texture2D createClear(){
		return new Texture2D(null, PE.TEXTURE_WRAP_REPEAT, PE.TEXTURE_WRAP_REPEAT, true, false, PE.MIPMAP_FILTER_SIGLE_PIXELATED);
	}
	
	public Texture2D(String path){
		this.id = GL11.glGenTextures();
		this.path = path;
		this.glDim = GL11.GL_TEXTURE_2D;

		forceBind();

		setTextureWrap(PE.TEXTURE_WRAP_BORDER, PE.TEXTURE_WRAP_BORDER);
		setTextureFilter(true);

		forceUnbind();

		Resources.add(this, this);
	}
	
	public Texture2D(String path, int textureWrapX, int textureWrapY, boolean pixelatedFilter, boolean generateMipMap,
			int mipMapFilter) {

		this.id = GL11.glGenTextures();
		this.path = path;
		this.glDim = GL11.GL_TEXTURE_2D;

		forceBind();

		setTextureWrap(textureWrapX, textureWrapY);
		setTextureFilter(pixelatedFilter);
		if(generateMipMap)
			generateMipMap(mipMapFilter);

		forceUnbind();

		Resources.add(this, this);
	}

	@Override
	public void setTextureWrap(int... textureWrapVars) {
		if (textureWrapVars.length != 2)
			throw new IllegalArgumentException("Only 2 texture wrap options may be given to a 2D texture.");

		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapVars[0]));
		GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_T, getGLTextureWrap(textureWrapVars[1]));
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
