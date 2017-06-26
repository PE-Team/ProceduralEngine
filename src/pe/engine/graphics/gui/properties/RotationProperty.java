package pe.engine.graphics.gui.properties;

import pe.engine.data.UnitConversions;
import pe.engine.main.PE;

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
			this.rotation = UnitConversions.toDeg(this.rotation, this.units);
			break;
		case PE.ANGLE_UNIT_RADIANS:
			this.rotation = UnitConversions.toRad(this.rotation, this.units);
			break;
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
		return UnitConversions.toDeg(rotation, units);
	}
	
	public float radians(){
		return UnitConversions.toRad(rotation, units);
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public int getUnits(){
		return units;
	}
}
