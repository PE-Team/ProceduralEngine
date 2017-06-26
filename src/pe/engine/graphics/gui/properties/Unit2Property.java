package pe.engine.graphics.gui.properties;

import pe.engine.data.UnitConversions;
import pe.engine.main.PE;
import pe.util.math.Vec2f;

public class Unit2Property {

	private RPixSourceI rpixSource = null;
	private Unit2Property maxValue = null;
	private Vec2f value;
	private int[] units;
	
	public Unit2Property(){
		this.value = Vec2f.ZERO;
		this.units = new int[]{PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	}
	
	public Unit2Property(Vec2f value, int[] units){
		if(units.length != 2)
			throw new IllegalArgumentException("There must be exactly 2 units given for a Vec2fPropterty");
		
		this.value = value;
		this.units = units;
	}
	
	public Unit2Property(Vec2f value, int[] units, Unit2Property maxValue, RPixSourceI rpixSource){
		if(units.length != 2)
			throw new IllegalArgumentException("There must be exactly 2 units given for a Vec2fPropterty");
		
		this.value = value;
		this.units = units;
		this.maxValue = maxValue;
		this.rpixSource = rpixSource;
	}
	
	public void set(int[] units){
		if(units.length != 2)
			throw new IllegalArgumentException("There must be exactly 2 units given for a Vec2fPropterty");
		
		this.set(units, this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public void set(int[] units, Unit2Property maxValue, float rpixRatio){
		if(units.length != 2)
			throw new IllegalArgumentException("There must be exactly 2 units given for a Vec2fPropterty");
		
		if(this.units == units)
			return;
		
		Vec2f maxValuePix = maxValue.pixels();
		
		switch(units[0]){
		case PE.GUI_UNIT_PIXELS:
			this.value.x = UnitConversions.toPixels(this.value.x, this.units[0], maxValuePix.x, rpixRatio);
			break;
		case PE.GUI_UNIT_PERCENT:
			this.value.x = UnitConversions.toPercent(this.value.x, this.units[0], maxValuePix.x, rpixRatio);
			break;
		case PE.GUI_UNIT_RPIXELS:
			this.value.x = UnitConversions.toRPixels(this.value.x, this.units[0], maxValuePix.x, rpixRatio);
			break;
		default:
			throw new IllegalArgumentException(
					"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
		}
		
		switch(units[1]){
		case PE.GUI_UNIT_PIXELS:
			this.value.y = UnitConversions.toPixels(this.value.y, this.units[1], maxValuePix.y, rpixRatio);
			break;
		case PE.GUI_UNIT_PERCENT:
			this.value.y = UnitConversions.toPercent(this.value.y, this.units[1], maxValuePix.y, rpixRatio);
			break;
		case PE.GUI_UNIT_RPIXELS:
			this.value.y = UnitConversions.toRPixels(this.value.y, this.units[1], maxValuePix.y, rpixRatio);
			break;
		default:
			throw new IllegalArgumentException(
					"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
		}
		
		this.units = units;
	}
	
	private static Vec2f toPixels(Vec2f value, int[] units, Unit2Property maxValue, float rpixRatio){
		Vec2f maxValuePix = maxValue.pixels();
		
		return new Vec2f(
				UnitConversions.toPixels(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toPixels(value.y, units[1], maxValuePix.y, rpixRatio)
				);
	}
	
	private static Vec2f toPercent(Vec2f value, int[] units, Unit2Property maxValue, float rpixRatio){
		Vec2f maxValuePix = maxValue.pixels();
		
		return new Vec2f(
				UnitConversions.toPercent(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toPercent(value.y, units[1], maxValuePix.y, rpixRatio)
				);
	}
	
	private static Vec2f toRPixels(Vec2f value, int[] units, Unit2Property maxValue, float rpixRatio){
		Vec2f maxValuePix = maxValue.pixels();
		
		return new Vec2f(
				UnitConversions.toRPixels(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toRPixels(value.y, units[1], maxValuePix.y, rpixRatio)
				);
	}
	
	public Vec2f pixels(Unit2Property maxValue, float rpixRatio){
		return toPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec2f pixels(){
		System.out.println(maxValue);
		return this.pixels(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public Vec2f percent(Unit2Property maxValue, float rpixRatio){
		return toPercent(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec2f percent(){
		return this.percent(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public Vec2f rpixels(Unit2Property maxValue, float rpixRatio){
		return toRPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec2f rpixels(){
		return this.rpixels(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public void set(Vec2f value){
		this.value = value;
	}
	
	public void set(Vec2f value, int[] units){
		if(units.length != 2)
			throw new IllegalArgumentException("There must be exactly 2 units given for a Vec2fPropterty");
		
		this.value = value;
		this.units = units;
	}
	
	public Unit2Property setRPixSource(RPixSourceI rpixSource){
		this.rpixSource = rpixSource;
		
		return this;
	}
	
	public Unit2Property setMaxValue(Unit2Property maxValue){
		this.maxValue = maxValue;
		
		return this;
	}
	
	public Vec2f getValue(){
		return value;
	}
	
	public int[] getUnits(){
		return units;
	}
}
