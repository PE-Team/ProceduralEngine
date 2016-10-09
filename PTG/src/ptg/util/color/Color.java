package ptg.util.color;

import ptg.util.maths.Maths;
import ptg.util.maths.Vec3f;
import ptg.util.maths.Vec4f;

public class Color {

	public float r,g,b,a;
	
	public Color(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
	}
	
	public Color(Vec3f vec){
		this.r = vec.x;
		this.g = vec.y;
		this.b = vec.z;
		this.a = 1;
	}
	
	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(Vec3f vec, float a){
		this.r = vec.x;
		this.g = vec.y;
		this.b = vec.z;
		this.a = a;
	}
	
	public Color(Vec4f vec){
		this.r = vec.x;
		this.g = vec.g;
		this.b = vec.z;
		this.a = vec.w;
	}
	
	public String toHex(){
		return Maths.intToHex(this.toInt());
	}
	
	public int toInt(){
		return ((int) this.r)<<24 + ((int) this.g)<<16 + ((int) this.b)<<8 + ((int) a);
	}
	
	public Color toDecimal(){
		this.r /= 255;
		this.g /= 255;
		this.b /= 255;
		return this;
	}
	
	public Color toCMY(){
		this.r = 255-r;
		this.g = 255-g;
		this.b = 255-b;
		return this;
	}
}
