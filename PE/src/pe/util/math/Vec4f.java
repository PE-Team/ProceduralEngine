package pe.util.math;

public class Vec4f {

	public float x, y, z, w;
	
	public Vec4f(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Vec4f(Vec2f vec, float z, float w){
		this.x = vec.x;
		this.y = vec.y;
		this.z = z;
		this.w = w;
	}
	
	public Vec4f(Vec3f vec, float w){
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = w;
	}
	
	public Vec4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vec4f mul(float scale){
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		this.w *= scale;
		return this;
	}
	
	public static Vec4f add(Vec4f vec1, Vec4f vec2){
		return new Vec4f(vec2.x+vec1.x, vec2.y+vec1.y, vec2.z+vec1.z, vec2.w+vec1.w);
	}
	
	public static Vec4f subtract(Vec4f vec1, Vec4f vec2){
		return new Vec4f(vec2.x-vec1.x, vec2.y-vec1.y, vec2.z-vec1.z, vec2.w-vec1.w);
	}
	
	public static float dot(Vec4f vec1, Vec4f vec2){
		return vec1.x*vec2.x + vec1.y*vec2.y + vec1.z*vec2.z + vec1.w*vec2.w;
	}
	
	public static float angleBetween(Vec4f vec1, Vec4f vec2){
		return (float) Math.toDegrees(Math.acos((dot(vec1,vec2))/(vec1.length()*vec2.length())));
	}
	
	public Vec4f unit(){
		float length = this.length();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		this.w /= length;
		return this;
	}
	
	public float length(){
		return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w);
	}
	
	public String toString(){
		return "{" + this.x + "," + this.y + "," + this.z + "," + this.w + "}";
	}
}
