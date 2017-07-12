package pe.engine.graphics.objects;

import java.nio.ByteBuffer;

/**
 * Unlike unbuffered textures, buffered textures have their texture loaded to
 * OpenGL upon their creation. These texture are also only created at runtime
 * using buffers. As such, they do not have a path.
 * 
 * Therefore, the variable <code>path</code> and method <code>setPath()</code>
 * have been deprecated. Additionally, the methods <code>load</code> and
 * <code>unload</code> are also deprecated since the texture is loaded to OpenGL
 * on creation, and has no outside reference so unloading the texture would be
 * the equivalent of deleting it. For safety, all of the deprecated methods have
 * their functionality overridden to just return (do nothing).
 * 
 * @author Ethan Penn
 * 
 * @since 1.0
 */
public class BufferedTexture2D extends Texture2D {

	protected String path = null;

	public BufferedTexture2D() {
		super(null);
	}

	public BufferedTexture2D(String path, int textureWrapX, int textureWrapY, boolean pixelatedFilter,
			boolean generateMipMap, int mipMapFilter) {
		super(null, textureWrapX, textureWrapY, pixelatedFilter, generateMipMap, mipMapFilter);
	}

	/**
	 * The texture is automatically loaded in at runtime and there is no source
	 * location. If the texture is ever unloaded, the texture data is lost, thus
	 * there is never a need to call <code>load()<code> as this function has
	 * been replaced by <code>load(width, height)</code> and <code>load(Buffer,
	 * width, height)<code>.
	 * 
	 * @see #load(ByteBuffer, int, int)
	 * 
	 * @since 1.0
	 */
	@Deprecated
	@Override
	public void load() {
		return;
	}

	/**
	 * Loads an empty <code>ByteBuffer</code> into memory through
	 * <code>load(ByteBuffer, width, height)</code>. This essentially will
	 * create a texture with the given width and height of black, clear pixels.
	 * 
	 * @param width
	 *            The width of the desired texture in pixels.
	 * @param height
	 *            The height of the desired texture in pixels.
	 * 
	 * @see #load(ByteBuffer, int, int)
	 * 
	 * @since 1.0
	 */
	public void load(int width, int height) {
		load((ByteBuffer) null, width, height);
	}

	/**
	 * Loads a <code>ByteBuffer<code> object into memory. Each byte represents a
	 * single color channel (RGBA), and the formate of the buffer must be in
	 * RGBA. Thus, the buffer must have a size equal to <code>4 * width * height</code>.
	 * 
	 * @param buffer
	 *            The buffer to upload as texture data.
	 * @param width
	 *            The width of the buffer (width of the texture) in pixels.
	 * @param height
	 *            The height of the buffer (height of the texture) in pixels.
	 * 
	 * @since 1.0
	 */
	public void load(ByteBuffer buffer, int width, int height) {
		loadByteBufferIntoTexture(buffer, width, height);
	}

	/**
	 * The texture is automatically loaded in at runtime and there is not source
	 * location, thus there is not a path to load the texture from.
	 * 
	 * @param path
	 *            The path the texture should load from.
	 * 
	 * @since 1.0
	 */
	@Deprecated
	@Override
	public void setPath(String path) {
		return;
	}

	/**
	 * The texture is automatically loaded in at runtime and there is no source
	 * location, thus there is no need to get the path from this texture.
	 * 
	 * @since 1.0
	 */
	@Deprecated
	@Override
	public String getPath() {
		return null;
	}
}
