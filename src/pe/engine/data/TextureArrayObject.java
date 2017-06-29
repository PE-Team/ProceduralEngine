package pe.engine.data;

import java.util.ArrayList;
import java.util.List;

import pe.engine.graphics.objects.Texture;
import pe.engine.graphics.objects.Texture2D;

public class TextureArrayObject{

	List<Texture> textures  = null;
	
	public static TextureArrayObject fillTexture2DClear(int textureCount){
		TextureArrayObject tao = new TextureArrayObject();
		
		for(int i = 0; i < textureCount; i++){
			tao.add(Texture2D.createClear());
		}
		
		return tao;
	}
	
	public static TextureArrayObject fillStaticTexture2DClear(int textureCount){
		TextureArrayObject tao = new TextureArrayObject();
		
		for(int i = 0; i < textureCount; i++){
			tao.add(Texture2D.CLEAR);
		}
		
		return tao;
	}
	
	public TextureArrayObject(){
		textures = new ArrayList<Texture>();
	}
	
	public void bind(){
		for(Texture texture:textures){
			texture.bind();
		}
	}
	
	public void unbind(){
		for(Texture texture:textures){
			texture.unbind();
		}
	}
	
	public void load(){
		for(Texture texture:textures){
			texture.load();
		}
	}
	
	public void add(Texture texture){
		texture.setLocation(textures.size());
		textures.add(texture);
	}
	
	public Texture remove(int textureLocation){
		int location = 0;
		for(Texture tex:textures){
			tex.setLocation(location);
			location++;
		}
		
		return textures.remove(textureLocation);
	}
	
	public void remove(Texture texture){
		textures.remove(texture);
		
		int location = 0;
		for(Texture tex:textures){
			tex.setLocation(location);
			location++;
		}
	}
	
	public void set(Texture texture, int textureLocation){
		textures.set(textureLocation, texture);
	}
	
	public void setPath(String path, int textureLocation){
		textures.get(textureLocation).setPath(path);
	}
	
	public Texture getTexture(int index){
		return textures.get(index);
	}
	
	public List<Texture> getTextures(){
		return textures;
	}
	
	public int length(){
		return textures.size();
	}
}
