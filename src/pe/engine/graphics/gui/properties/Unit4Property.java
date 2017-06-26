package pe.engine.graphics.gui.properties;

import pe.engine.data.UnitConversions;
import pe.engine.main.PE;
import pe.util.math.Vec2f;
import pe.util.math.Vec4f;

public class Unit4Property {

	private RPixSourceI rpixSource = null;
	private Unit4Property maxValue = null;
	private Vec4f value;
	private int[] units;
	
	public Unit4Property(){
		this.value = Vec4f.ZERO;
		this.units = new int[]{PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS, PE.GUI_UNIT_PIXELS};
	}
	
	public Unit4Property(Vec4f value, int[] units){
		if(units.length != 4)
			throw new IllegalArgumentException("There must be exactly 4 units given for a Vec4fPropterty");
		
		this.value = value;
		this.units = units;
	}
	
	public Unit4Property(Vec4f value, int[] units, Unit4Property maxValue, RPixSourceI rpixSource){
		if(units.length != 4)
			throw new IllegalArgumentException("There must be exactly 4 units given for a Vec4fPropterty");
		
		this.value = value;
		this.units = units;
		this.maxValue = maxValue;
		this.rpixSource = rpixSource;
	}
	
	public void set(int[] units){
		if(units.length != 4)
			throw new IllegalArgumentException("There must be exactly 4 units given for a Vec4fPropterty");
		
		this.set(units, this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public void set(int[] units, Unit4Property maxValue, float rpixRatio){
		if(units.length != 4)
			throw new IllegalArgumentException("There must be exactly 4 units given for a Vec4fPropterty");
		
		if(this.units == units)
			return;
		
		Vec4f maxValuePix = maxValue.pixels();
		
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
		
		switch(units[2]){
		case PE.GUI_UNIT_PIXELS:
			this.value.z = UnitConversions.toPixels(this.value.z, this.units[2], maxValuePix.z, rpixRatio);
			break;
		case PE.GUI_UNIT_PERCENT:
			this.value.z = UnitConversions.toPercent(this.value.z, this.units[2], maxValuePix.z, rpixRatio);
			break;
		case PE.GUI_UNIT_RPIXELS:
			this.value.z = UnitConversions.toRPixels(this.value.z, this.units[2], maxValuePix.z, rpixRatio);
			break;
		default:
			throw new IllegalArgumentException(
					"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
		}
		
		switch(units[3]){
		case PE.GUI_UNIT_PIXELS:
			this.value.w = UnitConversions.toPixels(this.value.w, this.units[3], maxValuePix.w, rpixRatio);
			break;
		case PE.GUI_UNIT_PERCENT:
			this.value.w = UnitConversions.toPercent(this.value.w, this.units[3], maxValuePix.w, rpixRatio);
			break;
		case PE.GUI_UNIT_RPIXELS:
			this.value.w = UnitConversions.toRPixels(this.value.w, this.units[3], maxValuePix.w, rpixRatio);
			break;
		default:
			throw new IllegalArgumentException(
					"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
		}
		
		this.units = units;
	}
	
	private static Vec4f toPixels(Vec4f value, int[] units, Unit4Property maxValue, float rpixRatio){
		Vec4f maxValuePix = maxValue.pixels();
		
		return new Vec4f(
				UnitConversions.toPixels(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toPixels(value.y, units[1], maxValuePix.y, rpixRatio),
				UnitConversions.toPixels(value.z, units[2], maxValuePix.z, rpixRatio),
				UnitConversions.toPixels(value.w, units[3], maxValuePix.w, rpixRatio)
				);
	}
	
	private static Vec4f toPercent(Vec4f value, int[] units, Unit4Property maxValue, float rpixRatio){
		Vec4f maxValuePix = maxValue.pixels();
		
		return new Vec4f(
				UnitConversions.toPercent(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toPercent(value.y, units[1], maxValuePix.y, rpixRatio),
				UnitConversions.toPercent(value.z, units[2], maxValuePix.z, rpixRatio),
				UnitConversions.toPercent(value.w, units[3], maxValuePix.w, rpixRatio)
				);
	}
	
	private static Vec4f toRPixels(Vec4f value, int[] units, Unit4Property maxValue, float rpixRatio){
		Vec4f maxValuePix = maxValue.pixels();
		
		return new Vec4f(
				UnitConversions.toRPixels(value.x, units[0], maxValuePix.x, rpixRatio),
				UnitConversions.toRPixels(value.y, units[1], maxValuePix.y, rpixRatio),
				UnitConversions.toRPixels(value.z, units[2], maxValuePix.z, rpixRatio),
				UnitConversions.toRPixels(value.w, units[3], maxValuePix.w, rpixRatio)
				);
	}
	
	public Vec4f pixels(Unit4Property maxValue, float rpixRatio){
		return toPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec4f pixels(){
		return this.pixels(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public Vec4f percent(Unit4Property maxValue, float rpixRatio){
		return toPercent(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec4f percent(){
		return this.percent(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public Vec4f rpixels(Unit4Property maxValue, float rpixRatio){
		return toRPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public Vec4f rpixels(){
		return this.rpixels(this.maxValue, this.rpixSource.getRPixRatio());
	}
	
	public void set(Vec4f value){
		this.value = value;
	}
	
	public void set(Vec4f value, int[] units){
		if(units.length != 4)
			throw new IllegalArgumentException("There must be exactly 4 units given for a Vec4fPropterty");
		
		this.value = value;
		this.units = units;
	}
	
	public Unit4Property setRPixSource(RPixSourceI rpixSource){
		this.rpixSource = rpixSource;
		
		return this;
	}
	
	public Unit4Property setMaxValue(Unit2Property maxValue){
		Vec2f maxValueVec = maxValue.getValue();
		Vec4f maxVal = Vec4f.ZERO;
		maxVal.x = maxValueVec.x;
		maxVal.y = maxValueVec.y;
		maxVal.z = maxValueVec.x;
		maxVal.w = maxValueVec.y;
		
		int[] maxValueVecUnits = maxValue.getUnits();
		int[] maxValUnits = new int[]{ maxValueVecUnits[0], maxValueVecUnits[1], maxValueVecUnits[0], maxValueVecUnits[1]};
		
		this.maxValue = new Unit4Property(maxVal, maxValUnits);
		
		return this;
	}
	
	public Unit4Property setMaxValue(Unit4Property maxValue){
		this.maxValue = maxValue;
		
		return this;
	}
	
	public Vec4f getValue(){
		return value;
	}
	
	public int[] getUnits(){
		return units;
	}
}
