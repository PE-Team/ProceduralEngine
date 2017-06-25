package pe.engine.main;

public class PE {

	/*
	 * Each Global Constant has an ID and a Version. The ID is a number which
	 * groups like variables together. The version is a number which is added
	 * onto the ID to separate the IDs. An ID is created by inputing a String
	 * (max 5 characters) in the StringToIntConverter and using the
	 * CHARSET_LETTERS charset
	 */

	// @formatter:off
																		// MAX_ID = 2147483647 (10 digits)
	
																		// ID: NULL = 18337
	public static final int NULL = 183370; 								//  Version = 0

																		// ID: TEXDM = 6027366
	public static final int TEXTURE_1D = 60273660; 						//   Version = 0
	public static final int TEXTURE_2D = 60273661; 						//   Version = 1
	public static final int TEXTURE_3D = 60273662; 						//   Version = 2
	
																		// ID: TEXWR = 8646190
	public static final int TEXTURE_WRAP_REPEAT = 86461900; 			//   Version = 0
	public static final int TEXTURE_WRAP_MIRROR = 86461901; 			//   Version = 1
	public static final int TEXTURE_WRAP_CLAMP = 86461902; 				//   Version = 2
	public static final int TEXTURE_WRAP_BORDER = 86461903; 			//   Version = 3
	
																		// ID: MMFTR = 8581495	
	public static final int MIPMAP_FILTER_SIGLE_PIXELATED = 85814950;	//   Version = 0
	public static final int MIPMAP_FILTER_BILINEAR = 85814951;			//   Version = 1
	public static final int MIPMAP_FILTER_DUO_PIXELATED = 85814952;		//   Version = 2
	public static final int MIPMAP_FILTER_TRILINEAR = 85814953;			//   Version = 3

																		// ID: MSHTP = 7669051
	public static final int STATIC_MESH_2D = 76690510; 					//   Version = 0
	public static final int DYNAMIC_MESH_2D = 76690511; 				//   Version = 1
	public static final int STATIC_MESH_3D = 76690512; 					//   Version = 2
	public static final int DYNAMIC_MESH_3D = 76690513; 				//   Version = 3

																		// ID: RPTTP = 7677090
	public static final int REPORT_TYPE_ALL = 76770900; 				//   Version = 0
	public static final int REPORT_TYPE_SUMMARY = 76770901; 			//   Version = 1
	public static final int REPORT_TYPE_NONE = 76770902; 				//   Version = 2

																		// ID: SDRTP = 7675427
	public static final int SHADER_TYPE_VERTEX = 76754270; 				//   Version = 0
	public static final int SHADER_TYPE_FRAGMENT = 76754271; 			//   Version = 1
	public static final int SHADER_TYPE_GEOMETRY = 76754272; 			//   Version = 2

																		// ID: GLVRS = 9014103
	public static final int GL_VERSION_11 = 90141030; 					//   Version = 0
	public static final int GL_VERSION_12 = 90141031; 					//   Version = 1
	public static final int GL_VERSION_13 = 90141032; 					//   Version = 2
	public static final int GL_VERSION_14 = 90141033; 					//   Version = 3
	public static final int GL_VERSION_15 = 90141034; 					//   Version = 4
	public static final int GL_VERSION_20 = 90141035; 					//   Version = 5
	public static final int GL_VERSION_21 = 90141036; 					//   Version = 6
	public static final int GL_VERSION_30 = 90141037; 					//   Version = 7
	public static final int GL_VERSION_31 = 90141038; 					//   Version = 8
	public static final int GL_VERSION_32 = 90141039; 					//   Version = 9
	public static final int GL_VERSION_33 = 901410310; 					//   Version = 10
	public static final int GL_VERSION_40 = 901410311; 					//   Version = 11
	public static final int GL_VERSION_41 = 901410312; 					//   Version = 12
	public static final int GL_VERSION_42 = 901410313; 					//   Version = 13
	public static final int GL_VERSION_43 = 901410314; 					//   Version = 14
	public static final int GL_VERSION_44 = 901410315; 					//   Version = 15
	public static final int GL_VERSION_45 = 901410316; 					//   Version = 16
	
	
																		// ID: MSEVT = 9530079
	public static final int MOUSE_ACTION_PRESS = 95300790; 				//   Version = 0
	public static final int MOUSE_ACTION_RELEASE = 95300791; 			//   Version = 1
	public static final int MOUSE_ACTION_DRAG = 95300792; 				//   Version = 2
	public static final int MOUSE_ACTION_HOVER = 95300793; 				//   Version = 3
	public static final int MOUSE_ACTION_SCROLL = 95300794;				//   Version = 4
	public static final int MOUSE_ACTION_MIN = MOUSE_ACTION_PRESS; 		//   Version = Version 0
	public static final int MOUSE_ACTION_MAX = MOUSE_ACTION_SCROLL;		//   Version = Version 4
	
																		// ID: MSBTN = 6751043
	public static final int MOUSE_BUTTON_1 = 67510430; 					//   Version = 0
	public static final int MOUSE_BUTTON_2 = 67510431; 					//   Version = 1
	public static final int MOUSE_BUTTON_3 = 67510432; 					//   Version = 2
	public static final int MOUSE_BUTTON_4 = 67510433; 					//   Version = 3
	public static final int MOUSE_BUTTON_5 = 67510434; 					//   Version = 4
	public static final int MOUSE_BUTTON_6 = 67510435; 					//   Version = 5
	public static final int MOUSE_BUTTON_7 = 67510436; 					//   Version = 6
	public static final int MOUSE_BUTTON_8 = 67510437; 					//   Version = 7
	public static final int MOUSE_BUTTON_LEFT = MOUSE_BUTTON_1;			//   Version = Version 0
	public static final int MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_2;		//   Version = Version 1
	public static final int MOUSE_BUTTON_RIGHT = MOUSE_BUTTON_3;		//   Version = Version 2
	public static final int MOUSE_BUTTON_MIN = MOUSE_BUTTON_1;			//   Version = Version 0
	public static final int MOUSE_BUTTON_MAX = MOUSE_BUTTON_8;			//   Version = Version 7

																		// ID: GUIUN = 6773397
	public static final int GUI_UNIT_PIXELS = 67733970; 				//   Version = 0
	public static final int GUI_UNIT_RPIXELS = 67733971; 				//   Version = 1
	public static final int GUI_UNIT_PERCENT = 67733972; 				//   Version = 2
	
																		// ID: GUIEV = 10147989
	public static final int GUI_EVENT_ON_PRESS = 101479890; 			//   Version = 0
	public static final int GUI_EVENT_ON_RELEASE = 101479891; 			//   Version = 1
	public static final int GUI_EVENT_ON_CLICK = 101479892; 			//   Version = 2
	public static final int GUI_EVENT_ON_DRAG = 101479893; 				//   Version = 3
	public static final int GUI_EVENT_ON_HOVER = 101479894; 			//   Version = 4
	public static final int GUI_EVENT_ON_TYPE = 101479895; 				//   Version = 5

																		// ID: ANGUN = 6771857
	public static final int ANGLE_UNIT_DEGREES = 67718570; 				//   Version = 0
	public static final int ANGLE_UNIT_RADIANS = 67718571;				//   Version = 1
	
	// @formatter:on
}
