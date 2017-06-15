package pe.engine.graphics.objects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import pe.engine.data.DisposableResource;
import pe.engine.data.Resources;
import pe.engine.main.PE;
import pe.util.color.Color;

public abstract class Texture implements DisposableResource{

	protected int id;
	protected int glDim;
	protected String path;
	protected int location;
	
	public int getLocation(){
		return location;
	}
	
	public void setLocation(int location){
		if(location < 0 || location > 30)
			throw new IllegalArgumentException("The location for the texture must be between 0 and 30 (inclusive).");
		
		this.location = location;
	}
	
	public void bind(){
		if(path == null)
			return;
		
		forceBind();
	}
	
	public void forceBind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + location);
		GL11.glBindTexture(glDim, id);
	}
	
	public void unbind(){
		if(path == null)
			return;
		
		forceUnbind();
	}
	
	public void forceUnbind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + location);
		GL11.glBindTexture(glDim, 0);
	}
	
	public void load(){
		if(path == null){
			/* Create a Clear texture */
			FloatBuffer fb = BufferUtils.createFloatBuffer(4);
			Color.CLEAR.putInBuffer4(fb);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, fb);
			
		}else{
			/* Try to load the texture from the path */
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
	}

	protected static int getGLMipMapFilter(int mipMapFilter) {
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

	protected static int getGLTextureFilter(boolean pixelatedFilter) {
		if (pixelatedFilter)
			return GL11.GL_NEAREST;
		return GL11.GL_LINEAR;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public abstract void setTextureWrap(int... textureWrapVars);
	
	public abstract void setTextureFilter(boolean pixelatedFilter);
	
	public abstract void generateMipMap(int mipMapFilter);

	protected static int getGLTextureWrap(int textureWrap) {
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