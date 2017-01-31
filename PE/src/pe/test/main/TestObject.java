package pe.test.main;

import pe.util.math.Vec2f;

public class TestObject {
	
	public static enum BOOLEAN{
		TRUE, FALSE;
	}
	
	public static final float final_valuef = 2557;
	public static float static_valuef = 2556;
	public static Vec2f static_vec2f = new Vec2f(25.0f, 2.0f);
	
	public String name;
	public float valuef;
	public BOOLEAN bool;

	public TestObject(String name, float valuef){
		this.name = name;
		this.valuef = valuef;
	}
	
	public TestObject(BOOLEAN bool){
		this.name = "BOOLEAN_ENUM_TEST";
		this.valuef = 0;
		this.bool = bool;
	}
	
	public TestObject set(float numb){
		this.valuef = numb;
		return this;
	}
	
	public String toString(){
		return name + ", " + valuef + ", " + bool;
	}
}
