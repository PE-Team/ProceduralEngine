package pe.engine.graphics.objects;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.main.PE;

public abstract class Texture implements DisposableResource{

	protected int id;
	protected int glDim;
	protected String path;
	protected int location;

	protected void initializeTexture(String path, int dimension, int textureWrapX, int textureWrapY, int textureWrapZ,
			boolean pixelatedFilter, boolean generateMipMap, int mipMapFilter) {
		this.id = GL11.glGenTextures();
		this.path = path;

		int glTexFilter = getGLTextureFilter(pixelatedFilter);
		int glMipMapFilter = getGLMipMapFilter(mipMapFilter);

		switch (dimension) {
		case 1:
		case PE.TEXTURE_1D:
			
			this.glDim = GL11.GL_TEXTURE_1D;
			
			GL11.glBindTexture(glDim, id);
			
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapX));

			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glTexFilter);
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glTexFilter);

			if (generateMipMap) {
				GL30.glGenerateMipmap(glDim);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glMipMapFilter);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glMipMapFilter);
			}

			break;
		case 2:
		case PE.TEXTURE_2D:
			
			this.glDim = GL11.GL_TEXTURE_2D;
			
			GL11.glBindTexture(glDim, id);

			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapX));
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_T, getGLTextureWrap(textureWrapY));

			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glTexFilter);
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glTexFilter);

			if (generateMipMap) {
				GL30.glGenerateMipmap(glDim);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glMipMapFilter);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glMipMapFilter);
			}

			break;
		case 3:
		case PE.TEXTURE_3D:
			
			this.glDim = GL12.GL_TEXTURE_3D;
			
			GL11.glBindTexture(glDim, id);

			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_S, getGLTextureWrap(textureWrapX));
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_WRAP_T, getGLTextureWrap(textureWrapY));
			GL11.glTexParameteri(glDim, GL12.GL_TEXTURE_WRAP_R, getGLTextureWrap(textureWrapZ));

			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glTexFilter);
			GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glTexFilter);

			if (generateMipMap) {
				GL30.glGenerateMipmap(glDim);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MIN_FILTER, glMipMapFilter);
				GL11.glTexParameteri(glDim, GL11.GL_TEXTURE_MAG_FILTER, glMipMapFilter);
			}

			break;
		default:
			throw new IllegalArgumentException("Textures with a dimension of '" + dimension + "' are not supported.");
		}

		unbind();
		
		Resources.add(this, this);
	}
	
	public int getLocation(){
		return location;
	}
	
	public void setLocation(int location){
		if(location < 0 || location > 30)
			throw new IllegalArgumentException("The location for the texture must be between 0 and 16 (inclusive).");
		
		this.location = location;
	}
	
	public void use(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + location);
		GL11.glBindTexture(glDim, id);
	}
	
	public void unbind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + location);
		GL11.glBindTexture(glDim, 0);
	}
	
	public void load(){
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			IntBuffer compBuffer = stack.mallocInt(1);

			STBImage.stbi_set_flip_vertically_on_load(true);
			ByteBuffer image = STBImage.stbi_load(path, widthBuffer, heightBuffer, compBuffer, 4);
			if (image == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}
			
			int width = widthBuffer.get();
			int height = heightBuffer.get();
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		}
	}

	private int getGLMipMapFilter(int mipMapFilter) {
		switch (mipMapFilter) {
		case PE.MIPMAP_FILTER_BILINEAR:
			return GL11.GL_LINEAR_MIPMAP_NEAREST;
		case PE.MIPMAP_FILTER_DUO_PIXELATED:
			return GL11.GL_NEAREST_MIPMAP_LINEAR;
		case PE.MIPMAP_FILTER_SIGLE_PIXELATED:
			return GL11.GL_NEAREST_MIPMAP_NEAREST;
		case PE.MIPMAP_FILTER_TRILINEAR:
			return GL11.GL_LINEAR_MIPMAP_LINEAR;
		}
		throw new IllegalArgumentException("The given texture wrap option is not supported");
	}

	private int getGLTextureFilter(boolean pixelatedFilter) {
		if (pixelatedFilter)
			return GL11.GL_NEAREST;
		return GL11.GL_LINEAR;
	}

	private int getGLTextureWrap(int textureWrap) {
		switch (textureWrap) {
		case PE.TEXTURE_WRAP_BORDER:
			return GL13.GL_CLAMP_TO_BORDER;
		case PE.TEXTURE_WRAP_CLAMP:
			return GL12.GL_CLAMP_TO_EDGE;
		case PE.TEXTURE_WRAP_MIRROR:
			return GL14.GL_MIRRORED_REPEAT;
		case PE.TEXTURE_WRAP_REPEAT:
			return GL11.GL_REPEAT;
		}
		throw new IllegalArgumentException("The given texture wrap option is not supported");
	}
	
	public void unload(){
		Resources.dispose(this);
	}
	
	public void dispose(){
		GL11.glDeleteTextures(id);
	}
}
