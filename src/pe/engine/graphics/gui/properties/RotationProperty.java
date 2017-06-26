package pe.engine.graphics.gui.properties;

import pe.engine.main.PE;
import pe.util.math.Maths;

public class RotationProperty {

	private float rotation;
	private int units;
	
	public RotationProperty(){
		this.rotation = 0;
		this.units = PE.ANGLE_UNIT_DEGREES;
	}
	
	public RotationProperty(float rotation, int units){
		this.rotation = rotation;
		this.units = units;
	}
	
	public void increase(float rotation){
		rotation += rotation;
	}
	
	public void decrease(float rotation){
		rotation -= rotation;
	}
	
	public void set(int units){
		if(this.units == units)
			return;
		
		switch(units){
		case PE.ANGLE_UNIT_DEGREES:
			this.rotation = toDeg(this.rotation, this.units);
			break;
		case PE.ANGLE_UNIT_RADIANS:
			this.rotation = toRad(this.rotation, this.units);
			break;
		}
		
		this.units = units;
	}
	
	private static float toDeg(float value, int units){
		switch(units){
		case PE.ANGLE_UNIT_DEGREES:
			return value;
		case PE.ANGLE_UNIT_RADIANS:
			return Maths.toDeg(value);
		}
		throw new IllegalArgumentException("The units given are not valid. Must be one of: Degrees, Radians.");
	}
	
	private static float toRad(float value, int units){
		switch(units){
		case PE.ANGLE_UNIT_DEGREES:
			return Maths.toRad(value);
		case PE.ANGLE_UNIT_RADIANS:
			return value;
		}
		throw new IllegalArgumentException("The units given are not valid. Must be one of: Degrees, Radians.");
	}
	
	public void set(float rotation){
		this.rotation = rotation;
	}
	
	public void set(float rotation, int units){
		this.rotation = rotation;
		this.units = units;
	}
	
	public float degrees(){
		return toDeg(rotation, units);
	}
	
	public float radians(){
		return toRad(rotation, units);
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public int getUnits(){
		return units;
	}
}
