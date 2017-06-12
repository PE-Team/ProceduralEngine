package pe.engine.main;

import pe.util.math.Vec2f;
import pe.util.math.Vec3f;
import pe.util.math.Vec4f;

public class PE {

	/*
	 * Each Global Constant has an ID and a Version. The ID is a number which
	 * groups like variables together. The version is a number which is added
	 * onto the ID to separate the IDs. An ID is created by inputing a String
	 * (max 5 characters) in the StringToIntConverter and using the
	 * CHARSET_LETTERS charset
	 */

																// ID: NULL = 18337
	public static final int NULL = 183370; 						//  Version = 0

																// ID: MSHTP = 7669051
	public static final int STATIC_MESH_2D = 76690510; 			//   Version = 0
	public static final int DYNAMIC_MESH_2D = 76690511; 		//   Version = 1
	public static final int STATIC_MESH_3D = 76690512; 			//   Version = 2
	public static final int DYNAMIC_MESH_3D = 76690513; 		//   Version = 3

																// ID: RPTTP = 7677090
	public static final int REPORT_TYPE_ALL = 76770900; 		//   Version = 0
	public static final int REPORT_TYPE_SUMMARY = 76770901; 	//   Version = 1
	public static final int REPORT_TYPE_NONE = 76770902; 		//   Version = 2

																// ID: SDRTP = 7675427
	public static final int SHADER_TYPE_VERTEX = 76754270; 		//   Version = 0
	public static final int SHADER_TYPE_FRAGMENT = 76754271; 	//   Version = 1
	public static final int SHADER_TYPE_GEOMETRY = 76754272; 	//   Version = 2

																// ID: GLVRS = 9014103
	public static final int GL_VERSION_11 = 90141030; 			//   Version = 0
	public static final int GL_VERSION_12 = 90141031; 			//   Version = 1
	public static final int GL_VERSION_13 = 90141032; 			//   Version = 2
	public static final int GL_VERSION_14 = 90141033; 			//   Version = 3
	public static final int GL_VERSION_15 = 90141034; 			//   Version = 4
	public static final int GL_VERSION_20 = 90141035; 			//   Version = 5
	public static final int GL_VERSION_21 = 90141036; 			//   Version = 6
	public static final int GL_VERSION_30 = 90141037; 			//   Version = 7
	public static final int GL_VERSION_31 = 90141038; 			//   Version = 8
	public static final int GL_VERSION_32 = 90141039; 			//   Version = 9
	public static final int GL_VERSION_33 = 901410310; 			//   Version = 10
	public static final int GL_VERSION_40 = 901410311; 			//   Version = 11
	public static final int GL_VERSION_41 = 901410312; 			//   Version = 12
	public static final int GL_VERSION_42 = 901410313; 			//   Version = 13
	public static final int GL_VERSION_43 = 901410314; 			//   Version = 14
	public static final int GL_VERSION_44 = 901410315; 			//   Version = 15
	public static final int GL_VERSION_45 = 901410316; 			//   Version = 16

																// ID: GUIUN = 6773397
	public static final int GUI_UNIT_PIXELS = 67733970; 		//   Version = 0
	public static final int GUI_UNIT_RPIXELS = 67733971; 		//   Version = 1
	public static final int GUI_UNIT_PERCENT = 67733972; 		//   Version = 2

																// ID: ANGUN = 6771857
	public static final int ANGLE_UNIT_DEGREES = 67718570; 		//   Version = 0
	public static final int ANGLE_UNIT_RADIANS = 67718571;		//   Version = 1

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
	 * @param percentMax
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
	 * Converts the given vector of values to pixels based on its units and the values of
	 * <code>maxWidth</code> and <code>rpixRatio</code>. <code>maxWidth</code>
	 * corresponds the the value returned when <code>value</code> = 100%.
	 * <code>rpixRatio</code> is multiplied by the given value when the value is
	 * in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param percentMax
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
		if(valueUnits.length != 2)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		return new Vec2f(x, y);
	}
	
	/**
	 * Converts the given vector of values to pixels based on its units and the values of
	 * <code>maxWidth</code> and <code>rpixRatio</code>. <code>maxWidth</code>
	 * corresponds the the value returned when <code>value</code> = 100%.
	 * <code>rpixRatio</code> is multiplied by the given value when the value is
	 * in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param percentMax
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
		if(valueUnits.length != 3)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		float z = toPixels(values.z, valueUnits[2], maxWidth, rpixRatio);
		return new Vec3f(x, y, z);
	}
	
	/**
	 * Converts the given vector of values to pixels based on its units and the values of
	 * <code>maxWidth</code> and <code>rpixRatio</code>. <code>maxWidth</code>
	 * corresponds the the value returned when <code>value</code> = 100%.
	 * <code>rpixRatio</code> is multiplied by the given value when the value is
	 * in <code>rpx</code>.
	 * 
	 * @param values
	 *            The values to convert to pixels.
	 * @param valueUnits
	 *            The units the given value is in.
	 * @param percentMax
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
		if(valueUnits.length != 4)
			throw new IllegalArgumentException("There must be the same number of units as values.");
		
		float x = toPixels(values.x, valueUnits[0], maxWidth, rpixRatio);
		float y = toPixels(values.y, valueUnits[1], maxWidth, rpixRatio);
		float z = toPixels(values.y, valueUnits[2], maxWidth, rpixRatio);
		float w = toPixels(values.y, valueUnits[3], maxWidth, rpixRatio);
		return new Vec4f(x, y, z, w);
	}
}
