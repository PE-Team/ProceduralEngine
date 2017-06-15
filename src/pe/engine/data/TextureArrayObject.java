package pe.engine.data;

import java.util.HashSet;
import java.util.Set;

import pe.engine.graphics.objects.Texture;

public class TextureArrayObject{

	Set<Texture> textures  = null;
	
	public TextureArrayObject(){
		textures = new HashSet<Texture>();
	}
	
	public void use(){
		for(Texture texture:textures){
			texture.use();
		}
	}
	
	public void unbind(){
		for(Texture texture:textures){
			texture.unbind();
		}
	}
	
	public void load(){
		for(Texture texture:textures){
			texture.use();
			texture.load();
			texture.unbind();
		}
	}
	
	public void unload(){
		for(Texture texture:textures){
			texture.unload();
		}
	}
	
	public void add(Texture texture){
		texture.setLocation(textures.size());
		textures.add(texture);
	}
	
	public void remove(int textureLocation){
		for(Texture texture:textures){
			if(texture.getLocation() == textureLocation){
				remove(texture);
			}
		}
	}
	
	public void remove(Texture texture){
		textures.remove(texture);
		texture.unload();
		
		int location = 0;
		for(Texture tex:textures){
			tex.setLocation(location);
			location++;
		}
	}
	
	public Set<Texture> getTextures(){
		return textures;
	}
	
	public int length(){
		return textures.size();
	}
}
