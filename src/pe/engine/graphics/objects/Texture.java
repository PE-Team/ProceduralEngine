package pe.engine.graphics.objects;

import org.lwjgl.opengl.GL11;

import pe.engine.main.PE;

public abstract class Texture {
	
	private int id;
	
	public Texture(int dimension, int textureWrap){
		this.id = GL11.glGenTextures();
		
		switch(dimension){
		case 1: case PE.TEXTURE_1D:
			// TODO
			break;
		case 2: case PE.TEXTURE_2D:
			// TODO
			break;
		case 3: case PE.TEXTURE_3D:
			// TODO
			break;
		default:
			throw new IllegalArgumentException("Textures with a dimension of '" + dimension + "' are not supported.");
		}
	}
}
