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

	@Deprecated
	protected String path = null;

	public BufferedTexture2D(ByteBuffer textureData, int width, int height) {
		super(null);
		bind();
		loadByteBufferIntoTexture(textureData, width, height);
		unbind();
	}

	@Deprecated
	@Override
	public void load() {
		return;
	}

	/**
	 * This method has no purpose in a <code>BufferedTexture2D</code> as this
	 * texture has no path. It has therefore been deprecated.
	 * 
	 * @since 1.0
	 */
	@Deprecated
	@Override
	public void setPath(String path) {
		return;
	}

	@Deprecated
	@Override
	public void unload() {
		return;
	}
}
