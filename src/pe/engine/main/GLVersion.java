package pe.engine.main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import pe.engine.main.PE;

public class GLVersion {
	
	private static int GLVersion;
	private static boolean initialized = false;

	public static void loadVersion() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		long versionWindow = GLFW.glfwCreateWindow(1, 1, "", MemoryUtil.NULL, MemoryUtil.NULL);
		GLFW.glfwMakeContextCurrent(versionWindow);
		GL.createCapabilities();
		GLCapabilities glCapabilities = GL.getCapabilities();
		GLFW.glfwDestroyWindow(versionWindow);
		
		if(glCapabilities.OpenGL45){
			GLVersion = PE.GL_VERSION_45;
		}else if(glCapabilities.OpenGL44){
			GLVersion = PE.GL_VERSION_44;
		}else if(glCapabilities.OpenGL43){
			GLVersion = PE.GL_VERSION_43;
		}else if(glCapabilities.OpenGL42){
			GLVersion = PE.GL_VERSION_42;
		}else if(glCapabilities.OpenGL41){
			GLVersion = PE.GL_VERSION_41;
		}else if(glCapabilities.OpenGL40){
			GLVersion = PE.GL_VERSION_40;
		}else if(glCapabilities.OpenGL33){
			GLVersion = PE.GL_VERSION_33;
		}else if(glCapabilities.OpenGL32){
			GLVersion = PE.GL_VERSION_32;
		}else if(glCapabilities.OpenGL31){
			GLVersion = PE.GL_VERSION_31;
		}else if(glCapabilities.OpenGL30){
			GLVersion = PE.GL_VERSION_30;
		}else if(glCapabilities.OpenGL21){
			GLVersion = PE.GL_VERSION_21;
		}else if(glCapabilities.OpenGL20){
			GLVersion = PE.GL_VERSION_20;
		}else if(glCapabilities.OpenGL15){
			GLVersion = PE.GL_VERSION_15;
		}else if(glCapabilities.OpenGL14){
			GLVersion = PE.GL_VERSION_14;
		}else if(glCapabilities.OpenGL13){
			GLVersion = PE.GL_VERSION_13;
		}else if(glCapabilities.OpenGL12){
			GLVersion = PE.GL_VERSION_12;
		}else if(glCapabilities.OpenGL11){
			GLVersion = PE.GL_VERSION_11;
		}else{
			GLVersion = PE.NULL;
		}
		
		initialized = true;
	}
	
	public static int getGLVersion(){
		return GLVersion;
	}
	
	public static boolean isBefore(int glVersion){
		return GLVersion < glVersion;
	}
	
	public static boolean isAfter(int glVersion){
		return glVersion <= GLVersion;
	}
	
	public static void checkAfter(int glVersion, String reason){
		if(isBefore(glVersion))
			throw new IllegalStateException("OpenGL " + versionName(glVersion) + " or above must be used");
	}
	
	public static void checkAfter(int glVersion){
		checkAfter(glVersion, ".");
	}
	
	public static String versionName(){
		return versionName(GLVersion);
	}
	
	public static String versionName(int glVersion){
		switch(glVersion){
		case PE.GL_VERSION_11:
			return "1.1";
		case PE.GL_VERSION_12:
			return "1.2";
		case PE.GL_VERSION_13:
			return "1.3";
		case PE.GL_VERSION_14:
			return "1.4";
		case PE.GL_VERSION_15:
			return "1.5";
		case PE.GL_VERSION_20:
			return "2.0";
		case PE.GL_VERSION_21:
			return "2.1";
		case PE.GL_VERSION_30:
			return "3.0";
		case PE.GL_VERSION_31:
			return "3.1";
		case PE.GL_VERSION_32:
			return "3.2";
		case PE.GL_VERSION_33:
			return "3.3";
		case PE.GL_VERSION_40:
			return "4.0";
		case PE.GL_VERSION_41:
			return "4.1";
		case PE.GL_VERSION_42:
			return "4.2";
		case PE.GL_VERSION_43:
			return "4.3";
		case PE.GL_VERSION_44:
			return "4.4";
		case PE.GL_VERSION_45:
			return "4.5";
		}
		return "NULL";
	}
}
