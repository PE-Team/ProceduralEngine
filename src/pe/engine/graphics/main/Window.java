package pe.engine.graphics.main;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import pe.engine.main.PE;

public class Window implements DisposableResource{
	
	private int width, height;
	private String title;
	private long id;
	private GLFWKeyCallback keyHandler;
	private boolean vsync;

	public Window(int width, int height, String title, boolean vsync, boolean resizeable, GLFWKeyCallback keyHandler){
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		this.keyHandler = keyHandler;
		
		GLFW.glfwDefaultWindowHints();
		if(GLVersion.isAfter(PE.GL_VERSION_32)){
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		/* Should be the correct implementation, but currently broken */
//			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
//			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
//			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		}else if(GLVersion.isAfter(PE.GL_VERSION_21)){
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		}else{
			throw new RuntimeException("OpenGL Version " + GLVersion.versionName() + " is not supported.\nPlease change to another version or update your graphics driver.");
		}
		if(resizeable){
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		}else{
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		}
		
		this.id = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.id == MemoryUtil.NULL){
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Center the window
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(id, (vidMode.width() - 640) / 2, (vidMode.height() - 480) / 2);
	
		show();
		GL.createCapabilities();
		
		setVSync(vsync);
		
		Resources.add(this);
	}
	
	public void setKeyHandler(GLFWKeyCallback keyHandler){
		this.keyHandler = keyHandler;
		GLFW.glfwSetKeyCallback(id, keyHandler);
	}
	
	public void show(){
		GLFW.glfwMakeContextCurrent(id);
	}
	
	public boolean shouldClose(){
		return GLFW.glfwWindowShouldClose(id);
	}
	
	public void buffersToFrameSize(IntBuffer width, IntBuffer height){
		GLFW.glfwGetFramebufferSize(id, width, height);
	}
	
	public void dispose(){
		GLFW.glfwDestroyWindow(id);
	}
	
	public void setTitle(String title){
		GLFW.glfwSetWindowTitle(id, title);
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public long getID(){
		return id;
	}
	
	public void update(){
		GLFW.glfwSwapBuffers(id);
		GLFW.glfwPollEvents();
	}
	
	public void setVSync(boolean vsync){
		this.vsync = vsync;
		if(vsync){
			GLFW.glfwSwapInterval(1);
		}else{
			GLFW.glfwSwapInterval(0);
		}
	}
}
