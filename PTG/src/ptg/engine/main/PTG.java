package ptg.engine.main;

public class PTG {
	
	/*
	 *  Each Global Constant has an ID and a Version.
	 *  The ID is a number which groups like variables together.
	 *  The version is a number which is added onto the ID to separate
	 *  the IDs. An ID is created by inputing a String (max 5 characters)
	 *  in the StringToIntConverter and using the CHARSET_LETTERS charset
	 */
	
	//ID: EXCPT = 475322
	public static final int EXCEPTION_ALL = 4753220;// Version = 0
	
	//ID: MESH = 18323
	public static final int MESH_2D = 183230;// Version = 0
	public static final int MESH_3D = 183231;// Version = 1
	
	//ID: RPTTP = 475344
	public static final int REPORT_TYPE_ALL = 4753440;// Version = 0;
	public static final int REPORT_TYPE_SUMMARY = 4753441;// Version = 1;
	public static final int REPORT_TYPE_NONE = 4753442;// Version = 2;
	
	public static final char[] CHARSET_LETTERS = {
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
	};
	
	public static final char[] CHARSET_NUMBERS = {
			'0','1','2','3','4','5','6','7','8','9'
	};
	
	public static final char[] CHARSET_HEX = {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f'
	};
	
	public static final char[] CHARSET_NUMBERS_LETTERS = {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
	};
}
