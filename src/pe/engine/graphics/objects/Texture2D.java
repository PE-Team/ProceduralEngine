package pe.engine.graphics.objects;

import pe.engine.main.PE;

public class Texture2D extends Texture {

	public Texture2D(String path, int textureWrapX, int textureWrapY, boolean pixelatedFilter, boolean generateMipMap,
			int mipMapFilter) {
		
		initializeTexture(path, 2, textureWrapX, textureWrapY, PE.NULL, pixelatedFilter, generateMipMap, mipMapFilter);
	}
}
