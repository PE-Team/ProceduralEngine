package pe.engine.data;

import java.util.HashMap;
import java.util.Map;

import pe.engine.graphics.objects.Texture;
import pe.engine.main.PE;

public class TextureSwapArray {

	private Map<Integer, TextureArrayObject> textureArrays;
	private int currentTAO = PE.NULL;
	private boolean autoUnload = false;

	public TextureSwapArray() {
		this.textureArrays = new HashMap<Integer, TextureArrayObject>();
	}

	public TextureSwapArray(boolean autoUnload) {
		this.textureArrays = new HashMap<Integer, TextureArrayObject>();
		this.autoUnload = autoUnload;
	}

	/**
	 * Adds a <code>TextureArrayObject</code> to this array and returns the key
	 * to that TAO. More specifically, the TAO will first try to be bound at the
	 * value of number of TAOs currently in this array. If the key for this
	 * value already exits, then it will continue to try incrementing the key
	 * until an empty key is found. It will then return this new key.
	 * 
	 * @param textureArray
	 *            The TAO to add.
	 * 
	 * @return The key the TAO was bound to.
	 * 
	 * @see TextureSwapArray
	 * 
	 * @since 1.0
	 */
	public int add(TextureArrayObject textureArray) {
		int key = textureArrays.size();
		if (textureArrays.containsKey(key)) {
			return add(textureArray, ++key);
		} else {
			textureArrays.put(key, textureArray);
			return key;
		}
	}

	/**
	 * Adds a <code>TextureArrayObject</code> with the specified key to this
	 * array and returns the key to that TAO. More specifically, the TAO will
	 * first try to be bound at the value of the key. If the key for this value
	 * already exits, then it will continue to try incrementing the key until an
	 * empty key is found. It will then return this new key.
	 * 
	 * @param textureArray
	 *            The TAO to add.
	 * 
	 * @param key
	 *            The key to try and bind the TAO to.
	 * 
	 * @return The key the TAO was bound to.
	 * 
	 * @see TextureSwapArray
	 * 
	 * @since 1.0
	 */
	public int add(TextureArrayObject textureArray, int key) {
		if (textureArrays.containsKey(key)) {
			return add(textureArray, ++key);
		} else {
			textureArrays.put(key, textureArray);
			return key;
		}
	}

	/**
	 * Adds a <code>Texture</code> to the <code>TextureArrayObject</code> at the
	 * given key and then returns the textures location in the TAO.
	 * 
	 * @param texture
	 *            The texture to add.
	 * @param key
	 *            The key for the TAO to add it to.
	 * @return The location of the texture add.
	 * 
	 * @throws NullPointerException
	 *             If the key does not already exist in the TAO.
	 * 
	 * @since 1.0
	 */
	public int add(Texture texture, int key) {
		textureArrays.get(key).add(texture);
		return texture.getLocation();
	}

	/**
	 * Removes the given key and the <code>Texture Object Array</code> associated with it.
	 * 
	 * @param key
	 *            The key and values associated with it to remove.
	 * 
	 * @since 1.0
	 */
	public void remove(int key) {
		textureArrays.remove(key);
	}

	/**
	 * Sets whether or not this <code>TextureSwapArray</code> should unload TAOs
	 * when they are swapped out.
	 * 
	 * @param autoUnload
	 *            Whether or not to automatically load them.
	 * 
	 * @since 1.0
	 */
	public void autoUnload(boolean autoUnload) {
		this.autoUnload = autoUnload;
	}

	/**
	 * Binds the currently swapped in <code>TextureArrayObject</code>.
	 * 
	 * @since 1.0
	 */
	public void bind() {
		textureArrays.get(currentTAO).bind();
	}

	/**
	 * Unbinds the currently swapped in <code>TextureArrayObject</code>.
	 * 
	 * @since 1.0
	 */
	public void unbind() {
		textureArrays.get(currentTAO).unbind();
	}

	/**
	 * Swaps the current <code>TextureArrayObject</code> to the TAO bound to the
	 * given key after first unbinding the old one and then binding the new one.
	 * If <code>autoUnload</code> is enabled, it will also unload the old TAO
	 * and load the new one.
	 * 
	 * @param key
	 *            They key for the new TAO to use.
	 * 
	 * @since 1.0
	 */
	public void swap(int key) {
		if (currentTAO == key)
			return;

		TextureArrayObject currentTextureArray = textureArrays.get(currentTAO);
		if (currentTAO != PE.NULL) {
			currentTextureArray.unbind();
			if (autoUnload) {
				currentTextureArray.unload();
			}
		}

		currentTAO = key;
		currentTextureArray = textureArrays.get(currentTAO);

		if (autoUnload) {
			currentTextureArray.load();
		}

		currentTextureArray.bind();
	}

	/**
	 * Toggles between using the TAO bound to <code>key1</code> or the TAO bound
	 * to <code>key2</code>. If the current TAO is neither keys, then it will
	 * automatically be bound to <code>key1</code>.
	 * 
	 * @param key1
	 *            The first key to toggle between. The default key.
	 * @param key2
	 *            The second key to toggle between.
	 * 
	 * @since 1.0
	 */
	public void toggle(int key1, int key2) {
		if (this.currentTAO == key1) {
			swap(key2);
		} else {
			swap(key1);
		}
	}
}
