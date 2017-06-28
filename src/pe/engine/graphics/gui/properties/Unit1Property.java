package pe.engine.graphics.gui.properties;

import pe.engine.main.PE;

public class Unit1Property {

	private RPixSourceI rpixSource = null;
	private Unit1Property maxValue = null;
	private float value;
	private int units;
	
	public Unit1Property(){
		this.value = 0;
		this.units = PE.GUI_UNIT_PIXELS;
	}
	
	public Unit1Property(float value, int units){
		this.value = value;
		this.units = units;
	}
	
	public Unit1Property(float value, int units, Unit1Property maxValue, RPixSourceI rpixSource){
		this.value = value;
		this.units = units;
		this.maxValue = maxValue;
		this.rpixSource = rpixSource;
	}
	
	public void set(int units){
		this.set(units, this.maxValue.pixels(), this.rpixSource.getRPixRatio());
	}
	
	public void set(int units, float maxValue, float rpixRatio){
		if(this.units == units)
			return;
		
		switch(units){
		case PE.GUI_UNIT_PIXELS:
			this.value = pixels(maxValue, rpixRatio);
			break;
		case PE.GUI_UNIT_PERCENT:
			this.value = percent(maxValue, rpixRatio);
			break;
		case PE.GUI_UNIT_RPIXELS:
			this.value = rpixels(maxValue, rpixRatio);
			break;
		}
		
		this.units = units;
	}
	
	private static float toPixels(float value, int units, float maxValue, float rpixRatio) {
		switch (units) {
		case PE.GUI_UNIT_PIXELS:
			return value;
		case PE.GUI_UNIT_PERCENT:
			return value * maxValue;
		case PE.GUI_UNIT_RPIXELS:
			return value * rpixRatio;
		}
		throw new IllegalArgumentException(
				"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
	}
	
	private static float toPercent(float value, int units, float maxValue, float rpixRatio){
		switch (units) {
		case PE.GUI_UNIT_PIXELS:
			return value / maxValue;
		case PE.GUI_UNIT_PERCENT:
			return value;
		case PE.GUI_UNIT_RPIXELS:
			return value / rpixRatio * maxValue;
		}
		throw new IllegalArgumentException(
				"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
	}
	
	private static float toRPixels(float value, int units, float maxValue, float rpixRatio){
		switch (units) {
		case PE.GUI_UNIT_PIXELS:
			return value / rpixRatio;
		case PE.GUI_UNIT_PERCENT:
			return value / rpixRatio * maxValue;
		case PE.GUI_UNIT_RPIXELS:
			return value;
		}
		throw new IllegalArgumentException(
				"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
	}
	
	public float pixels(float maxValue, float rpixRatio){
		return toPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public float pixels(){
		return this.pixels(this.maxValue.pixels(), this.rpixSource.getRPixRatio());
	}
	
	public float percent(float maxValue, float rpixRatio){
		return toPercent(this.value, this.units, maxValue, rpixRatio);
	}
	
	public float percent(){
		return this.percent(this.maxValue.pixels(), this.rpixSource.getRPixRatio());
	}
	
	public float rpixels(float maxValue, float rpixRatio){
		return toRPixels(this.value, this.units, maxValue, rpixRatio);
	}
	
	public float rpixels(){
		return this.rpixels(this.maxValue.pixels(), this.rpixSource.getRPixRatio());
	}
	
	public void set(float value){
		this.value = value;
	}
	
	public void set(float value, int units){
		this.value = value;
		this.units = units;
	}
	
	public float getValue(){
		return value;
	}
	
	public int getUnits(){
		return units;
	}
}
