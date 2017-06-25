package pe.engine.graphics.gui.properties;

import pe.engine.main.PE;
import pe.engine.main.UnitConversions;

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
			this.rotation = UnitConversions.toDegrees(rotation, this.units);
		case PE.ANGLE_UNIT_RADIANS:
			this.rotation = UnitConversions.toRadians(rotation, this.units);
		}
		
		this.units = units;
	}
	
	public void set(float rotation){
		this.rotation = rotation;
	}
	
	public void set(float rotation, int units){
		this.rotation = rotation;
		this.units = units;
	}
	
	public float degrees(){
		return UnitConversions.toDegrees(rotation, units);
	}
	
	public float radians(){
		return UnitConversions.toRadians(rotation, units);
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public int getUnits(){
		return units;
	}
}
