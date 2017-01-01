package ptg.test.main;

import ptg.util.maths.Vec2f;

public class TestObject {
	
	public static enum BOOLEAN{
		TRUE, FALSE;
	}
	
	public static final float final_value_f = 2557;
	public static float static_value_f = 2556;
	public static Vec2f static_vec2f = new Vec2f(25.0f, 2.0f);
	
	public String name;
	public float value_f;
	public BOOLEAN bool;

	public TestObject(String name, float value_f){
		this.name = name;
		this.value_f = value_f;
	}
	
	public TestObject(BOOLEAN bool){
		this.name = "BOOLEAN enum test";
		this.value_f = 0;
		this.bool = bool;
	}
	
	public String toString(){
		return name + ", " + value_f + ", " + bool;
	}
}
