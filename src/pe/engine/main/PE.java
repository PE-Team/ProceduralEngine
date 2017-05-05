package pe.engine.main;

public class PE {
	
	/*
	 *  Each Global Constant has an ID and a Version.
	 *  The ID is a number which groups like variables together.
	 *  The version is a number which is added onto the ID to separate
	 *  the IDs. An ID is created by inputing a String (max 5 characters)
	 *  in the StringToIntConverter and using the CHARSET_LETTERS charset
	 */
	
															//	 ID: NULL = 18337
	public static final int NULL = 183370;					//    Version = 0
	
															//	ID: MTHTP = 475331
	public static final int MATH_TYPE_FLOAT = 4753310;		//    Version = 0
	public static final int MATH_TYPE_MATRIX4F = 4753311;	//    Version = 1
	
	
															//	ID: EXCPT = 475322
	public static final int EXCEPTION_ALL = 4753220;		//	  Version = 0
	
															//	ID: MSHTP = 475330
	public static final int STATIC_MESH_2D = 4753300;		//	  Version = 0
	public static final int DYNAMIC_MESH_2D = 4753301;		//	  Version = 1
	public static final int STATIC_MESH_3D = 4753302;		//	  Version = 2
	public static final int DYNAMIC_MESH_3D = 4753303;		//	  Version = 3
	
															//	ID: RPTTP = 475344
	public static final int REPORT_TYPE_ALL = 4753440;		//	  Version = 0
	public static final int REPORT_TYPE_SUMMARY = 4753441;	//	  Version = 1
	public static final int REPORT_TYPE_NONE = 4753442;		//	  Version = 2
	
															//  ID: SDRTP = 475331
	public static final int SHADER_TYPE_VERTEX = 4753310;	//	  Version = 0
	public static final int SHADER_TYPE_FRAGMENT = 4753311;	//	  Version = 1
	public static final int SHADER_TYPE_GEOMETRY = 4753312;	//	  Version = 2
	
															//  ID: GLVRS = 475332
	public static final int GL_VERSION_11 = 4753320;		//	  Version = 0
	public static final int GL_VERSION_12 = 4753321;		//	  Version = 1
	public static final int GL_VERSION_13 = 4753322;		//	  Version = 2
	public static final int GL_VERSION_14 = 4753323;		//	  Version = 3
	public static final int GL_VERSION_15 = 4753324;		//	  Version = 4
	public static final int GL_VERSION_20 = 4753325;		//	  Version = 5
	public static final int GL_VERSION_21 = 4753326;		//	  Version = 6
	public static final int GL_VERSION_30 = 4753327;		//	  Version = 7
	public static final int GL_VERSION_31 = 4753328;		//	  Version = 8
	public static final int GL_VERSION_32 = 4753329;		//	  Version = 9
	public static final int GL_VERSION_33 = 47533210;		//	  Version = 10
	public static final int GL_VERSION_40 = 47533211;		//	  Version = 11
	public static final int GL_VERSION_41 = 47533212;		//	  Version = 12
	public static final int GL_VERSION_42 = 47533213;		//	  Version = 13
	public static final int GL_VERSION_43 = 47533214;		//	  Version = 14
	public static final int GL_VERSION_44 = 47533215;		//	  Version = 15
	public static final int GL_VERSION_45 = 47533216;		//	  Version = 16
}
