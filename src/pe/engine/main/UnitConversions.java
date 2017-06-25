package pe.engine.main;

import org.lwjgl.glfw.GLFW;

import pe.util.math.Vec2f;
import pe.util.math.Vec3f;
import pe.util.math.Vec4f;

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

	/**
	 * Converts the given rotation to degress based on the units of the given
	 * rotation.
	 * 
	 * @param rotation
	 *            The rotation in the given units.
	 * @param rotationUnit
	 *            The given units for the rotation.
	 * 
	 * @return The rotation in degrees.
	 * 
	 * @since 1.0
	 */
	public static float toDegrees(float rotation, int rotationUnit) {
		switch (rotationUnit) {
		case PE.ANGLE_UNIT_DEGREES:
			return rotation;
		case PE.ANGLE_UNIT_RADIANS:
			return (float) Math.toDegrees(rotation);
		}
		throw new IllegalArgumentException(
				"An invalid rotation unit of measurement was given.\nMake sure the rotation is in degrees or radians.");
	}
	
	/**
	 * Converts the given rotation to radians based on the units of the given
	 * rotation.
	 * 
	 * @param rotation
	 *            The rotation in the given units.
	 * @param rotationUnit
	 *            The given units for the rotation.
	 * 
	 * @return The rotation in radians.
	 * 
	 * @since 1.0
	 */
	public static float toRadians(float rotation, int rotationUnit) {
		switch (rotationUnit) {
		case PE.ANGLE_UNIT_DEGREES:
			return (float) Math.toRadians(rotation);
		case PE.ANGLE_UNIT_RADIANS:
			return rotation;
		}
		throw new IllegalArgumentException(
				"An invalid rotation unit of measurement was given.\nMake sure the rotation is in degrees or radians.");
	}

	/**
	 * Converts the given value to pixels based on its units and the values of
	 * <code>maxWidth</code> and <code>rpixRatio</code>. <code>maxWidth</code>
	 * corresponds the the value returned when <code>value</code> = 100%.
	 * <code>rpixRatio</code> is multiplied by the given value when the value is
	 * in <code>rpx</code>.
	 * 
	 * @param value
	 *            The value to convert to pixels.
	 * @param valueUnit
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The value returned with given value is in percent and at 100%.
	 *            Other values scale linearly to this.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by value.
	 * 
	 * @since 1.0
	 */
	public static float toPixels(float value, int valueUnit, float maxWidth, float rpixRatio) {
		switch (valueUnit) {
		case PE.GUI_UNIT_PIXELS:
			return value;
		case PE.GUI_UNIT_PERCENT:
			return value * maxWidth;
		case PE.GUI_UNIT_RPIXELS:
			return value * rpixRatio;
		}
		throw new IllegalArgumentException(
				"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The value returned with given value is in percent and at 100%.
	 *            Other values scale linearly to this.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec2f toPixels(Vec2f values, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		return new Vec2f(x, y);
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The value returned with given value is in percent and at 100%.
	 *            Other values scale linearly to this. Each value corresponds to
	 *            its element in <code>values</code>.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec2f toPixels(Vec2f values, int[] valueUnits, Vec2f maxWidth, float rpixRatio) {
		if (valueUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth.x, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth.y, rpixRatio);
		return new Vec2f(x, y);
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The value returned with given value is in percent and at 100%.
	 *            Other values scale linearly to this.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec3f toPixels(Vec3f values, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 3)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		float z = toPixels(values.z, valueUnits[2], maxWidth, rpixRatio);
		return new Vec3f(x, y, z);
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The value returned with given value is in percent and at 100%.
	 *            Other values scale linearly to this.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec4f toPixels(Vec4f values, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 4)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		float z = toPixels(values.z, valueUnits[2], maxWidth, rpixRatio);
		float w = toPixels(values.w, valueUnits[3], maxWidth, rpixRatio);
		return new Vec4f(x, y, z, w);
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The values returned with given value is in percent and at
	 *            100%. Other values scale linearly to this. Each value
	 *            corresponds to either the x value or the y value, switching
	 *            starting at x.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec4f toPixels(Vec4f values, int[] valueUnits, Vec2f maxWidth, float rpixRatio) {
		if (valueUnits.length != 4)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth.x, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth.y, rpixRatio);
		float z = toPixels(values.z, valueUnits[2], maxWidth.x, rpixRatio);
		float w = toPixels(values.w, valueUnits[3], maxWidth.y, rpixRatio);
		return new Vec4f(x, y, z, w);
	}

	/**
	 * Converts the given vector of values to pixels based on its units and the
	 * values of <code>maxWidth</code> and <code>rpixRatio</code>.
	 * <code>maxWidth</code> corresponds the the value returned when
	 * <code>value</code> = 100%. <code>rpixRatio</code> is multiplied by the
	 * given value when the value is in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param maxWidth
	 *            The values returned with given value is in percent and at
	 *            100%. Other values scale linearly to this. Each value
	 *            corresponds to its element in <code>values</code>.
	 * @param rpixRatio
	 *            The ratio to multiply the given value by to convert from rpx
	 *            to px.
	 * 
	 * @return The amount of pixels represented by the values.
	 * 
	 * @since 1.0
	 */
	public static Vec4f toPixels(Vec4f values, int[] valueUnits, Vec4f maxWidth, float rpixRatio) {
		if (valueUnits.length != 4)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = toPixels(values.x, valueUnits[0], maxWidth.x, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth.y, rpixRatio);
		float z = toPixels(values.z, valueUnits[2], maxWidth.z, rpixRatio);
		float w = toPixels(values.w, valueUnits[3], maxWidth.w, rpixRatio);
		return new Vec4f(x, y, z, w);
	}

	public static float convertFromPix(float value, int valueUnits, float maxWidth, float rpixRatio) {
		switch (valueUnits) {
		case PE.GUI_UNIT_PIXELS:
			return value;
		case PE.GUI_UNIT_RPIXELS:
			return value / rpixRatio;
		case PE.GUI_UNIT_PERCENT:
			return value / maxWidth;
		}
		throw new IllegalArgumentException(
				"An invalid unit was given.\nMake sure the units is either in pixels (px), percent (%), or relative pixels (rpx)");
	}

	public static Vec2f convertFromPix(Vec2f value, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = convertFromPix(value.x, valueUnits[0], maxWidth, rpixRatio);
		float y = convertFromPix(value.y, valueUnits[1], maxWidth, rpixRatio);

		return new Vec2f(x, y);
	}

	public static Vec2f convertFromPix(Vec2f value, int[] valueUnits, Vec2f maxWidth, float rpixRatio) {
		if (valueUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = convertFromPix(value.x, valueUnits[0], maxWidth.x, rpixRatio);
		float y = convertFromPix(value.y, valueUnits[1], maxWidth.y, rpixRatio);

		return new Vec2f(x, y);
	}

	public static Vec3f convertFromPix(Vec3f value, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 3)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = convertFromPix(value.x, valueUnits[0], maxWidth, rpixRatio);
		float y = convertFromPix(value.y, valueUnits[1], maxWidth, rpixRatio);
		float z = convertFromPix(value.z, valueUnits[2], maxWidth, rpixRatio);

		return new Vec3f(x, y, z);
	}

	public static Vec3f convertFromPix(Vec3f value, int[] valueUnits, Vec3f maxWidth, float rpixRatio) {
		if (valueUnits.length != 3)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = convertFromPix(value.x, valueUnits[0], maxWidth.x, rpixRatio);
		float y = convertFromPix(value.y, valueUnits[1], maxWidth.y, rpixRatio);
		float z = convertFromPix(value.z, valueUnits[2], maxWidth.z, rpixRatio);

		return new Vec3f(x, y, z);
	}

	public static Vec4f convertFromPix(Vec4f value, int[] valueUnits, float maxWidth, float rpixRatio) {
		if (valueUnits.length != 4)
			throw new IllegalArgumentException("There must be the same number of units as values.");

		float x = convertFromPix(value.x, valueUnits[0], maxWidth, rpixRatio);
		float y = convertFromPix(value.y, valueUnits[1], maxWidth, rpixRatio);
		float z = convertFromPix(value.z, valueUnits[2], maxWidth, rpixRatio);
		float w = convertFromPix(value.w, valueUnits[3], maxWidth, rpixRatio);

		return new Vec4f(x, y, z, w);
	}
}
