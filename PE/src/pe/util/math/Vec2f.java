package pe.util.math;

public class Vec2f {

	public float x, y;
	
	public Vec2f(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vec2f mul(float scale){
		this.x *= scale;
		this.y *= scale;
		return this;
	}
	
	public static Vec2f add(Vec2f vec1, Vec2f vec2){
		return new Vec2f(vec2.x+vec1.x, vec2.y+vec1.y);
	}
	
	public static Vec2f subtract(Vec2f vec1, Vec2f vec2){
		return new Vec2f(vec2.x-vec1.x, vec2.y-vec1.y);
	}
	
	public static float dot(Vec2f vec1, Vec2f vec2){
		return vec1.x*vec2.x + vec1.y*vec2.y;
	}
	
	public static Vec3f cross(Vec2f vec1, Vec2f vec2){
		return new Vec3f(0,0,vec1.x*vec2.y - vec1.y*vec2.x);
	}
	
	public static float angleBetween(Vec2f vec1, Vec2f vec2){
		return (float) Math.toDegrees(Math.acos((dot(vec1,vec2))/(vec1.length()*vec2.length())));
	}
	
	public Vec2f unit(){
		float length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}
	
	public float length(){
		return (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public String toString(){
		return "{" + this.x + "," + this.y + "}";
	}
}
