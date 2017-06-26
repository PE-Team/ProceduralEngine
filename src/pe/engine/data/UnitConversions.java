package pe.engine.data;

import org.lwjgl.glfw.GLFW;

import pe.engine.main.PE;
import pe.util.math.Maths;

public class UnitConversions {

	/**
	 * Converts the given mouse button from <code>PE</code> to what is used in
	 * <code>GLFW</code>.
	 * 
	 * @param PE_MouseButton
	 *            The <code>PE</code> mouse button.
	 *            
	 * @return The <code>GLFW</code> mouse button.
	 * 
	 * @since 1.0
	 */
	public static int toGLFWMouseButton(int PE_MouseButton) {
		return PE_MouseButton - PE.MOUSE_BUTTON_MIN + GLFW.GLFW_MOUSE_BUTTON_1;
	}

	/**
	 * Converts the given mouse button from <code>GLFW</code> to what is used in
	 * <code>PE</code>.
	 * 
	 * @param GLFW_MouseButton
	 *            The <code>GLFW</code> mouse button.
	 *            
	 * @return The <code>PE</code> mouse button.
	 * 
	 * @since 1.0
	 */
	public static int toPEMouseButton(int GLFW_MouseButton) {
		return GLFW_MouseButton - GLFW.GLFW_MOUSE_BUTTON_1 + PE.MOUSE_BUTTON_MIN;
	}

	public static float toPixels(float value, int units, float maxValue, float rpixRatio) {
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
	
	public static float toPercent(float value, int units, float maxValue, float rpixRatio){
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
	
	public static float toRPixels(float value, int units, float maxValue, float rpixRatio){
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
	
	public static float toDeg(float value, int units){
		switch(units){
		case PE.ANGLE_UNIT_DEGREES:
			return value;
		case PE.ANGLE_UNIT_RADIANS:
			return Maths.toDeg(value);
		}
		throw new IllegalArgumentException("The units given are not valid. Must be one of: Degrees, Radians.");
	}
	
	public static float toRad(float value, int units){
		switch(units){
		case PE.ANGLE_UNIT_DEGREES:
			return Maths.toRad(value);
		case PE.ANGLE_UNIT_RADIANS:
			return value;
		}
		throw new IllegalArgumentException("The units given are not valid. Must be one of: Degrees, Radians.");
	}
}
